package com.example.sqlgenerator.services.db;

import com.example.sqlgenerator.config.DataSourceFactory;
import com.example.sqlgenerator.exceptions.NotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractDbService {

    protected final DataSourceFactory dataSourceFactory;

    protected AbstractDbService(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public abstract Set<String> getDbSchemasByName(String name);

    public abstract Map<String, List<String>> getDbStructureByName(String name, String schemaName);

    // I need create a new jdbcTemplate every time, because the user chooses for himself which database he wants to pull data from
    protected JdbcTemplate getJdbcTemplate(String dataSourceName) {
        DataSource dataSource = dataSourceFactory.getDataSource(dataSourceName);
        return new JdbcTemplate(dataSource);
    }

    protected void isSchemaExists(String schemaName, DatabaseMetaData metaData) throws SQLException {
        ResultSet rsSchemas = metaData.getSchemas();
        boolean schemaExists = false;

        while (rsSchemas.next()) {
            String existingSchemaName = rsSchemas.getString("TABLE_SCHEM");
            if (existingSchemaName.equals(schemaName)) {
                schemaExists = true;
                break;
            }
        }

        if (!schemaExists) {
            throw new NotFoundException("Schema with name '%s' not found.".formatted(schemaName));
        }
    }

    protected Map<String, List<String>> getDbSchemaStructure(String schemaName, Connection con, DatabaseMetaData metaData) throws SQLException {
        try (ResultSet rs = metaData.getColumns(con.getCatalog(), schemaName, null, null)) {
            Map<String, List<String>> result = new HashMap<>();
            List<String> currentColumns = new ArrayList<>();
            String currentTableName = null;

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                String dataType = rs.getString("DATA_TYPE");

                handleTableChange(currentTableName, tableName, currentColumns, result);
                currentColumns.add(columnName + "(" + dataType + ")");

                currentTableName = tableName;
            }

            handleTableChange(currentTableName, null, currentColumns, result);

            return result;
        }
    }

    private void handleTableChange(String currentTableName, String newTableName, List<String> currentColumns, Map<String, List<String>> result) {
        if (!Objects.equals(newTableName, currentTableName)) {
            if (currentTableName != null) {
                result.put(currentTableName, new ArrayList<>(currentColumns));
                currentColumns.clear();
            }
        }
    }
}
