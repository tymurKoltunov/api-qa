package com.swagger;

import com.github.javafaker.Faker;
import com.swagger.api.controller.PetController;
import com.swagger.api.controller.PetsController;
import com.swagger.petstore.models.Pet;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

public class PetCreationTests {
    static {
        requestSpecification = new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .addFilter(new AllureRestAssured())
                .addHeader("X-Tracing-Id", UUID.randomUUID().toString())
                .build();
    }
    Faker faker = new Faker();
    PetController petController = new PetController();

    @Test
    @DisplayName("Creation of a new pet via API")
    void creationOfANewPetViaAPI(){
        String targetPetName = faker.name().name();
        long targetPetId = faker.number().randomNumber();
        Pet targetPet = new Pet().name(targetPetName).id(targetPetId);

        var createdPetResponse = petController
                .addNewPetToStore(targetPet);
        Assertions.assertEquals(200, createdPetResponse.statusCode());

        var petByIdResponse = petController.getPetById(targetPet.getId());

        Pet actualPet = petByIdResponse.as(Pet.class);

        Assertions.assertEquals(targetPetName, actualPet.getName());
        assertThat(actualPet).isEqualTo(targetPet);
        assertThat(actualPet.getPhotoUrls()).contains("");

    }

    private void extracted(Pet targetPetName, Pet actualPet) {
        Assertions.assertEquals(targetPetName, actualPet.getName());
    }
}
