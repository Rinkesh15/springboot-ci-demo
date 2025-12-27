package com.example.springboot_ci_demo_v1.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TextProcessingRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:processText")
            .routeId("text-processing-route")
            .log("Received text for processing")
            .bean("textService", "analyzeText");
    }
}
