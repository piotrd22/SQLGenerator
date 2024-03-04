package com.example.sqlgenerator.config;

import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.ai.azure.openai.AzureOpenAiChatClient;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiChatClientConfig {

//  Adding ':' at the end means that these are default values and if they are not there Spring will not inject them
    @Value("${spring.openai.api-key:}")
    private String openAiApiKey;

    @Value("${spring.azure.openai.api-key:}")
    private String azureOpenAiApiKey;

    @Value("${spring.azure.openai.endpoint:}")
    private String azureOpenAiApiEndpoint;

    @Bean
    public ChatClient getChatClient() {
        if (!openAiApiKey.isEmpty()) {
            return createOpenAiChatClient();
        } else if (!azureOpenAiApiKey.isEmpty() && !azureOpenAiApiEndpoint.isEmpty()) {
            return createAzureOpenAiChatClient();
        } else {
            throw new IllegalStateException("No API key has been set for OpenAI or Azure OpenAI.");
        }
    }

    private ChatClient createOpenAiChatClient() {
        var openAiApi = new OpenAiApi(openAiApiKey);

        var chatOptions = new OpenAiChatOptions.Builder()
                .withModel("gpt-3.5-turbo")
                .withTemperature(0F)
                .withMaxTokens(200)
                .build();

        return new OpenAiChatClient(openAiApi, chatOptions);
    }

    private ChatClient createAzureOpenAiChatClient() {
        var azureOpenAiClient = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(azureOpenAiApiKey))
                .endpoint(azureOpenAiApiEndpoint)
                .buildClient();

        var chatOptions = new AzureOpenAiChatOptions.Builder()
                .withModel("gpt-3.5-turbo")
                .withTemperature(0F)
                .withMaxTokens(200)
                .build();

        return new AzureOpenAiChatClient(azureOpenAiClient, chatOptions);
    }
}
