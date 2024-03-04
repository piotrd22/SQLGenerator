package com.example.sqlgenerator.services.db.mysql;

import com.example.sqlgenerator.config.AbstractDbServiceQualifier;
import com.example.sqlgenerator.config.DataSourceFactory;
import com.example.sqlgenerator.enums.SupportedDbType;
import com.example.sqlgenerator.services.db.AbstractDbService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

@Service
@AbstractDbServiceQualifier(type = SupportedDbType.MYSQL)
public class MySqlDbService extends AbstractDbService {

    protected MySqlDbService(DataSourceFactory dataSourceFactory) {
        super(dataSourceFactory);
    }

    // MySQL does not support schemas
    @Override
    public Set<String> getDbSchemasByName(String name) {
        return new HashSet<>();
    }

    @Override
    public Map<String, List<String>> getDbStructureByName(String name, String schemaName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(name);

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();
            return getDbSchemaStructure(null, con, metaData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
