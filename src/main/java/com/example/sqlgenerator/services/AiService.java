package com.example.sqlgenerator.services;

import org.springframework.ai.chat.ChatResponse;

public interface AiService {
    ChatResponse getSqlQuery(String nlQuery, String dbName, String schema);
}
