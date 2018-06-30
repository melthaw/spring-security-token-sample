package in.clouthink.daas.security.token.sample.web;

import in.clouthink.daas.security.token.core.Authentication;
import in.clouthink.daas.security.token.core.CaptchaManager;
import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.federation.FederationService;
import in.clouthink.daas.security.token.sample.spi.impl.SampleUser;
import in.clouthink.daas.security.token.spi.impl.SimpleFederationRequest;
import in.clouthink.daas.security.token.support.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token/guest")
public class SampleGuestRestController {

    @Autowired
    private CaptchaManager captchaManager;

//    @Autowired
//    private FederationService federationService;

    @GetMapping(value = "/captcha")
    public String getCaptchaId() {
        if (SecurityUtils.currentAuthentication() != null) {
            System.out.println("NOAUTH TOKEN:"
                                       + SecurityUtils.currentAuthentication()
                                                      .currentToken()
                                                      .getToken());
        }
        return captchaManager.generate().getId();
    }

//    @GetMapping(value = "/federation")
//    public Token federationToken() {
//        SampleUser user = new SampleUser();
//        user.setId("mock_id_for_federation_request");
//        user.setUsername("mock_user_for_federation_request");
//        SimpleFederationRequest federationRequest = new SimpleFederationRequest(user);
//        Authentication authentication = federationService.login(federationRequest);
//        return authentication.currentToken();
//    }

}
