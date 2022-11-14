package activities;

import io.restassured.response.Response;
import jdk.jfr.ContentType;
import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Activity2 {
    final static String URI = "https://petstore.swagger.io/v2/user";
    String userIds;
    String username;

    @Test(priority = 1)
    public void addUserRequest() throws IOException {
        FileInputStream inputFile = new FileInputStream("src/test/java/activities/inputJsonFile1.json");
        String reqBody = new String(inputFile.readAllBytes());
        Response response = given()
                .header("Content-Type","application/json")
                .body(reqBody)
                .when().post(URI);
        inputFile.close();
        System.out.println(response.body().asPrettyString());
        username = (reqBody.split(":")[2]).split(",")[0].replace("\"","").trim();
        userIds = response.then().extract().path("message");
        response.then().statusCode(200);
        response.then().body("message",equalTo(userIds));
    }

    @Test(priority = 2)
    public void getUserRequest(){
        File resultJsonFile = new File("src/test/java/activities/outputJsonFile2.json");
        Response response = given()
                .header("Content-Type","application/json")
                .pathParam("username", username)
                .when().get(URI + "/{username}");
        String responseBody = response.getBody().asPrettyString();
        try{
            resultJsonFile.createNewFile();
            FileWriter fw = new FileWriter(resultJsonFile.getPath());
            fw.write(responseBody);
            fw.close();
        }
        catch (IOException ie){
            ie.printStackTrace();
        }
        System.out.println(response.body().asPrettyString());
        // Assertion
        response.then().body("id", equalTo(9902));
        response.then().body("username", equalTo("Ritika"));
        response.then().body("firstName", equalTo("Ritika"));
        response.then().body("lastName", equalTo("Sharma"));
        response.then().body("email", equalTo("sharmaritika@mail.com"));
        response.then().body("password", equalTo("password123"));
        response.then().body("phone", equalTo("9812763450"));

    }

    @Test(priority = 3)
    public void deleteUserRequest(){
        Response response = given()
                .header("Content-Type","application/json")
                .pathParam("username", username)
                .when().delete(URI + "/{username}");
        System.out.println(response.body().asPrettyString());
        response.then().statusCode(200);
        response.then().body("message",equalTo(username));
    }
}