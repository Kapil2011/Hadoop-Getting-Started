package com.kapildas.afpvalidator.config;

import java.util.ResourceBundle;

public enum ApplicationConfiguration {
    JDBC_URL, JDBC_USER, JDBC_PASSWORD, FILE_NAME_PATTERN, DUP_CORP_QUERY, CORP_VALIDATION_QUERY;

    private final ResourceBundle applicationConfig = ResourceBundle.getBundle("application");

    public String getValue() {
        return applicationConfig.getString(name());
    }
}
