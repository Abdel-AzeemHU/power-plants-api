package com.AIQ.utils;

import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;
    private static ConfigLoader configLoader;

    private ConfigLoader(){
        properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
    }

    public static ConfigLoader getInstance(){
        if(configLoader == null){
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public String getUsername(){
        String prop = properties.getProperty("username");
        if(prop != null) return prop;
        else throw new RuntimeException("property username is not specified in the config.properties file");
    }

    public String getPassword(){
        String prop = properties.getProperty("password");
        if(prop != null) return prop;
        else throw new RuntimeException("property password is not specified in the config.properties file");
    }

    public String getBaseUrl(){
        String prop = properties.getProperty("BASE_URI");
        if(prop != null) return prop;
        else throw new RuntimeException("property base_url is not specified in the config.properties file");
    }

    public String getToken(){
        String prop = properties.getProperty("token");
        if(prop != null) return prop;
        else throw new RuntimeException("property token is not specified in the config.properties file");
    }

}
