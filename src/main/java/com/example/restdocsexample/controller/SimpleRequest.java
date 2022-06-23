package com.example.restdocsexample.controller;

public class SimpleRequest {

    private String name;


    public SimpleRequest() {
    }

    public SimpleRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
