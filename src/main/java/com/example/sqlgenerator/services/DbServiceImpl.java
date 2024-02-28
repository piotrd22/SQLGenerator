package com.example.sqlgenerator.services;

import com.example.sqlgenerator.config.DataSourceFactory;
import com.example.sqlgenerator.config.DataSourceProperties;
import com.example.sqlgenerator.exceptions.NotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class DbServiceImpl implements DbService {

    private final DataSourceFactory dataSourceFactory;
    private final DataSourceProperties dataSourceProperties;

    public DbServiceImpl(DataSourceFactory dataSourceFactory, DataSourceProperties dataSourceProperties) {
        this.dataSourceFactory = dataSourceFactory;
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public Set<String> getDbNames() {
        return dataSourceProperties.getDataSources().keySet();
    }

    @Override
    public Set<String> getDbSchemasByName(String name) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(name);
        Set<String> dbSchemas = new HashSet<>();

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getSchemas();

            while (rs.next()) {
                String schemaName = rs.getString("TABLE_SCHEM");
                dbSchemas.add(schemaName);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dbSchemas;
    }

    @Override
    public String getDbStructureByName(String name, String schemaName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(name);

        StringBuilder result = new StringBuilder();

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();

            isSchemaExists(schemaName, metaData);

            try (ResultSet rs = metaData.getColumns(null, schemaName, null, null)) {
                String currentTableName = null;
                StringBuilder currentColumns = new StringBuilder();

                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String columnName = rs.getString("COLUMN_NAME");
                    String dataType = rs.getString("DATA_TYPE");

                    if (!tableName.equals(currentTableName)) {
                        if (currentTableName != null) {
                            result.append("Table: ").append(currentTableName).append(", Columns: ").append(currentColumns).append("\n");
                        }

                        currentTableName = tableName;
                        currentColumns = new StringBuilder();
                    }

                    currentColumns.append(columnName).append("(").append(dataType).append("), ");
                }

                if (currentTableName != null) {
                    result.append("Table: ").append(currentTableName).append(", Columns: ").append(currentColumns).append("\n");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

    @Override
    public String getDbTypeByName(String name) {
        Map<String, String> ds = getDataSourceByName(name);
        return ds.get("type");
    }

    private void isSchemaExists(String schemaName, DatabaseMetaData metaData) throws SQLException {
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

    // I create a new jdbcTemplate every time, because the user chooses for himself which database he wants to pull data from
    private JdbcTemplate getJdbcTemplate(String dataSourceName) {
        DataSource dataSource = dataSourceFactory.getDataSource(dataSourceName);
        return new JdbcTemplate(dataSource);
    }

    private Map<String, String> getDataSourceByName(String name) {
        Map<String, String> ds = dataSourceProperties.getDataSources().get(name);
        if (ds == null) {
            throw new NotFoundException("DataSource with name '%s' not found.".formatted(name));
        }
        return ds;
    }
}
