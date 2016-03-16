package in.clouthink.daas.security.token.sample.test;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import in.clouthink.daas.security.token.repackage.org.springframework.security.crypto.codec.Base64;

/**
 */
public class DispRestApiClient {
    
    public static void test() {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(bodyMap,
                                                                                                          headers);
        
        Map result = new RestTemplate().postForObject("http://dispdash.dashuf.com/disp/imageReUpload",
                                                      request,
                                                      Map.class);
        
        System.out.println(result);
        
    }
    
    public static void main(String[] args) throws Exception {
        test();
    }
}
