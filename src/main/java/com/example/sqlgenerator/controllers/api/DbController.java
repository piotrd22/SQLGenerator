package com.example.sqlgenerator.controllers.api;

import com.example.sqlgenerator.services.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/db")
public class DbController {

    private final Logger logger = LoggerFactory.getLogger(DbController.class);
    private final DbService dbService;

    public DbController(DbService dbService) {
        this.dbService = dbService;
    }

    @GetMapping
    public ResponseEntity<Set<String>> getDbNames() {
        logger.info("Inside: DbController -> getDbNames()...");
        Set<String> names = dbService.getDbNames();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/schemas/{name}")
    public ResponseEntity<Set<String>> getDbSchemasByName(@PathVariable String name) {
        logger.info("Inside: DbController -> getDbSchemasByName()...");
        Set<String> schemas = dbService.getDbSchemasByName(name);
        return ResponseEntity.ok(schemas);
    }
}
