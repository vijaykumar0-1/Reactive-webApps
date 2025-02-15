package com.learning.reactive.basic.controller;

import com.learning.reactive.basic.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("traditional")
public class TraditionalController {

    private static final Logger logger = LoggerFactory.getLogger(TraditionalController.class);
    private final RestClient restClient = RestClient.builder()
            .requestFactory(new JdkClientHttpRequestFactory())
            .baseUrl("http://localhost:7070").build();

    @GetMapping("/product")
    public List<Product> getProduct()
    {
        var productList = this.restClient
                .get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
        });
        logger.info("received response {}", productList);
        return productList;
    }
}
