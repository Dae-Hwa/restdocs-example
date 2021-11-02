package com.example.restdocsexample.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
class SimpleAcceptanceTest {

    private static final String BASE_URL = "http://localhost";

    @LocalServerPort
    private int port;

    protected RequestSpecification spec;

    @BeforeEach
    void setUpRestDocs(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void read() {
        RequestSpecification given = RestAssured.given(this.spec)
                                                .baseUri(BASE_URL)
                                                .port(port)
                                                .pathParam("id", 1)
                                                .queryParam("name", "name");

        Response actual = given.when()
                               .filter(document(
                                       "{class_name}/{method_name}/",
                                       pathParameters(parameterWithName("id").description("아이디")),
                                       requestParameters(parameterWithName("name").description("이름")),
                                       responseFields(
                                               fieldWithPath("id")
                                                       .type(JsonFieldType.NUMBER)
                                                       .description("아이디"),
                                               fieldWithPath("name")
                                                       .type(JsonFieldType.STRING)
                                                       .description("이름")
                                       )
                               )).get("/simple/{id}");

        actual.then()
              .statusCode(HttpStatus.OK.value())
              .log().all();
    }
}
