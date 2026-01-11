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

    @GetMapping("/words/{number}")
public List<String> getWords(@PathVariable int number) {
    if (number <= 0) {
        throw new IllegalArgumentException("Number must be greater than zero");
    }
    return wordService.getWords(number);
}
}
