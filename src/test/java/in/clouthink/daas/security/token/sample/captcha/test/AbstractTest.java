package in.clouthink.daas.security.token.sample.captcha.test;

import in.clouthink.daas.security.token.sample.captcha.CaptchaSampleApplication;
import in.clouthink.daas.security.token.sample.spi.impl.SampleRole;
import in.clouthink.daas.security.token.sample.spi.impl.SampleUser;
import in.clouthink.daas.security.token.sample.spi.impl.SampleUserRepository;
import in.clouthink.daas.security.token.spi.KeyGeneratorFactory;
import in.clouthink.daas.security.token.spi.PasswordDigesterProvider;
import in.clouthink.daas.security.token.spi.impl.DefaultPasswordDigesterProvider;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaptchaSampleApplication.class)
@ActiveProfiles(profiles = "test")
public abstract class AbstractTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected SampleUserRepository sampleUserRepository;

    private PasswordDigesterProvider provider = new DefaultPasswordDigesterProvider();

    protected MockMvc mvc;

    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // prepare user
        SampleUser sampleUser = sampleUserRepository.findByUsername("sampleUser");
        if (sampleUser == null) {
            sampleUser = new SampleUser();
            sampleUser.setUsername("sampleUser");
            String salt = KeyGeneratorFactory.getInstance().generateHexKey();
            String password = provider.getPasswordDigester("MD5")
                                      .encode("samplePwd", salt);
            sampleUser.setPassword(password);
            sampleUser.setSalt(salt);

            List roles = new ArrayList();
            roles.add(new SampleRole("DUMMY"));
            sampleUser.setRoles(roles);

            sampleUserRepository.save(sampleUser);
        }
    }

    public void tearDown() {
        // clean up user
        SampleUser sampleUser = sampleUserRepository.findByUsername("sampleUser");
        if (sampleUser == null) {
            sampleUserRepository.delete(sampleUser);
        }
    }
}
