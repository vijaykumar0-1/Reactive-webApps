package com.learning.reactive.basic.controller;

import com.learning.reactive.basic.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("reactive")
public class ReactiveController {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveController.class);
    private final WebClient webClient = WebClient.builder()
                                                    .baseUrl("http://localhost:7070")
                                                    .build();
    
    @GetMapping(value = "/product", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProduct()
    {
        return this.webClient.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> logger.info("Received: {}", product))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    logger.error("Downstream API error: Status {}, Body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
                    return Flux.empty();  // Return an empty Flux instead of failing
                })
                .onErrorResume(Exception.class, ex -> {
                    logger.error("Unexpected error occurred", ex);
                    return Flux.empty();
                });
    }
}
