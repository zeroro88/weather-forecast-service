package com.example.service;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {
    private final ChatLanguageModel chatModel;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    public WeatherService(@Value("${deepseek.api.key}") String apiKey) {
        this.chatModel = DeepSeekChatModel.builder()
                .apiKey(apiKey)
                .modelName("deepseek-chat")
                .build();
    }

    public String checkWeather(String query) {
        LocalDateTime now = LocalDateTime.now();
        String today = now.format(dateFormatter);
        String tomorrow = now.plusDays(1).format(dateFormatter);

        String prompt = String.format("""
             你是一个专业的天气预报员。今天是%s。
             请针对用户的问题"%s"进行回答。
             要求：
             1. 从问题中提取出地点和时间信息
             2. 根据地理位置、季节特点和天气规律进行预测
             3. 明确说明是否会下雨，如果会下雨，说明大概时间段
             4. 给出温度范围和实用建议
             5. 在回答中明确指出具体日期（如：3月15日）
             6. 用简短的口语化中文回答，像真实的天气预报员那样
             """, today, query);
        return chatModel.generate(Collections.singletonList(new UserMessage(prompt)))
                .content()
                .text();
    }
} 