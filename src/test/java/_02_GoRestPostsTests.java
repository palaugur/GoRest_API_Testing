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

public class _02_GoRestPostsTests {
    int postID;
    String userID = "1301504";
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
    public void getPosts() {

        given()
                .spec(reqSpec)

                .when()
                .get("posts")

                .then()
                //.log().body()
                .statusCode(200)
        ;
    }

    @Test(priority = 2)
    public void createUserPost() {

        String randomTitle = randomGenerator.lorem().sentence();
        String randomBody = randomGenerator.lorem().paragraph();

        Map<String, String> newPost = new HashMap<>();
        newPost.put("title", randomTitle);
        newPost.put("body", randomBody);

        postID =
                given()
                        .spec(reqSpec)
                        .body(newPost)
                        //.log().uri()
                        //.log().body()

                        .when()
                        .post("users/" + userID + "/posts")

                        .then()
                        //.log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
        //System.out.println("postID = " + postID);

    }

    @Test(dependsOnMethods = "createUserPost")
    public void getPostByID() {
        given()
                .spec(reqSpec)

                .when()
                .get("posts/" + postID)

                .then()
                //.log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "getPostByID")
    public void updatePost() {

        Map<String, String> updatePost = new HashMap<>();
        updatePost.put("title", "Merhaba Java35");

        given()
                .spec(reqSpec)
                .body(updatePost)

                .when()
                .put("posts/" + postID)

                .then()
                .statusCode(200)
                //.log().body()
                .contentType(ContentType.JSON)
        ;
    }

    @Test(dependsOnMethods = "updatePost")
    public void deletePost() {
        given()
                .spec(reqSpec)

                .when()
                .delete("posts/" + postID)

                .then()
                //.log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deletePost")
    public void deletePostNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("posts/" + postID)

                .then()
                //.log().body()
                .statusCode(404)
        ;
    }
}
