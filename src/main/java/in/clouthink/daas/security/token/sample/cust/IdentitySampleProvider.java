package in.clouthink.daas.security.token.sample.cust;

import in.clouthink.daas.security.token.spi.IdentityProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
public class IdentitySampleProvider implements IdentityProvider<SampleUser> {
    
    @Autowired
    private SampleUserRepository sampleUserRepository;
    
    @Override
    public SampleUser findByUsername(String username) {
        return sampleUserRepository.findByUsername(username);
    }
    
}
