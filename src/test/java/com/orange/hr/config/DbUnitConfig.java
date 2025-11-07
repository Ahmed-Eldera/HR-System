package com.orange.hr.config;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbUnitConfig {

    @Bean
    public DatabaseConfig databaseConfig() {
        DatabaseConfig config = new DatabaseConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
        config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, false);
//        config.setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_COLUMN_NAMES,true);
        config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        return config;
    }
}
