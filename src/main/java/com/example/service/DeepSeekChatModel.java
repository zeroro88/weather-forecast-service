package com.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import okhttp3.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DeepSeekChatModel implements ChatLanguageModel {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json");
    private final String apiKey;
    private final String modelName;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    private DeepSeekChatModel(String apiKey, String modelName) {
        this.apiKey = apiKey;
        this.modelName = modelName;
        this.client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
        this.mapper = new ObjectMapper();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String apiKey;
        private String modelName = "deepseek-chat";

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public DeepSeekChatModel build() {
            return new DeepSeekChatModel(apiKey, modelName);
        }
    }

    @Override
    public Response<AiMessage> generate(List<ChatMessage> messages) {
        StringBuilder prompt = new StringBuilder();
        for (ChatMessage message : messages) {
            prompt.append(message.text()).append("\n");
        }
        
        try {
            String jsonBody = mapper.writeValueAsString(Map.of(
                "model", modelName,
                "messages", List.of(Map.of(
                    "role", "user",
                    "content", prompt.toString()
                ))
            ));

            Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(jsonBody, JSON))
                .build();

            okhttp3.Response httpResponse = client.newCall(request).execute();
            String responseBody = httpResponse.body().string();
            httpResponse.close();
            
            Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);
            String content = ((Map)((Map)((List)responseMap.get("choices")).get(0)).get("message")).get("content").toString();
            return Response.from(new AiMessage(content));
        } catch (Exception e) {
            throw new RuntimeException("调用 DeepSeek API 失败", e);
        }
    }
} 