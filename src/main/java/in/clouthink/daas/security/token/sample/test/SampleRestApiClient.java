package in.clouthink.daas.security.token.sample.test;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import in.clouthink.daas.security.token.spi.KeyGeneratorFactory;
import in.clouthink.daas.security.token.spi.PasswordDigesterProviderFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 */
public class SampleRestApiClient {
    
    static String token;
    
    public static String testLogin() {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("username", "sampleUser");
        bodyMap.add("password", "samplePwd");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap,
                                                                                                          headers);
        
        Map result = new RestTemplate().postForObject("http://127.0.0.1:8080/login",
                                                      request,
                                                      Map.class);
        
        System.out.println(result);
        
        return (String) ((Map) result.get("data")).get("token");
    }

    public static void testAnonymouse() throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            String bearer = new String(Base64.encode(token.getBytes("UTF-8")),
                                       "UTF-8");
            headers.set("Authorization", "Bearer " + bearer);
        }

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> result = new RestTemplate().exchange("http://127.0.0.1:8080/token/sample/anonymouse",
                                                                    HttpMethod.GET,
                                                                    request,
                                                                    String.class);
        if (!"Hello Anonymouse".equalsIgnoreCase(result.getBody())) {
            throw new RuntimeException(result.getBody());
        }
    }

    public static void testNoAuth() throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            String bearer = new String(Base64.encode(token.getBytes("UTF-8")),
                                       "UTF-8");
            headers.set("Authorization", "Bearer " + bearer);
        }

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> result = new RestTemplate().exchange("http://127.0.0.1:8080/token/noauth",
                                                                    HttpMethod.GET,
                                                                    request,
                                                                    String.class);
        if (!"Hello NoAuth".equalsIgnoreCase(result.getBody())) {
            throw new RuntimeException(result.getBody());
        }
    }


    public static void testHelloWorld() throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            String bearer = new String(Base64.encode(token.getBytes("UTF-8")),
                                       "UTF-8");
            headers.set("Authorization", "Bearer " + bearer);
        }
        
        HttpEntity request = new HttpEntity(headers);
        
        ResponseEntity<String> result = new RestTemplate().exchange("http://127.0.0.1:8080/token/sample/helloworld",
                                                                    HttpMethod.GET,
                                                                    request,
                                                                    String.class);
        if (!"Hello World".equalsIgnoreCase(result.getBody())) {
            throw new RuntimeException(result.getBody());
        }
    }
    
    public static void main(String[] args) throws Exception {
//        testAnonymouse();
        testNoAuth();
        token = testLogin();
        testNoAuth();
//        testHelloWorld();
//        testLogin();
//        testHelloWorld();
//        long totalTime = 0;
//        for (int i = 0; i < 1000; i++) {
//            long currentTimeMillis = System.currentTimeMillis();
//            testHelloWorld();
//            long duration = System.currentTimeMillis() - currentTimeMillis;
//            totalTime += duration;
//        }
//        System.out.println("Total:" + totalTime);
//        System.out.println("Average:" + totalTime / 10000);
        
        // System.out.println(PasswordDigesterProviderFactory.getInstance()
        // .getPasswordDigester("MD5"));
        //
        // System.out.println(KeyGeneratorFactory.getInstance().generateId());
        // System.out.println(KeyGeneratorFactory.getInstance().generateHexKey());
        // System.out.println(KeyGeneratorFactory.getInstance().generateBase32Key());
        // System.out.println(KeyGeneratorFactory.getInstance().generateBase64Key());
    }
}
