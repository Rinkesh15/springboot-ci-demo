package com.autominsi.springboot_ci_demo_with_aws.controller;

import java.util.List;

import com.autominsi.springboot_ci_demo_with_aws.service.WordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WordController {

    private final WordService wordService;

    // Hardcoded password (PMD security violation)
    private static final String PASSWORD = "admin123";

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    // Jenkins auto-trigger verification v2

    @GetMapping("/words/{number}")
    public List<String> getWords(@PathVariable int number) {
        return wordService.getWords(number);
    }

    
}
