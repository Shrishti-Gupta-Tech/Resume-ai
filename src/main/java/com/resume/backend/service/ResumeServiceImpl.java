package com.resume.backend.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeServiceImpl implements ResumeService {

    private ChatClient chatClient;

    public ResumeServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public Map<String, Object> generateResumeResponse(String userResumeDescription) throws  IOException {
        String promptString = this.loadPromptFromFile("resume_prompt.txt");
        String promptContent = this.putValuesToTemplate(promptString,Map.of("userDescription",userResumeDescription));
        Prompt prompt = new Prompt(promptContent);
        String response = chatClient.prompt(prompt).call().content();

        Map<String, Object> stringObjectMap =  parseMultipleResponse(response);
        return stringObjectMap;
    }

    String loadPromptFromFile(String filename) throws IOException {
        Path path = new ClassPathResource(filename).getFile().toPath();
        return Files.readString(path);
    }

    String putValuesToTemplate(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            template=template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
            return template;
}
    public static Map<String, Object> parseMultipleResponse(String response) {
        Map<String, Object> jsonResponse = new HashMap<>();

        // Extract "think" content
        int thinkStart = response.indexOf("<think>") + 7;
        int thinkEnd = response.indexOf("</think>");
        if (thinkStart != -1 + 7 && thinkEnd != -1 && thinkStart < thinkEnd) {
            String thinkContent = response.substring(thinkStart, thinkEnd).trim();
            jsonResponse.put("think", thinkContent);
        } else {
            jsonResponse.put("think", null);
        }

        // Extract JSON content
        int jsonStart = response.indexOf("```json") + 7;
        int jsonEnd = response.lastIndexOf("```");
        if (jsonStart != -1 + 7 && jsonEnd != -1 && jsonStart < jsonEnd) {
            String jsonContent = response.substring(jsonStart, jsonEnd).trim();
            try {
                Map<String, Object> dataContent = new HashMap<>();
                dataContent = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonContent, Map.class);
                jsonResponse.put("data", dataContent);
            } catch (Exception e) {
                jsonResponse.put("data", null);
                System.err.println("Invalid JSON format in the response: " + e.getMessage());
            }
        } else {
            jsonResponse.put("data", null);
        }

        return jsonResponse;
    }

}


