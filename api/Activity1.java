package activities;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Activity1 {

    final static String URI = "https://petstore.swagger.io/v2/pet";
    int petId;

    @Test(priority = 1)
    public void addPetRequest() {
        String reqBody = "{\"id\": 77237,\"name\": \"Riley\",\"status\": \"alive\"}";
        Response response = given()
                .header("Content-Type", "application/json")
                .body(reqBody).when().post(URI);
        System.out.println(response.body().asPrettyString());
        petId = response.then().extract().path("id");
        response.then().statusCode(200);
        response.then().body("id", equalTo(petId), "name", equalTo("Riley"), "status", equalTo("alive"));
    }

    @Test(priority = 2)
    public void getPetRequest() {
        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("petId", petId)
                .when().get(URI + "/{petId}");
        System.out.println(response.body().asPrettyString());
        response.then().statusCode(200);
        response.then().body("id", equalTo(77237), "name", equalTo("Riley"), "status", equalTo("alive"));
    }

    @Test(priority = 3)
    public void deletePetRequest() {
        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("petId", petId)
                .when().delete(URI + "/{petId}");
        System.out.println(response.body().asPrettyString());
        response.then().statusCode(200);
        response.then().body("message", equalTo("77237"), "code", equalTo(200));

    }
}