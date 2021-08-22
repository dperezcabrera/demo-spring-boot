package com.example.demo.e2e;

public class TestConfigurationException extends RuntimeException {

    public TestConfigurationException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
}
