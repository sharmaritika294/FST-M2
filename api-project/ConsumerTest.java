package liveProject;


import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    Map<String, String> reqHeaders = new HashMap<>();
    String resourcePath = "/api/users";

    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder ) {
        reqHeaders.put("Content-Type", "application/json");

        DslPart reqResBody = new PactDslJsonBody()
                .numberType("id", 123)
                .stringType("firstName", "Ritika")
                .stringType("lastName", "Sharma")
                .stringType("email", "sharmaritika294@example.com");

        return builder.given("Request to create a user")
                .uponReceiving("Request to create a user")
                .method("POST")
                .path(resourcePath)
                .headers(reqHeaders)
                .body(reqResBody)
                .willRespondWith()
                .status(201)
                .body(reqResBody)
                .toPact();
    }
    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerTest () {
        String baseURI = "http://localhost:8282";
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Ritika");
        reqBody.put("lastName", "Sharma");
        reqBody.put("email", "sharmaritika294@example.com");

        Response response = given().headers(reqHeaders).body(reqBody)
                .when().post(baseURI + resourcePath);

        System.out.println(response.getBody().asPrettyString());
        response.then().statusCode(201);
    }


}