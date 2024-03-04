package com.example.sqlgenerator.services.db.oracle;

import com.example.sqlgenerator.config.AbstractDbServiceQualifier;
import com.example.sqlgenerator.config.DataSourceFactory;
import com.example.sqlgenerator.enums.SupportedDbType;
import com.example.sqlgenerator.services.db.AbstractDbService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
@AbstractDbServiceQualifier(type = SupportedDbType.ORACLE)
public class OracleDbService extends AbstractDbService {

    private static final List<String> IGNORED_SCHEMAS = Arrays.asList(
            "SYSTEM",
            "MDDATA",
            "GGSYS",
            "SYSRAC",
            "SYSDG",
            "GSMUSER",
            "OPS$ORACLE",
            "DGPDB_INT",
            "SYS$UMF",
            "GGSHAREDCAP",
            "OLAPSYS",
            "XDB",
            "CTXSYS",
            "GSMROOTUSER",
            "SYSBACKUP",
            "ANONYMOUS",
            "AUDSYS",
            "OJVMSYS",
            "DIP",
            "MDSYS",
            "SYS",
            "DBSNMP",
            "APPQOSSYS",
            "OUTLN",
            "REMOTE_SCHEDULER_AGENT",
            "DBSFWUSER",
            "SYSKM",
            "DVF",
            "XS$NULL",
            "LBACSYS",
            "WMSYS",
            "GSMADMIN_INTERNAL",
            "GSMCATUSER",
            "DVSYS"
    );

    protected OracleDbService(DataSourceFactory dataSourceFactory) {
        super(dataSourceFactory);
    }

    @Override
    public Set<String> getDbSchemasByName(String name) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(name);

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getSchemas();
            Set<String> dbSchemas = new HashSet<>();

            while (rs.next()) {
                String schemaName = rs.getString("TABLE_SCHEM");

                if (!IGNORED_SCHEMAS.contains(schemaName)) {
                    dbSchemas.add(schemaName);
                }
            }

            return dbSchemas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, List<String>> getDbStructureByName(String name, String schemaName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(name);

        try (Connection con = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            DatabaseMetaData metaData = con.getMetaData();
            isSchemaExists(schemaName, metaData);
            return getDbSchemaStructure(schemaName, con, metaData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
