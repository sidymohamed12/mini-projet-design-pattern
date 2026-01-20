package com.miniprojet.factory.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Charge la configuration depuis un fichier YAML
 */
public class YamlConfigLoader {

    private final Map<String, BeanDefinition> repositories;
    private final Map<String, BeanDefinition> services;

    public YamlConfigLoader(String configPath) {
        this.repositories = new HashMap<>();
        this.services = new HashMap<>();
        loadConfig(configPath);
    }

    @SuppressWarnings("unchecked")
    private void loadConfig(String configPath) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(configPath);

            if (inputStream == null) {
                throw new RuntimeException("Configuration file not found: " + configPath);
            }

            Map<String, Object> config = yaml.load(inputStream);

            // Charger les repositories
            if (config.containsKey("repositories")) {
                Map<String, Map<String, Object>> repoConfig = (Map<String, Map<String, Object>>) config
                        .get("repositories");

                for (Map.Entry<String, Map<String, Object>> entry : repoConfig.entrySet()) {
                    BeanDefinition bean = createBeanDefinition(
                            entry.getKey(),
                            entry.getValue());
                    repositories.put(entry.getKey(), bean);
                }
            }

            // Charger les services
            if (config.containsKey("services")) {
                Map<String, Map<String, Object>> servConfig = (Map<String, Map<String, Object>>) config.get("services");

                for (Map.Entry<String, Map<String, Object>> entry : servConfig.entrySet()) {
                    BeanDefinition bean = createBeanDefinition(
                            entry.getKey(),
                            entry.getValue());
                    services.put(entry.getKey(), bean);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    @SuppressWarnings("unchecked")
    private BeanDefinition createBeanDefinition(String interfaceName,
            Map<String, Object> config) {
        BeanDefinition bean = new BeanDefinition();
        bean.setInterfaceName(interfaceName);
        bean.setClassName((String) config.get("class"));
        bean.setSingleton(
                config.getOrDefault("singleton", true).equals(true));

        if (config.containsKey("args")) {
            bean.setArgs((List<String>) config.get("args"));
        }

        return bean;
    }

    public Map<String, BeanDefinition> getRepositories() {
        return repositories;
    }

    public Map<String, BeanDefinition> getServices() {
        return services;
    }

    public BeanDefinition getRepository(String interfaceName) {
        return repositories.get(interfaceName);
    }

    public BeanDefinition getService(String interfaceName) {
        return services.get(interfaceName);
    }
}