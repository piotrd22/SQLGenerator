package com.example.sqlgenerator.services.db;

import com.example.sqlgenerator.config.AbstractDbServiceBeanFactory;
import com.example.sqlgenerator.config.DataSourceProperties;
import com.example.sqlgenerator.enums.SupportedDbType;
import com.example.sqlgenerator.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DbServiceImpl implements DbService {

    private final DataSourceProperties dataSourceProperties;
    private final AbstractDbServiceBeanFactory abstractDbServiceBeanFactory;

    public DbServiceImpl(DataSourceProperties dataSourceProperties, AbstractDbServiceBeanFactory abstractDbServiceBeanFactory) {
        this.dataSourceProperties = dataSourceProperties;
        this.abstractDbServiceBeanFactory = abstractDbServiceBeanFactory;
    }

    @Override
    public Set<String> getDbNames() {
        return dataSourceProperties.getDataSources().keySet();
    }

    @Override
    public Set<String> getDbSchemasByName(String name) {
        AbstractDbService dbService = getDbServiceByDbName(name);
        return dbService.getDbSchemasByName(name);
    }

    @Override
    public Map<String, List<String>> getDbStructureByName(String name, String schemaName) {
        AbstractDbService dbService = getDbServiceByDbName(name);
        return dbService.getDbStructureByName(name, schemaName);
    }

    @Override
    public String getDbTypeByName(String name) {
        Map<String, String> ds = getDataSourceByName(name);
        return ds.get("type");
    }

    private AbstractDbService getDbServiceByDbName(String name) {
        String dbType = getDbTypeByName(name);
        SupportedDbType supportedDbType = SupportedDbType.fromString(dbType);
        return abstractDbServiceBeanFactory.getAbstractDbServiceBean(supportedDbType);
    }

    private Map<String, String> getDataSourceByName(String name) {
        Map<String, String> ds = dataSourceProperties.getDataSources().get(name);
        if (ds == null) {
            throw new NotFoundException("DataSource with name '%s' not found.".formatted(name));
        }
        return ds;
    }
}
