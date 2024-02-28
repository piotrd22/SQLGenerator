package com.example.sqlgenerator.config;

import com.example.sqlgenerator.exceptions.NotFoundException;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataSourceFactory {

    private final DataSourceProperties dataSourceProperties;
    private final Map<String, DataSource> dataSourceCache;

    public DataSourceFactory(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.dataSourceCache = new ConcurrentHashMap<>();
    }

    public synchronized DataSource getDataSource(String dataSourceName) {
        DataSource cachedDataSource = dataSourceCache.get(dataSourceName);
        if (cachedDataSource != null) {
            return cachedDataSource;
        }

        Map<String, Map<String, String>> dataSources = dataSourceProperties.getDataSources();
        Map<String, String> dataSource = dataSources.get(dataSourceName);

        if (dataSource == null) {
            throw new NotFoundException("DataSource with name '%s' not found.".formatted(dataSourceName));
        }

        DataSource newDataSource = DataSourceBuilder.create(this.getClass().getClassLoader())
                .url(dataSource.get("url"))
                .password(dataSource.get("password"))
                .username(dataSource.get("username"))
                .driverClassName(dataSource.get("driver-class-name"))
                .build();

        dataSourceCache.put(dataSourceName, newDataSource);

        return newDataSource;
    }
}
