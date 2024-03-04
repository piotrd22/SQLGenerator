package com.example.sqlgenerator.services.db;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DbService {
    Set<String> getDbNames();
    Set<String> getDbSchemasByName(String name);
    String getDbTypeByName(String name);

//  Key is the name of the table and list is the list of columns and their types
    Map<String, List<String>> getDbStructureByName(String name, String schemaName);
}
