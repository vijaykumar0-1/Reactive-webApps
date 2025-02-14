package com.learning.reactive.reactive.controller;

import com.learning.reactive.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
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
                .doOnNext(product -> logger.info("Received: {}", product));
    }
}
