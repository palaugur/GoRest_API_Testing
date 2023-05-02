import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class _01_GoRestUsersTests {
    int userID;
    Faker randomGenerator = new Faker();

    RequestSpecification reqSpec;


    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v2/users";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer daaf873b642bffc80dff64501c8be18dd2030d1b32da83df2380eb0a178554e6")
                .setContentType(ContentType.JSON)
                .build();


    }




    @Test(enabled = false)
    public void createUserMAP() {
        //"Authorization: Bearer daaf873b642bffc80dff64501c8be18dd2030d1b32da83df2380eb0a178554e6

        String rndFullname = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullname);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
    }

    @Test
    public void createUserClass() {

        String rndFullname = randomGenerator.name().fullName();
        String rndEmail = randomGenerator.internet().emailAddress();

        User newUser = new User();

        newUser.setName(rndFullname);
        newUser.setGender("male");
        newUser.setEmail(rndEmail);
        newUser.setStatus("active");

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createUserClass")
    public void getUserByID() {

        given()
                .spec(reqSpec)
                .when()
                .get("" + userID)

                .then()
                //.log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "getUserByID")
    public void updateUser() {
        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", "Hello World1234");

        given()
                .spec(reqSpec)
                .body(updateUser)
                .when()
                .put("" + userID)

                .then()
                .statusCode(200)
                //.log().body()
                .body("id", equalTo(userID))
                .body("name", equalTo("Hello World1234"))
        ;
    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser() {
        given()
                .spec(reqSpec)

                .when()
                .delete("" + userID)

                .then()
                //.log().body()
                .statusCode(204)



        ;
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("" + userID)

                .then()
                //.log().body()
                .statusCode(404)


        ;
    }
}
