package in.clouthink.daas.security.token.sample.token.test;

import in.clouthink.daas.security.token.sample.spi.impl.SampleRole;
import in.clouthink.daas.security.token.sample.spi.impl.SampleUser;
import in.clouthink.daas.security.token.sample.spi.impl.SampleUserRepository;
import in.clouthink.daas.security.token.spi.KeyGeneratorFactory;
import in.clouthink.daas.security.token.spi.PasswordDigesterProvider;
import in.clouthink.daas.security.token.spi.impl.DefaultPasswordDigesterProvider;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public abstract class AbstractTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    protected SampleUserRepository sampleUserRepository;

    private PasswordDigesterProvider provider = new DefaultPasswordDigesterProvider();

    @Before
    public void setUp() {
//        RestAssuredMockMvc.standaloneSetup();
        RestAssured.port = this.port;

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

    @After
    public void tearDown() {
        // clean up user
        SampleUser sampleUser = sampleUserRepository.findByUsername("sampleUser");
        if (sampleUser == null) {
            sampleUserRepository.delete(sampleUser);
        }
    }

}
