package in.clouthink.daas.security.token.sample.web;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token/auth")
public class SampleAuthRestController {

    @GetMapping(value = "/anonymous")
    public String sayAnonymous() {
        return "Hello Anonymous";
    }

    @GetMapping(value = "/helloworld")
    public String sayHelloWorld() {
        return "Hello World";
    }

    @PostMapping(value = "/echo1")
    public String echoParamStr(@Param("input") String input) {
        return input;
    }

    @PostMapping(value = "/echo2")
    public String echoBodyStr(@RequestBody String input) {
        return input;
    }

}
