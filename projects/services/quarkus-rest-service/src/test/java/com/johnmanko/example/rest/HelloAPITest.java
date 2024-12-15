package com.johnmanko.example.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(HelloAPI.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testing Hello API")
class HelloAPITest {

    @Test
    @Order(1)
    @DisplayName("Endpoint: GET / (Hello World)")
    void sayHello() {
        String message = given()
                .accept(ContentType.TEXT)
                .when()
                .get()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        Assertions.assertEquals("Hello World", message);
    }

    @Test
    @Order(2)
    @DisplayName("Endpoint: GET /greeting (greeting.template and GREETING_SUBJECT)")
    void greeting() {
        String message = given()
                .accept(ContentType.TEXT)
                .when()
                .get("/greeting")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        Assertions.assertEquals("What's up, Doc", message);
    }


    @Test
    @Order(3)
    @DisplayName("Endpoint: GET /greeting/{subject} (greeting.template and {subject})")
    void greetingTo() {
        String message = given()
                .accept(ContentType.TEXT)
                .when()
                .get("/greeting/Moe")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        Assertions.assertEquals("What's up, Moe", message);
    }
}