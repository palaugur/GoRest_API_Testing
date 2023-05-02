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

public class _03_GoRestCommentsTests {
    int commentID;
    String postID = "17081";
    Faker randomGenerator = new Faker();
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup() {

        baseURI = "https://gorest.co.in/public/v2/";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer daaf873b642bffc80dff64501c8be18dd2030d1b32da83df2380eb0a178554e6")
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test(priority = 1)
    public void getComments() {

        given()
                .spec(reqSpec)

                .when()
                .get("comments")

                .then()
                //.log().body()
                .statusCode(200)
        ;
    }

    @Test(priority = 2)
    public void createUserComments() {

        String randomName = randomGenerator.name().fullName();
        String randomEmail = randomGenerator.internet().emailAddress();
        String randomMessage = randomGenerator.lorem().sentence();

        Map<String, String> newComment = new HashMap<>();
        newComment.put("name", randomName);
        newComment.put("email", randomEmail);
        newComment.put("body", randomMessage);


        commentID =
                given()
                        .spec(reqSpec)
                        .body(newComment)
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("posts/" + postID + "/comments")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
        System.out.println("commentID = " + commentID);

    }

    @Test(dependsOnMethods = "createUserComments")
    public void getCommentByID() {
        given()
                .spec(reqSpec)

                .when()
                .get("comments/" + commentID)

                .then()
                //.log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "getCommentByID")
    public void updateComment() {

        Map<String, String> updateComment = new HashMap<>();
        updateComment.put("name", "Merhaba JAVA");


        given()
                .spec(reqSpec)
                .body(updateComment)

                .when()
                .put("comments/" + commentID)

                .then()
                .statusCode(200)
                //.log().body()
                .contentType(ContentType.JSON)
        ;
    }

    @Test(dependsOnMethods = "updateComment")
    public void deleteComment() {
        given()
                .spec(reqSpec)

                .when()
                .delete("comments/" + commentID)

                .then()
                //.log().all()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteComment")
    public void deleteCommentNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("comments/" + commentID)

                .then()
                //.log().all()
                .statusCode(404)
        ;
    }
}
