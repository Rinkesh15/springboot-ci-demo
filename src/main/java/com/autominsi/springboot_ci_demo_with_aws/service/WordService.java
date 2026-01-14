package com.autominsi.springboot_ci_demo_with_aws.service;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class WordService {

    private static final List<String> WORDS = List.of(
        "cloud", "devops", "jenkins", "spring",
        "java", "aws", "pipeline", "ci", "cd"
    );

    public List<String> getWords(int count) {

        if (count <= 0) {
            throw new IllegalArgumentException("Number must be greater than zero");
        }

        List<String> shuffled = new ArrayList<>(WORDS);
        Collections.shuffle(shuffled);

        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }
}
