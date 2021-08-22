package com.example.demo.e2e;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import lombok.NonNull;

public class TestConfiguration {

    private static final String CONFIG_FILE_PROPERTY_NAME = "e2e-config-file";

    public static final TestConfiguration E2E = new TestConfiguration(System.getProperty(CONFIG_FILE_PROPERTY_NAME));

    private Properties properties;

    public TestConfiguration(@NonNull String fileName) {
        this.properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(fileName)));
        } catch (IOException ex) {
            throw new TestConfigurationException("No ha podido cargarse la configuración inicial", ex);
        }
    }

    public TestConfiguration(@NonNull Properties properties) {
        this.properties = properties;
    }

    public String getServiceUrl(String serviceName) {
        return properties.getProperty(String.join(".", "service", serviceName, "url"));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
