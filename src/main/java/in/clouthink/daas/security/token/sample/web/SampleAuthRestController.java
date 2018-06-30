package in.clouthink.daas.security.token.sample.web;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token/auth")
public class SampleAuthRestController {

    @GetMapping(value = "/anonymouse")
    public String sayAnonymous() {
        return "Hello Anonymouse";
    }

    @GetMapping(value = "/helloworld")
    public String sayHelloWorld() {
        return "Hello World";
    }

    @PostMapping(value = "/echo1")
    @ResponseBody
    public String echoParamStr(String input) {
        return input;
    }

    @PostMapping(value = "/echo2")
    public String echoBodyStr(@RequestBody String input) {
        return input;
    }

}
