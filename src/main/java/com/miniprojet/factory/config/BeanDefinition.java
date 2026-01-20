package com.miniprojet.factory.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente la configuration d'un bean (Repository ou Service)
 */
public class BeanDefinition {
    private String interfaceName;
    private String className;
    private boolean singleton;
    private List<String> args;

    public BeanDefinition() {
        this.args = new ArrayList<>();
        this.singleton = true;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "interfaceName='" + interfaceName + '\'' +
                ", className='" + className + '\'' +
                ", singleton=" + singleton +
                ", args=" + args +
                '}';
    }
}