package com.example.restdocsexample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/simple/{id}")
    public SimpleResponse simpleRead(@PathVariable long id, SimpleRequest simpleRequest) {
        return new SimpleResponse(id, simpleRequest.getName());
    }
}
