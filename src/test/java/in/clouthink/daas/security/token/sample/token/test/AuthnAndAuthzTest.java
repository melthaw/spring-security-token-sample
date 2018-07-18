package in.clouthink.daas.security.token.sample.token.test;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthnAndAuthzTest extends AbstractTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        super.setUp();
    }

    @Test
    public void testLoginAndAccess() throws Exception {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("username", "sampleUser");
        bodyMap.add("password", "samplePwd");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap,
                                                                                                          headers);

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity("/login",
                                                                       request,
                                                                       Map.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = (String) ((Map) loginResponse.getBody().get("data")).get("token");
        assertThat(token).isNotNull();

        headers = new HttpHeaders();
        String bearer = new String(Base64.encode(token.getBytes("UTF-8")),
                                   "UTF-8");
        headers.set("Authorization", "Bearer " + bearer);
        request = new HttpEntity(headers);

        ResponseEntity<String> getHelloWorldResponse = restTemplate.exchange("/token/auth/helloworld",
                                                                             HttpMethod.GET,
                                                                             request,
                                                                             String.class);

        assertThat(getHelloWorldResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getHelloWorldResponse.getBody().equals("Hello World"));
    }

}
