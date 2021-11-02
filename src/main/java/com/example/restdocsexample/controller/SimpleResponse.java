package com.example.restdocsexample.controller;

public class SimpleResponse {

    private long id;

    private String name;

    public SimpleResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
