package in.clouthink.daas.security.token.sample.test;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class SampleRestApiClient {
    
    static String token;
    
    public static void testLogin() {
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
    }
    
    public static void testHelloWorld() throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        String bearer = new String(Base64.encode(token.getBytes("UTF-8")),
                                   "UTF-8");
        headers.set("Authorization", "Bearer " + bearer);
        
        HttpEntity request = new HttpEntity(headers);
        
        ResponseEntity<String> result = new RestTemplate().exchange("http://127.0.0.1/token/sample/helloworld",
                                                                    HttpMethod.GET,
                                                                    request,
                                                                    String.class);
        if (!"Hello World".equalsIgnoreCase(result.getBody())) {
            throw new RuntimeException();
        }
        // System.out.println(result.getBody());
    }
    
    public static void main(String[] args) throws Exception {
        testLogin();
        long totalTime = 0;
        for (int i = 0; i < 10000; i++) {
            long currentTimeMillis = System.currentTimeMillis();
            testHelloWorld();
            long duration = System.currentTimeMillis() - currentTimeMillis;
            totalTime += duration;
        }
        System.out.println("Total:" + totalTime );
        System.out.println("Average:" + totalTime / 10000);
    }
}
