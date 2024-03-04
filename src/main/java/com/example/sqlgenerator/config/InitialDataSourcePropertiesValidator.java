package com.example.sqlgenerator.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataSourcePropertiesValidator implements CommandLineRunner {

    private final DataSourceProperties dataSourceProperties;

    public InitialDataSourcePropertiesValidator(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public void run(String... args) {
        dataSourceProperties.validate();
    }
}
