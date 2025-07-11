package com.patientManagement.integrationTests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class AuthIntegrationTest {

    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOkWithValidToken(){
        String loginPayload = """
            {
                "email" : "testuser@test.com",
                "password" : "password123"
            }
        """;

        Response response = given()
        .contentType("application/json")
        .body(loginPayload)
        .when()
        .post("/auth/login")
        .then()
        .statusCode(200)
        .body("token", notNullValue())
        .extract()
        .response();

        System.out.println("Generated Token: "+ response.jsonPath().getString("token"));
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin(){
        String payload = """
                {
                "email" : "wronguser@test.com",
                "password" : "wrongPassword124"
            }
                """;

        given()
            .contentType("application/json")
            .body(payload)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(401);
    }
}
