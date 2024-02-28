package com.example.sqlgenerator.services;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final DbService dbService;

    public AiServiceImpl(OpenAiChatClient chatClient, DbService dbService) {
        this.chatClient = chatClient;
        this.dbService = dbService;
    }

    @Override
    public ChatResponse getSqlQuery(String nlQuery, String dbName, String schema) {
        String dbStructure = dbService.getDbStructureByName(dbName, schema);
        String dbType = dbService.getDbTypeByName(dbName);
        String prompt;
        if (dbType == null) {
            prompt = "convert following nl query into sql query for db tables: %s: '%s'".formatted(dbStructure, nlQuery);
        } else {
            prompt = "convert following nl query into sql query for db type: '%s' for db tables: %s: '%s'".formatted(dbType, dbStructure, nlQuery);
        }
        System.out.println(prompt);
        return chatClient.call(new Prompt(prompt));
    }
}
