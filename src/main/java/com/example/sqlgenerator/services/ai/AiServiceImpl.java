package com.example.sqlgenerator.services.ai;

import com.example.sqlgenerator.services.db.DbService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final DbService dbService;

    public AiServiceImpl(ChatClient chatClient, DbService dbService) {
        this.chatClient = chatClient;
        this.dbService = dbService;
    }

    @Override
    public ChatResponse getSqlQuery(String nlQuery, String dbName, String schema) {
        Map<String, List<String>> dbStructure = dbService.getDbStructureByName(dbName, schema);
        String dbType = dbService.getDbTypeByName(dbName);
        String prompt = "convert following nl query into sql query for db type: '%s' for db tables: %s: '%s'".formatted(dbType, dbStructure, nlQuery);
        System.out.println(prompt);
        return chatClient.call(new Prompt(prompt));
    }
}
