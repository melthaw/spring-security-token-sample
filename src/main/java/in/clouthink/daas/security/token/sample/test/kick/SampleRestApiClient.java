package in.clouthink.daas.security.token.sample.test.kick;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;

/**
 */
public class SampleRestApiClient {
    
    String token;
    
    public String testLogin() {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("username", "sampleUser");
        bodyMap.add("password", "samplePwd");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap,
                                                                                                          headers);
                                                                                                          
        Map result = new RestTemplate().postForObject("http://127.0.0.1/login",
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
        
        ResponseEntity<String> result = restTemplate.exchange("http://127.0.0.1/token/sample/helloworld",
                                                              HttpMethod.GET,
                                                              request,
                                                              String.class);
        System.out.println(result.getBody());
        if (!"Hello World".equalsIgnoreCase(result.getBody())) {
            throw new RuntimeException(result.getBody());
        }
    }
    
    public static void main(String[] args) throws Exception {
        SampleRestApiClient clientA = new SampleRestApiClient();
        clientA.testLogin();
        
        SampleRestApiClient clientB = new SampleRestApiClient();
        clientB.testLogin();
        
        SampleRestApiClient clientC = new SampleRestApiClient();
        clientC.testLogin();
        
        SampleRestApiClient clientD = new SampleRestApiClient();
        clientD.testLogin();
        
        try {
            clientA.testHelloWorld();
        }
        catch (HttpStatusCodeException e) {
             System.out.println(e.getResponseBodyAsString());
        }
        try {
            clientB.testHelloWorld();
        }
        catch (HttpStatusCodeException e) {
             System.out.println(e.getResponseBodyAsString());
        }
        try {
            clientC.testHelloWorld();
        }
        catch (HttpStatusCodeException e) {
             System.out.println(e.getResponseBodyAsString());
        }
        try {
            clientD.testHelloWorld();
        }
        catch (HttpStatusCodeException e) {
             System.out.println(e.getResponseBodyAsString());
        }
    }
}
