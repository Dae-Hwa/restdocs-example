package com.example.restdocsexample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SimpleController {

    @GetMapping("/simple/{id}")
    public SimpleResponse simpleRead(@PathVariable long id, SimpleRequest simpleRequest) {
        return new SimpleResponse(id, simpleRequest.getName());
    }

    @PostMapping("/simple")
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleResponse simpleCreate(@RequestBody SimpleRequest simpleRequest) {
        return new SimpleResponse(1, simpleRequest.getName());
    }
}
