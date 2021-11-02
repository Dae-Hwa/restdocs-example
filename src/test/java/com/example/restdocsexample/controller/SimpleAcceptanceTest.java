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
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.restdocs.snippet.Snippet;

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

    private Snippet simplePathParameterSnippet() {
        return pathParameters(parameterWithName("id").description("아이디"));
    }

    private Snippet simpleRequestParameterSnippet() {
        return requestParameters(parameterWithName("name").description("이름"));
    }

    private Snippet simpleResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("id")
                        .type(JsonFieldType.NUMBER)
                        .description("아이디"),
                fieldWithPath("name")
                        .type(JsonFieldType.STRING)
                        .description("이름")
        );
    }

    @Test
    void read() {
        RestDocumentationFilter restDocumentationFilter = document(
                // identifier, 이를 이용해 adoc파일을 저장할 디렉토리를 생성한다
                "simple-read",
                simplePathParameterSnippet(),
                simpleRequestParameterSnippet(),
                simpleResponseFieldsSnippet()
        );

        RequestSpecification given = RestAssured.given(this.spec)
                                                .baseUri(BASE_URL)
                                                .port(port)
                                                .pathParam("id", 1)
                                                .queryParam("name", "name")
                                                .filter(restDocumentationFilter);

        Response actual = given.when()
                               .get("/simple/{id}");

        actual.then()
              .statusCode(HttpStatus.OK.value())
              .log().all();
    }
}
