package com.autominsi.springboot_ci_demo_with_aws.controller;

import java.util.List;

import com.autominsi.springboot_ci_demo_with_aws.service.WordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    // Jenkins auto-trigger verification v2

    @GetMapping("/words/{number}")
    public List<String> getWords(@PathVariable int number) {
        return wordService.getWords(number);
    }

    public String demoPmdViolation() {
    int unusedVariable = 10;   // PMD Violation: Unused variable

    return "PMD Demo";
}
    public void testException() {
    try {
        int x = 10 / 0;
    } catch (Exception e) {
        // PMD Violation: Empty catch block
    }
}

}
