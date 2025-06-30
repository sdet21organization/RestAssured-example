import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class RestAssured01Tests extends TestBase {


    @Test
    public void checkBodyAsStringContainsSubstringTest() {
        Response response = given()
                .header("x-api-key", "reqres-free-v1")

                .when()
                .get("/users");

        Assertions.assertTrue(response.body().asString().contains("george.bluth@reqres.in"));
    }

    @Test
    public void checkBodySingleUserAsString() {
        Response response = given()
                .header("x-api-key", "reqres-free-v1")

                .when()
                .get("/users/2");

        Assertions.assertTrue(response.body().asString().contains("janet.weaver@reqres.in"),
                "Response body does not contain expected email");

        Assertions.assertEquals(200, response.statusCode(), "Status code is not 200");

    }

    @Test
    public void checkBodySingleUserBodyJsonpath() {
        given()
                .header("x-api-key", "reqres-free-v1")

                .when()
                .get("/users/2")
                .then()
                .body("data.first_name", equalTo("Janet"))
                .body("data.last_name", equalTo("Weaver"))
                .body("data.email", equalTo("janet.weaver@reqres.in"))
                .body("data.id", is(2));

    }


    @Test
    public void checkBodySingleUserCreate() {

        String requestBody = """
                {
                    "name": "morpheus",
                    "job": "leader"
                }
                """;

        given()
                .header("x-api-key", "reqres-free-v1")
                .contentType("application/json")
                .body(requestBody)

                .when()
                .post("/users")

                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"));

    }

    @Test
    public void checkBodySingleUserBodyJsonpath2() {

        String responseBody =

                given()
                        .header("x-api-key", "reqres-free-v1")

                        .when()
                        .get("/users/2")
                        .then()
                        .extract().asString();


        String firstName =
                from(responseBody)
                        .get("data.first_name");

        Assertions.assertEquals("Janet", firstName, "First name does not match");

    }


}