package com.example.sqlgenerator.services;

import java.util.Set;

public interface DbService {
    Set<String> getDbNames();
    Set<String> getDbSchemasByName(String name);
    String getDbStructureByName(String name, String schemaName);
    String getDbTypeByName(String name);
}
