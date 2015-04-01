package in.clouthink.daas.security.token.sample;

import in.clouthink.daas.security.token.annotation.EnableToken;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(value = "in.clouthink.daas.security.token.sample.web")
public class ApplicationMvcConfigure {
    
}
