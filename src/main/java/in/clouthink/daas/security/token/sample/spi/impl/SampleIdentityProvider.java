package in.clouthink.daas.security.token.sample.spi.impl;

import in.clouthink.daas.security.token.exception.UserNotFoundException;
import in.clouthink.daas.security.token.spi.IdentityProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class SampleIdentityProvider implements IdentityProvider<SampleUser> {

    @Autowired
    private SampleUserRepository sampleUserRepository;

    @Override
    public SampleUser findByUsername(String username) {
        return sampleUserRepository.findByUsername(username);
    }

    @Override
    public SampleUser lock(String username) {
        SampleUser sampleUser = sampleUserRepository.findByUsername(username);
        if (sampleUser == null) {
            throw new UserNotFoundException(username);
        }
        sampleUser.setLocked(true);
        return sampleUserRepository.save(sampleUser);
    }

}
