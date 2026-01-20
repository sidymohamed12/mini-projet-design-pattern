package com.miniprojet.service.impl;

import com.miniprojet.dto.ClientDTO;
import com.miniprojet.dto.CommandeDTO;
import com.miniprojet.dto.LigneCommandeDTO;
import com.miniprojet.dto.ProduitDTO;
import com.miniprojet.exception.ProductNotFoundException;
import com.miniprojet.model.*;
import com.miniprojet.repository.ICommandeRepository;
import com.miniprojet.repository.IProduitRepository;
import com.miniprojet.repository.impl.CommandeRepository;
import com.miniprojet.service.ICommandeService;
import com.miniprojet.service.IStockService;
import com.miniprojet.strategy.impl.SortieStockStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour les commandes
 * Utilise les DTOs pour la couche de présentation
 * Les repositories sont injectés via constructeur
 */
public class CommandeService implements ICommandeService {

    private final ICommandeRepository commandeRepository;
    private final IProduitRepository produitRepository;
    private final IStockService stockService;

    public CommandeService(
            ICommandeRepository commandeRepository,
            IProduitRepository produitRepository,
            IStockService stockService) {
        this.commandeRepository = commandeRepository;
        this.produitRepository = produitRepository;
        this.stockService = stockService;
    }

    @Override
    public List<CommandeDTO> listAll() {
        return commandeRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public CommandeDTO create(CommandeDTO dto) {
        Commande commande = toEntity(dto);

        // Validation et calcul du montant
        commande.calculerMontantTotal();

        // Vérifier le stock disponible
        for (LigneCommande ligne : commande.getLignes()) {
            if (ligne.getProduit().getStock().getQuantite() < ligne.getQuantite()) {
                throw new IllegalStateException(
                        "Stock insuffisant pour le produit: " + ligne.getProduit().getName());
            }
        }

        Commande saved = commandeRepository.save(commande);
        return toDTO(saved);
    }

    @Override
    public CommandeDTO clone(int id) throws ProductNotFoundException {
        Commande original = commandeRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        Commande clone = original.clone();
        Commande saved = commandeRepository.save(clone);
        return toDTO(saved);
    }

    @Override
    public CommandeDTO update(int id, CommandeDTO dto) throws ProductNotFoundException {
        Commande existing = commandeRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existing.setStatut(StatutCommande.valueOf(dto.getStatut()));
        commandeRepository.update(existing);
        return toDTO(existing);
    }

    @Override
    public void delete(int id) throws ProductNotFoundException {
        boolean removed = commandeRepository.delete(id);
        if (!removed) {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public Optional<CommandeDTO> findById(int id) {
        return commandeRepository.findById(id)
                .map(this::toDTO);
    }

    public void validerCommande(int id) throws ProductNotFoundException {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (commande.getStatut() != StatutCommande.EN_ATTENTE) {
            throw new IllegalStateException("Seules les commandes en attente peuvent être validées");
        }

        // Déduire le stock
        stockService.setStrategy(new SortieStockStrategy());
        for (LigneCommande ligne : commande.getLignes()) {
            stockService.appliquerMouvement(ligne.getProduit(), ligne.getQuantite());
        }

        commande.setStatut(StatutCommande.VALIDEE);
        commandeRepository.update(commande);
    }

    public void annulerCommande(int id) throws ProductNotFoundException {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (commande.getStatut() == StatutCommande.LIVREE ||
                commande.getStatut() == StatutCommande.ANNULEE) {
            throw new IllegalStateException("Cette commande ne peut pas être annulée");
        }

        commande.setStatut(StatutCommande.ANNULEE);
        commandeRepository.update(commande);
    }

    public List<CommandeDTO> findByClientId(int clientId) {
        CommandeRepository repo = (CommandeRepository) commandeRepository;
        return repo.findByClientId(clientId).stream()
                .map(this::toDTO)
                .toList();
    }

    public List<CommandeDTO> findByStatut(StatutCommande statut) {
        CommandeRepository repo = (CommandeRepository) commandeRepository;
        return repo.findByStatut(statut).stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Récupère l'entité Commande (pour usage interne)
     */
    public Optional<Commande> findEntityById(int id) {
        return commandeRepository.findById(id);
    }

    // Mapping DTO <-> Entity
    private CommandeDTO toDTO(Commande commande) {
        ClientDTO clientDTO = new ClientDTO.Builder()
                .id(commande.getClient().getId())
                .nom(commande.getClient().getNom())
                .prenom(commande.getClient().getPrenom())
                .email(commande.getClient().getEmail())
                .telephone(commande.getClient().getTelephone())
                .adresse(commande.getClient().getAdresse())
                .build();

        List<LigneCommandeDTO> lignesDTO = commande.getLignes().stream()
                .map(ligne -> {
                    ProduitDTO produitDTO = new ProduitDTO.Builder()
                            .id(ligne.getProduit().getId())
                            .name(ligne.getProduit().getName())
                            .description(ligne.getProduit().getDescription())
                            .price(ligne.getProduit().getPrice())
                            .build();

                    return new LigneCommandeDTO(
                            produitDTO,
                            ligne.getQuantite(),
                            ligne.getPrixUnitaire());
                })
                .toList();

        return new CommandeDTO.Builder()
                .id(commande.getId())
                .client(clientDTO)
                .lignes(lignesDTO)
                .dateCommande(commande.getDateCommande())
                .statut(commande.getStatut().name())
                .montantTotal(commande.getMontantTotal())
                .build();
    }

    private Commande toEntity(CommandeDTO dto) {
        // Récupérer le client depuis le repository
        Client client = new Client.Builder()
                .id(dto.getClient().getId())
                .nom(dto.getClient().getNom())
                .prenom(dto.getClient().getPrenom())
                .email(dto.getClient().getEmail())
                .telephone(dto.getClient().getTelephone())
                .adresse(dto.getClient().getAdresse())
                .build();

        // Créer les lignes de commande
        List<LigneCommande> lignes = dto.getLignes().stream()
                .map(ligneDTO -> {
                    Produit produit = produitRepository.findById(ligneDTO.getProduit().getId())
                            .orElseThrow(() -> new IllegalStateException(
                                    "Produit introuvable: " + ligneDTO.getProduit().getId()));

                    return new LigneCommande(produit, ligneDTO.getQuantite());
                })
                .toList();

        return new Commande.Builder()
                .id(dto.getId())
                .client(client)
                .lignes(lignes)
                .dateCommande(dto.getDateCommande() != null ? dto.getDateCommande() : LocalDateTime.now())
                .statut(dto.getStatut() != null ? StatutCommande.valueOf(dto.getStatut()) : StatutCommande.EN_ATTENTE)
                .montantTotal(dto.getMontantTotal())
                .build();
    }
}