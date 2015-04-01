package in.clouthink.daas.security.token.sample.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/token/sample")
public class SampleRestApi {
    
    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    @ResponseBody
    public String sayHelloWorld() {
        return "Hello World";
    }
    
    @RequestMapping(value = "/echo1", method = RequestMethod.POST)
    @ResponseBody
    public String echoParamStr(String input) {
        return input;
    }
    
    @RequestMapping(value = "/echo2", method = RequestMethod.POST)
    @ResponseBody
    public String echoBodyStr(@RequestBody String input) {
        return input;
    }
    
}
