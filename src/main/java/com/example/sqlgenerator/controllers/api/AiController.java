package com.example.sqlgenerator.controllers.api;

import com.example.sqlgenerator.dtos.request.GetSqlQueryDto;
import com.example.sqlgenerator.dtos.response.AiResponseDto;
import com.example.sqlgenerator.services.AiService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final Logger logger = LoggerFactory.getLogger(AiController.class);
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<AiResponseDto> getSqlQuery(@RequestBody @Valid GetSqlQueryDto dto) {
        logger.info("Inside: AiController -> getSqlQuery()...");
        ChatResponse chatResponse = aiService.getSqlQuery(dto.getNlQuery(), dto.getDbName(), dto.getSchema());
        return ResponseEntity.ok(new AiResponseDto(chatResponse.getResult().getOutput().getContent()));
    }
}
