package com.example.sqlgenerator.config;

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
}
