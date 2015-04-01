package in.clouthink.daas.security.token.sample.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import in.clouthink.daas.security.token.sample.cust.SampleRole;
import in.clouthink.daas.security.token.sample.cust.SampleUser;
import in.clouthink.daas.security.token.sample.cust.SampleUserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import in.clouthink.daas.security.token.spi.PasswordDigesterProvider;
import in.clouthink.daas.security.token.spi.impl.DefaultPasswordDigesterProvider;

@Component
public class ApplicationStartupCallback implements
                                       ApplicationListener<ContextRefreshedEvent> {
    
    private static final Log logger = LogFactory.getLog(ApplicationStartupCallback.class);
    
    private PasswordDigesterProvider provider = new DefaultPasswordDigesterProvider();
    
    @Autowired
    private SampleUserRepository sampleUserRepository;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.debug("On ContextStartedEvent");
        initializeApplication();
    }
    
    private void initializeApplication() {
        // initialize Platform Administrator
        SampleUser sampleUser = sampleUserRepository.findByUsername("sampleUser");
        if (sampleUser == null) {
            sampleUser = new SampleUser();
            sampleUser.setUsername("sampleUser");
            String salt = UUID.randomUUID().toString().replace("-", "");
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
}
