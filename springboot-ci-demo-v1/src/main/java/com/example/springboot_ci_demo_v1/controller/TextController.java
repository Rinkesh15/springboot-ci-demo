package com.example.springboot_ci_demo_v1.controller;

import java.util.Map;

import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TextController {

    private final ProducerTemplate producerTemplate;

    public TextController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @PostMapping("/analyze")
    public Map<String, Integer> analyzeText(@RequestBody String text) {

        return producerTemplate.requestBody(
                "direct:processText",
                text,
                Map.class
        );
    }
}
