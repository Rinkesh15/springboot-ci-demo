package com.example.springboot_ci_demo_v1.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class TextService {

    public Map<String, Integer> analyzeText(String text) {

        Map<String, Integer> result = new HashMap<>();

        if (text == null || text.trim().isEmpty()) {
            result.put("words", 0);
            result.put("sentences", 0);
            result.put("spaces", 0);
            return result;
        }

        // Word count
        int wordCount = text.trim().split("\\s+").length;

        // Sentence count
        int sentenceCount = text.split("[.!?]").length;

        // Space count
        int spaceCount = 0;
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }

        result.put("words", wordCount);
        result.put("sentences", sentenceCount);
        result.put("spaces", spaceCount);

        return result;
    }
}
