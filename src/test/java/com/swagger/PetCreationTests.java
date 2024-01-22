package com.swagger;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.fail;

public class PetCreationTests {

    Faker faker = new Faker();

    @Test
    @DisplayName("Creation of a new pet via API")
    void creationOfANewPetViaAPI(){
        var targetId = UUID.randomUUID().toString();
        String targetPetName = faker.name().name();
        var post = petStoreApiClient()
                .body("{\n" +
                        "  \"id\": \"" + targetId + "\",\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 0,\n" +
                        "    \"name\": \"string\"\n" +
                        "  },\n" +
                        "  \"name\": \""+ targetPetName +"\",\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"string\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 0,\n" +
                        "      \"name\": \"string\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"available\"\n" +
                        "}")
                .when()
                .post("/pet");
        post.prettyPrint();

        post
                .then()
                .assertThat()
                .statusCode(200);
        String actualPetName = petStoreApiClient()
                .get("/pet/{targetId}", targetId)
                .jsonPath().getString("name");
        Assertions.assertEquals(targetPetName, actualPetName);
    }

    private static RequestSpecification petStoreApiClient() {
        return given()
                .baseUri("https://petstore.swagger.io")
                .basePath("/v2")
                .contentType(ContentType.JSON);
    }
}
