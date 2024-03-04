package com.example.sqlgenerator.config;

import com.example.sqlgenerator.enums.SupportedDbType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = DataSourceProperties.PREFIX)
public class DataSourceProperties {
    public static final String PREFIX = "spring";
    private Map<String, Map<String, String>> dataSources;

    public void validate() {
        for (Map<String, String> dataSource : dataSources.values()) {
            validateField(dataSource, "url");
            validateField(dataSource, "username");
            validateField(dataSource, "password");
            validateField(dataSource, "driver-class-name");
            validateField(dataSource, "type");

            if (!SupportedDbType.isValid(dataSource.get("type"))) {
                throw new IllegalArgumentException("Unsupported database type: " + dataSource.get("type"));
            }
        }
    }

    private void validateField(Map<String, String> dataSource, String fieldName) {
        if (dataSource.get(fieldName) == null || dataSource.get(fieldName).isEmpty()) {
            throw new IllegalArgumentException("Field '" + fieldName + "' is missing or empty in data source configuration.");
        }
    }
}
