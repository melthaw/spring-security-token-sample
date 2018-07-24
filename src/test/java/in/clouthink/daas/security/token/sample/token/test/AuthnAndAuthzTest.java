package in.clouthink.daas.security.token.sample.token.test;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class AuthnAndAuthzTest extends AbstractTest {

    @Before
    public void setup() {
        super.setUp();
    }

    @Test
    public void testLoginAndAccess() throws Exception {

        given().when()
               .get("/token/auth/anonymous")
               .then()
               .statusCode(HttpStatus.OK.value())
               .body(equalTo("Hello Anonymous"));

        String token = given().request()
                              .formParam("username", "testuser")
                              .formParam("password", "samplePwd")
                              .when()
                              .post("/login")
                              .then()
                              .statusCode(HttpStatus.OK.value())
                              .body("data.token", notNullValue())
                              .extract()
                              .path("data.token");

        given().header("Authorization", "Bearer " + new String(Base64.encode(token.getBytes("UTF-8")),
                                                               "UTF-8"))
               .when()
               .get("/token/auth/helloworld")
               .then()
               .statusCode(HttpStatus.OK.value())
               .body(equalTo("Hello World"));

        given().header("Authorization", "Bearer " + new String(Base64.encode(token.getBytes("UTF-8")),
                                                               "UTF-8"))
               .queryParam("input", "hi")
               .contentType(ContentType.JSON)
               .when()
               .post("/token/auth/echo1")
               .then()
               .statusCode(HttpStatus.OK.value())
               .body(equalTo("hi"));

        given().header("Authorization", "Bearer " + new String(Base64.encode(token.getBytes("UTF-8")),
                                                               "UTF-8"))
               .contentType(ContentType.JSON)
               .body("hi")
               .when()
               .post("/token/auth/echo2")
               .then()
               .statusCode(HttpStatus.OK.value())
               .body(equalTo("hi"));
    }

}
