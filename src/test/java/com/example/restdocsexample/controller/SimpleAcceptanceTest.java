package com.example.restdocsexample.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
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

import java.util.Map;

import static org.springframework.restdocs.cli.CliDocumentation.curlRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
                .addFilter(
                        documentationConfiguration(restDocumentation)
                                .snippets()
                                .withDefaults(
                                        curlRequest(),
                                        httpRequest(),
                                        httpResponse(),
                                        requestBody(),
                                        responseBody()
                                )
                )
                .build();
    }

    private Snippet simpleReadPathParameterSnippet() {
        return pathParameters(parameterWithName("id").description("아이디"));
    }

    private Snippet simpleRequestParameterSnippet() {
        return requestParameters(parameterWithName("name").description("이름"));
    }

    private Snippet simpleRequestBodySnippet() {
        return requestFields(
                fieldWithPath("name")
                        .type(JsonFieldType.STRING)
                        .description("이름")

        );
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
    void simpleRead() {
        RestDocumentationFilter restDocumentationFilter = document(
                // identifier, 이를 이용해 adoc파일을 저장할 디렉토리를 생성한다
                "simple-read",
                simpleReadPathParameterSnippet(),
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

    @Test
    void simpleCreate() {
        RestDocumentationFilter restDocumentationFilter = document(
                "simple-create",
                simpleRequestBodySnippet(),
                simpleResponseFieldsSnippet()
        );

        RequestSpecification given = RestAssured.given(this.spec)
                .baseUri(BASE_URL)
                .port(port)
                .filter(restDocumentationFilter);

        Response actual = given.when()
                .log().all()
                .body(Map.of("name", "name"))
                .contentType(ContentType.JSON)
                .post("/simple");

        actual.then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all();
    }

    @Test
    void simpleCreate2() {
        RestDocumentationFilter restDocumentationFilter = document(
                "{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                simpleRequestBodySnippet(),
                simpleResponseFieldsSnippet()
        );

        RequestSpecification given = RestAssured.given(this.spec)
                .baseUri(BASE_URL)
                .port(port)
                .filter(restDocumentationFilter);

        Response actual = given.when()
                .log().all()
                .body(Map.of("name", "name"))
                .contentType(ContentType.JSON)
                .post("/simple");

        actual.then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all();
    }
}
