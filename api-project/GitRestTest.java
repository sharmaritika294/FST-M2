package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GitRestTest {

    RequestSpecification requestSpec;
    String token = "";
    int SSH_id;

    @BeforeClass
    public void setUp() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .addHeader("Content-Type","application/json" )
                .addHeader("Authorization", token)
                .build();
    }

    @Test
    public void addKey() {
        String SSHKey= "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8ivQdYe7ECJ7ndD4xc9tuAaCcNuULnmQ2j0dJTspVNb4U5FA3DONOmiw2Rh2Z6ck153Wf+QnwFw779Nk37/u76Juu+mwCYoMq+evddrvUm5MroAgeoZXOgIRg/zfdtvRBQ03QckziKIxufPRk0lW8zqRVkrwVFII6w9DfrIr9z0HKGfFjgw/Q7LGBqQ1D5eY6tHWBruz+N2I+m+sQZ7TlKd9qWYv2rHogjp/1lALXCcRv/ULunfugYeH53Cq8mula06f0WCpqmUzepYbcAxkBSBMAdXpiMddD5cZkCFL1Xh02weprpteVgWUbUch/dh/3EgjuB+7qpPq4ldrPzEZf";
        String reqBody = "{\"title\": \"TestAPIKey\",\"key\": \""+SSHKey+"\"}";
        Response response = given().spec(requestSpec)
                .body(reqBody)
                .when().post("/user/keys");
        SSH_id = response.then().extract().path("id");
        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(201);
    }

    @Test
    public void getRequest() {
        Response response = given().spec(requestSpec)
                .pathParam("keyId", SSH_id)
                .when().get("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(200);

    }

    @Test
    public void removeRequest() {
        Response response = given().spec(requestSpec)
                .pathParam("keyId", SSH_id)
                .when().delete("/user/keys/{keyId}");
        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(204);
    }
}
