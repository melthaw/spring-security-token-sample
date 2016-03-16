package in.clouthink.daas.security.token.sample.web;

import in.clouthink.daas.security.token.support.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/token/")
public class SampleNoAuthApi {
    
    @RequestMapping(value = "/noauth", method = RequestMethod.GET)
    @ResponseBody
    public String sayNoAuth() {
        if (SecurityUtils.currentAuthentication() != null) {
            System.out.println("NOAUTH TOKEN:"
                             + SecurityUtils.currentAuthentication()
                                            .currentToken().getToken());
        }
        return "Hello NoAuth";
    }
    
}
