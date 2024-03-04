package com.example.sqlgenerator.controllers.api;

import com.example.sqlgenerator.dtos.request.GetSqlQueryDto;
import com.example.sqlgenerator.dtos.response.AiResponseDto;
import com.example.sqlgenerator.exceptions.RateLimitExceededException;
import com.example.sqlgenerator.services.ai.AiService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final Logger logger = LoggerFactory.getLogger(AiController.class);
    private final AiService aiService;
    private final Bucket bucket;

    public AiController(AiService aiService) {
        this.aiService = aiService;

        // Bucket4j rate limiter config, the limit is 60 requests per minute
        // I do a simple configuration in the controller, because I only need it on this one endpoint, but of course you can do it differently
        Bandwidth limit = Bandwidth.builder()
                .capacity(60)
                .refillGreedy(60, Duration.ofMinutes(1))
                .build();

        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping
    public ResponseEntity<AiResponseDto> getSqlQuery(@RequestBody @Valid GetSqlQueryDto dto) {
        logger.info("Inside: AiController -> getSqlQuery()...");

        if (bucket.tryConsume(1)) {
            ChatResponse chatResponse = aiService.getSqlQuery(dto.getNlQuery(), dto.getDbName(), dto.getSchema());
            return ResponseEntity.ok(new AiResponseDto(chatResponse.getResult().getOutput().getContent()));
        }

        throw new RateLimitExceededException();
    }
}
