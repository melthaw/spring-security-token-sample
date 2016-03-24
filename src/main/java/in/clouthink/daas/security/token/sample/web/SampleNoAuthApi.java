package in.clouthink.daas.security.token.sample.web;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.federation.FederationService;
import in.clouthink.daas.security.token.sample.cust.SampleUser;
import in.clouthink.daas.security.token.spi.impl.SimpleFederationRequest;
import in.clouthink.daas.security.token.support.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/token/")
public class SampleNoAuthApi {
    
    @Autowired
    private FederationService federationService;
    
    @RequestMapping(value = "/noauth", method = RequestMethod.GET)
    @ResponseBody
    public String sayNoAuth() {
        if (SecurityUtils.currentAuthentication() != null) {
            System.out.println("NOAUTH TOKEN:"
                               + SecurityUtils.currentAuthentication()
                                              .currentToken()
                                              .getToken());
        }
        return "Hello NoAuth";
    }
    
    @RequestMapping(value = "/federation", method = RequestMethod.GET)
    @ResponseBody
    public Token federationToken() {
        SampleUser user = new SampleUser();
        user.setId("mock_id_for_federation_request");
        user.setUsername("mock_user_for_federation_request");
        SimpleFederationRequest federationRequest = new SimpleFederationRequest(user);
        Authentication authentication = federationService.login(federationRequest);
        return authentication.currentToken();
    }
    
}
