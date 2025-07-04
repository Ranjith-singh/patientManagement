package com.patientManagement.integrationTests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class PatientIntegrationTest {

    @BeforeAll()
    public static void setup(){
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnPatientWithValidToken(){
        String loginPayload = """
                {
                "email" : "testuser@test.com",
                "password" : "password123"
                }
                """;

        String token = given()
        .contentType("application/json")
        .body(loginPayload)
        .when()
        .post("/auth/login")
        .then()
        .statusCode(200)
        .body("token", notNullValue())
        .extract()
        .jsonPath()
        .get("token");

        given()
        .header("Authorization", "Bearer " + token)
        .when()
        .get("/api/patient")
        .then()
        .statusCode(200)
        .body("patients", notNullValue());
    }
}
