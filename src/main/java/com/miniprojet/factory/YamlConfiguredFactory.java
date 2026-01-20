package com.miniprojet.factory;

import com.miniprojet.factory.config.BeanDefinition;
import com.miniprojet.factory.config.YamlConfigLoader;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * APPROCHE 1: Factory basée sur configuration YAML avec injection manuelle
 * Utilise un cache (Map) pour les instances Singleton
 */
public class YamlConfiguredFactory {

    private static YamlConfiguredFactory instance;
    private final YamlConfigLoader configLoader;
    private final Map<String, Object> repositoryCache;
    private final Map<String, Object> serviceCache;

    private YamlConfiguredFactory(String configPath) {
        this.configLoader = new YamlConfigLoader(configPath);
        this.repositoryCache = new HashMap<>();
        this.serviceCache = new HashMap<>();
    }

    public static YamlConfiguredFactory getInstance(String configPath) {
        if (instance == null) {
            instance = new YamlConfiguredFactory(configPath);
        }
        return instance;
    }

    /**
     * Crée ou retourne une instance de Repository
     */
    @SuppressWarnings("unchecked")
    public <T> T getRepository(String interfaceName) {
        // Vérifier le cache si singleton
        if (repositoryCache.containsKey(interfaceName)) {
            return (T) repositoryCache.get(interfaceName);
        }

        BeanDefinition beanDef = configLoader.getRepository(interfaceName);
        if (beanDef == null) {
            throw new RuntimeException("No repository configuration for: " + interfaceName);
        }

        try {
            Class<?> clazz = Class.forName(beanDef.getClassName());

            // Utiliser la méthode getInstance() pour les repositories Singleton
            Object instance = clazz.getMethod("getInstance").invoke(null);

            // Mettre en cache si singleton
            if (beanDef.isSingleton()) {
                repositoryCache.put(interfaceName, instance);
            }

            return (T) instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create repository: " +
                    interfaceName, e);
        }
    }

    /**
     * Crée ou retourne une instance de Service avec injection de dépendances
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(String interfaceName) {
        // Vérifier le cache si singleton
        if (serviceCache.containsKey(interfaceName)) {
            return (T) serviceCache.get(interfaceName);
        }

        BeanDefinition beanDef = configLoader.getService(interfaceName);
        if (beanDef == null) {
            throw new RuntimeException("No service configuration for: " + interfaceName);
        }

        try {
            Class<?> clazz = Class.forName(beanDef.getClassName());

            // Résoudre les dépendances
            Object[] dependencies = resolveDependencies(beanDef.getArgs());

            // Créer l'instance avec les dépendances
            Constructor<?> constructor = findConstructor(clazz, dependencies);
            Object instance = constructor.newInstance(dependencies);

            // Mettre en cache si singleton
            if (beanDef.isSingleton()) {
                serviceCache.put(interfaceName, instance);
            }

            return (T) instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create service: " +
                    interfaceName, e);
        }
    }

    /**
     * Résout les dépendances en créant les instances nécessaires
     */
    private Object[] resolveDependencies(java.util.List<String> argTypes) {
        if (argTypes == null || argTypes.isEmpty()) {
            return new Object[0];
        }

        Object[] dependencies = new Object[argTypes.size()];

        for (int i = 0; i < argTypes.size(); i++) {
            String argType = argTypes.get(i);

            // Déterminer si c'est un repository ou un service
            if (configLoader.getRepository(argType) != null) {
                dependencies[i] = getRepository(argType);
            } else if (configLoader.getService(argType) != null) {
                dependencies[i] = getService(argType);
            } else {
                throw new RuntimeException("Cannot resolve dependency: " + argType);
            }
        }

        return dependencies;
    }

    /**
     * Trouve le constructeur approprié pour les dépendances
     */
    private Constructor<?> findConstructor(Class<?> clazz, Object[] dependencies) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == dependencies.length) {
                return constructor;
            }
        }
        throw new RuntimeException("No suitable constructor found for: " +
                clazz.getName());
    }

    /**
     * Vide les caches (utile pour les tests)
     */
    public void clearCache() {
        repositoryCache.clear();
        serviceCache.clear();
    }

    /**
     * Affiche les statistiques du cache
     */
    public void printCacheStats() {
        System.out.println("\n=== Factory Cache Statistics ===");
        System.out.println("Repositories cached: " + repositoryCache.size());
        System.out.println("Services cached: " + serviceCache.size());
        System.out.println("Total instances: " +
                (repositoryCache.size() + serviceCache.size()));
    }
}