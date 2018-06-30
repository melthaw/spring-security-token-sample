package in.clouthink.daas.security.token.sample.captcha;

import in.clouthink.daas.security.token.annotation.EnableToken;
import in.clouthink.daas.security.token.configure.TokenConfigurer;
import in.clouthink.daas.security.token.configure.TokenConfigurerAdapter;
import in.clouthink.daas.security.token.configure.UrlAclBuilder;
import in.clouthink.daas.security.token.configure.UrlAclProviderBuilder;
import in.clouthink.daas.security.token.core.*;
import in.clouthink.daas.security.token.core.acl.HttpMethod;
import in.clouthink.daas.security.token.sample.spi.impl.SampleDigestMetadataProvider;
import in.clouthink.daas.security.token.sample.spi.impl.SampleIdentityProvider;
import in.clouthink.daas.security.token.spi.*;
import in.clouthink.daas.security.token.spi.impl.redis.LoginAttemptProviderRedisImpl;
import in.clouthink.daas.security.token.spi.impl.redis.TokenProviderRedisImpl;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@SpringBootApplication
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ComponentScan(value = "in.clouthink.daas.security.token.sample.web")
@EnableMongoRepositories({"in.clouthink.daas.security.token.spi.impl.mongodb",
        "in.clouthink.daas.security.token.sample.spi.impl"})
@EnableScheduling
@EnableToken
public class CaptchaSampleApplication extends SpringBootServletInitializer {

//    @Primary
//    @Bean
//    public TokenProvider myTokenProvider() {
//        return new TokenProviderRedisImpl();
//    }
//
//    @Primary
//    @Bean
//    public IdentityProvider identitySampleProvider() {
//        return new SampleIdentityProvider();
//    }
//
//    @Primary
//    @Bean
//    public LoginAttemptProvider myLoginAttemptProvider() {
//        return new LoginAttemptProviderRedisImpl();
//    }
//
//    @Primary
//    @Bean
//    public DigestMetadataProvider digestMetadataSampleProvider() {
//        return new SampleDigestMetadataProvider();
//    }

    @Bean
    public in.clouthink.daas.security.token.event.authentication.LoggerListener authenticationLoggerListener() {
        return new in.clouthink.daas.security.token.event.authentication.LoggerListener();
    }

    @Bean
    public TokenConfigurer tokenConfigurer() {
        return new TokenConfigurerAdapter() {

            @Override
            public void configure(MessageProvider messageProvider) {
                messageProvider.setLocale(Locale.CHINESE);
            }

            @Override
            public void configure(LoginEndpoint endpoint) {
                endpoint.setLoginProcessesUrl("/login");
                endpoint.setAuditCallback(new AuditCallback() {
                    @Override
                    public void auditLogin(HttpServletRequest request, boolean isAuthenticated) {
                        System.out.println("username:" + request.getParameter("username"));
                        System.out.println("password:" + request.getParameter("password"));
                        System.out.println("version:" + request.getParameter("version"));
                        System.out.println("device:" + request.getParameter("device"));
                        System.out.println("isAuthenticated:" + (isAuthenticated ? "Yes" : "Mo"));
                    }

                    @Override
                    public void auditLogout(HttpServletRequest request) {

                    }
                });
            }

            @Override
            public void configure(LogoutEndpoint endpoint) {
                endpoint.setLogoutProcessesUrl("/logout");
            }

            @Override
            public void configure(TokenAuthenticationFilter filter) {
                filter.setProcessesUrl("/token/**");
            }

            @Override
            public void configure(AuthenticationFilter filter) {
                filter.setIgnoredProcessesUrl("/token/guest/**");
                filter.setProcessesUrl("/token/auth/**");
            }

            @Override
            public void configure(AuthorizationFilter filter) {
                filter.setIgnoredProcessesUrl("/token/guest/**");
                filter.setProcessesUrl("/token/auth/**");
            }

            @Override
            public void configure(TokenOptions tokenOptions) {
                tokenOptions.setTokenTimeout(60 * 60 * 1000);
                tokenOptions.setRefreshTokenInteval(3 * 60 * 1000);
            }

            @Override
            public void configure(UrlAclProviderBuilder builder) {
                builder.add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/auth/helloworld")
                                         .httpMethods(HttpMethod.GET)
                                         .grantRules("ROLE:DUMMY,USERNAME:testuser"))
                       .add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/auth/echo1")
                                         .httpMethods(HttpMethod.POST)
                                         .grantRules("ROLE:DUMMY"))
                       .add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/auth/echo2")
                                         .httpMethods(HttpMethod.POST)
                                         .grantRules("USERNAME:testuser"));
            }

            @Override
            public void configure(FeatureConfigurer featureConfigurer) {
                featureConfigurer.enable(AuthenticationFeature.CAPTCHA_ENABLED);
            }

            @Override
            public void configure(CaptchaOptions captchaOptions) {
                captchaOptions.setLength(6);
                captchaOptions.setCaptchaTimeout(60 * 1000);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{CaptchaSampleApplication.class}, args);
    }

}
