package in.clouthink.daas.security.token.sample.token.test;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AuthnAndAuthzTest extends AbstractTest {

    @Before
    public void setup() {
        super.setUp();
    }

    @Test
    public void testLoginAndAccess() throws Exception {
        String token = given()
                .param("username", "sampleUser")
                .param("password", "samplePwd")
                .then()
                .statusCode(HttpStatus.OK.value())
                .when()
                .post("/login")
                .jsonPath()
                .get("data.token");

        assertThat(token).isNotBlank();

        given()
                .header("Authorization", "Bearer " + new String(Base64.encode(token.getBytes("UTF-8")),
                                                                "UTF-8"))
                .when()
                .get("/token/auth/helloworld")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body(equalTo("Hello World"));

    }

}
