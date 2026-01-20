package com.miniprojet.factory;

import com.miniprojet.factory.config.BeanDefinition;
import com.miniprojet.factory.config.YamlConfigLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * APPROCHE 2: Factory avec Réflexion Java complète
 * Détecte automatiquement les dépendances via l'introspection du constructeur
 * Ne nécessite PAS la liste "args" dans le YAML
 */
public class ReflectionFactory {

    private static ReflectionFactory instance;
    private final YamlConfigLoader configLoader;
    private final Map<String, Object> instanceCache;

    private ReflectionFactory(String configPath) {
        this.configLoader = new YamlConfigLoader(configPath);
        this.instanceCache = new HashMap<>();
    }

    public static ReflectionFactory getInstance(String configPath) {
        if (instance == null) {
            instance = new ReflectionFactory(configPath);
        }
        return instance;
    }

    /**
     * Crée ou retourne une instance (Repository ou Service)
     * Utilise la réflexion pour détecter et résoudre les dépendances
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String interfaceName) {
        // Vérifier le cache
        if (instanceCache.containsKey(interfaceName)) {
            return (T) instanceCache.get(interfaceName);
        }

        // Chercher dans repositories puis services
        BeanDefinition beanDef = configLoader.getRepository(interfaceName);
        if (beanDef == null) {
            beanDef = configLoader.getService(interfaceName);
        }

        if (beanDef == null) {
            throw new RuntimeException("No bean configuration for: " + interfaceName);
        }

        try {
            Class<?> clazz = Class.forName(beanDef.getClassName());

            // Pour les repositories avec getInstance()
            if (hasGetInstanceMethod(clazz)) {
                Object instance = clazz.getMethod("getInstance").invoke(null);
                if (beanDef.isSingleton()) {
                    instanceCache.put(interfaceName, instance);
                }
                return (T) instance;
            }

            // Pour les services: introspection du constructeur
            Constructor<?> constructor = selectBestConstructor(clazz);
            Object[] dependencies = resolveDependenciesViaReflection(constructor);

            Object instance = constructor.newInstance(dependencies);

            // Mettre en cache si singleton
            if (beanDef.isSingleton()) {
                instanceCache.put(interfaceName, instance);
            }

            return (T) instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean: " + interfaceName, e);
        }
    }

    /**
     * Vérifie si la classe a une méthode getInstance() statique
     */
    private boolean hasGetInstanceMethod(Class<?> clazz) {
        try {
            clazz.getMethod("getInstance");
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Sélectionne le meilleur constructeur (celui avec le plus de paramètres)
     */
    private Constructor<?> selectBestConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length == 0) {
            throw new RuntimeException("No public constructor found for: " +
                    clazz.getName());
        }

        // Prendre le constructeur avec le plus de paramètres
        Constructor<?> best = constructors[0];
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() > best.getParameterCount()) {
                best = constructor;
            }
        }

        return best;
    }

    /**
     * RÉFLEXION COMPLÈTE: Analyse les paramètres du constructeur
     * et résout automatiquement les dépendances
     */
    private Object[] resolveDependenciesViaReflection(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> paramType = param.getType();

            System.out.println("  📦 Resolving dependency: " + paramType.getName());

            // Chercher la configuration correspondante
            String interfaceName = findInterfaceNameForType(paramType);

            if (interfaceName != null) {
                // Résolution récursive
                dependencies[i] = getBean(interfaceName);
            } else {
                throw new RuntimeException("Cannot resolve dependency: " +
                        paramType.getName());
            }
        }

        return dependencies;
    }

    /**
     * Trouve le nom d'interface correspondant au type de paramètre
     */
    private String findInterfaceNameForType(Class<?> paramType) {
        // Chercher dans les repositories
        for (Map.Entry<String, BeanDefinition> entry : configLoader.getRepositories().entrySet()) {
            try {
                Class<?> implClass = Class.forName(entry.getValue().getClassName());
                if (paramType.isAssignableFrom(implClass)) {
                    return entry.getKey();
                }
            } catch (ClassNotFoundException e) {
                // Ignorer
            }
        }

        // Chercher dans les services
        for (Map.Entry<String, BeanDefinition> entry : configLoader.getServices().entrySet()) {
            try {
                Class<?> implClass = Class.forName(entry.getValue().getClassName());
                if (paramType.isAssignableFrom(implClass)) {
                    return entry.getKey();
                }
            } catch (ClassNotFoundException e) {
                // Ignorer
            }
        }

        return null;
    }

}

// aop = orientee aspect