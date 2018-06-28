package in.clouthink.daas.security.token.sample.test.controller;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import in.clouthink.daas.security.token.sample.test.common.AbstractTest;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 */
public class AuthnTest extends AbstractTest {

    String token;

    @Test
    public void testCrud() throws Exception {
        testLogin();
    }

    public String testLogin() {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("username", "sampleUser");
        bodyMap.add("password", "samplePwd1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap,
                                                                                                          headers);

        Map result = new RestTemplate().postForObject("http://127.0.0.1:8080/login",
                                                      request,
                                                      Map.class);

        System.out.println(result);

        token = (String) ((Map) result.get("data")).get("token");
        return token;
    }

    public void testHelloWorld() throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        String bearer = new String(Base64.encode(token.getBytes("UTF-8")),
                                   "UTF-8");
        headers.set("Authorization", "Bearer " + bearer);

        HttpEntity request = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
//
//
//            @Override
//            public void handleError(ClientHttpResponse response) throws IOException {
//                HttpStatus statusCode = getHttpStatusCode(response);
//                switch (statusCode.series()) {
//                    case CLIENT_ERROR:
//                        throw new HttpClientErrorException(statusCode, response.getStatusText(),
//                                                           response.getHeaders(), getResponseBody(response), getCharset(response));
//                    case SERVER_ERROR:
//                        throw new HttpServerErrorException(statusCode, response.getStatusText(),
//                                                           response.getHeaders(), getResponseBody(response), getCharset(response));
//                    default:
//                        throw new RestClientException("Unknown status code [" + statusCode + "]");
//                }
//
//
//            }
//
//
//            private HttpStatus getHttpStatusCode(ClientHttpResponse response) throws IOException {
//                HttpStatus statusCode;
//                try {
//                    statusCode = response.getStatusCode();
//                }
//                catch (IllegalArgumentException ex) {
//                    throw new UnknownHttpStatusCodeException(response.getRawStatusCode(),
//                                                             response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
//                }
//                return statusCode;
//            }
//
//            private byte[] getResponseBody(ClientHttpResponse response) {
//                try {
//                    InputStream responseBody = response.getBody();
//                    if (responseBody != null) {
//                        return FileCopyUtils.copyToByteArray(responseBody);
//                    }
//                }
//                catch (IOException ex) {
//                    // ignore
//                }
//                return new byte[0];
//            }
//
//            private Charset getCharset(ClientHttpResponse response) {
//                HttpHeaders headers = response.getHeaders();
//                MediaType contentType = headers.getContentType();
//                return contentType != null ? contentType.getCharSet() : null;
//            }
//        });

        ResponseEntity<String> result = restTemplate.exchange("http://127.0.0.1:8080/token/sample/helloworld",
                                                              HttpMethod.GET,
                                                              request,
                                                              String.class);
        System.out.println(result.getBody());
        if (!"Hello World".equalsIgnoreCase(result.getBody())) {
            throw new RuntimeException(result.getBody());
        }
    }

//    public static void main(String[] args) throws Exception {
//        AuthnTest clientA = new AuthnTest();
//        clientA.testLogin();
//
//        AuthnTest clientB = new AuthnTest();
//        clientB.testLogin();
//
//        AuthnTest clientC = new AuthnTest();
//        clientC.testLogin();
//
//        AuthnTest clientD = new AuthnTest();
//        clientD.testLogin();
//
//        try {
//            clientA.testHelloWorld();
//        }
//        catch (HttpStatusCodeException e) {
//             System.out.println(e.getResponseBodyAsString());
//        }
//        try {
//            clientB.testHelloWorld();
//        }
//        catch (HttpStatusCodeException e) {
//             System.out.println(e.getResponseBodyAsString());
//        }
//        try {
//            clientC.testHelloWorld();
//        }
//        catch (HttpStatusCodeException e) {
//             System.out.println(e.getResponseBodyAsString());
//        }
//        try {
//            clientD.testHelloWorld();
//        }
//        catch (HttpStatusCodeException e) {
//             System.out.println(e.getResponseBodyAsString());
//        }
//    }
}
