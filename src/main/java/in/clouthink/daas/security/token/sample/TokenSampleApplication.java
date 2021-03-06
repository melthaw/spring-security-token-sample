package in.clouthink.daas.security.token.sample;

import in.clouthink.daas.security.token.annotation.EnableToken;
import in.clouthink.daas.security.token.configure.TokenConfigurer;
import in.clouthink.daas.security.token.configure.TokenConfigurerAdapter;
import in.clouthink.daas.security.token.configure.UrlAclBuilder;
import in.clouthink.daas.security.token.configure.UrlAclProviderBuilder;
import in.clouthink.daas.security.token.core.*;
import in.clouthink.daas.security.token.core.acl.HttpMethod;
import in.clouthink.daas.security.token.sample.spi.impl.SampleDigestMetadataProvider;
import in.clouthink.daas.security.token.sample.spi.impl.SampleIdentityProvider;
import in.clouthink.daas.security.token.spi.DigestMetadataProvider;
import in.clouthink.daas.security.token.spi.IdentityProvider;
import in.clouthink.daas.security.token.support.i18n.MessageProvider;
import in.clouthink.daas.security.token.support.web.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisShardInfo;

import java.nio.charset.Charset;
import java.util.Locale;

@SpringBootApplication
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ComponentScan(value = "in.clouthink.daas.security.token.sample.web")
@EnableMongoRepositories(
        {"in.clouthink.daas.security.token.spi.impl.mongodb", "in.clouthink.daas.security.token.sample.spi.impl"}
)
@EnableToken
public class TokenSampleApplication extends SpringBootServletInitializer {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        RedisConnectionFactory result = new JedisConnectionFactory(new JedisShardInfo(redisHost, redisPort));
        return result;
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate result = new RedisTemplate();
        result.setConnectionFactory(jedisConnectionFactory());
        result.setKeySerializer(new StringRedisSerializer(Charset.forName("UTF-8")));
        return result;
    }

//    @Primary
//    @Bean
//    @DependsOn("redisTemplate")
//    public TokenProvider myTokenProvider() {
//        return new TokenProviderRedisImpl();
//    }

    @Primary
    @Bean
    public IdentityProvider identitySampleProvider() {
        return new SampleIdentityProvider();
    }

//    @Primary
//    @Bean
//    @DependsOn("redisTemplate")
//    public LoginAttemptProvider myLoginAttemptProvider() {
//        return new LoginAttemptProviderRedisImpl();
//    }

    @Primary
    @Bean
    public DigestMetadataProvider digestMetadataSampleProvider() {
        return new SampleDigestMetadataProvider();
    }

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
                filter.setIgnoredProcessesUrl("/token/auth/anonymous");
                filter.setProcessesUrl("/token/auth/**");
            }

            @Override
            public void configure(AuthorizationFilter filter) {
                filter.setIgnoredProcessesUrl("/token/guest/**");
                filter.setIgnoredProcessesUrl("/token/auth/anonymous");
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
                                         .url("/token/auth/helloworld*")
                                         .httpMethods(HttpMethod.GET)
                                         .grantRules("ROLE:DUMMY,USERNAME:testuser"))
                       .add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/auth/echo1*")
                                         .httpMethods(HttpMethod.POST)
                                         .grantRules("ROLE:DUMMY"))
                       .add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/auth/echo2*")
                                         .httpMethods(HttpMethod.POST)
                                         .grantRules("USERNAME:testuser"));
            }

            @Override
            public void configure(FeatureConfigurer featureConfigurer) {
                featureConfigurer.enable(AuthenticationFeature.LOGIN_ATTEMPT_ENABLED);
//                featureConfigurer.enable(AuthenticationFeature.CAPTCHA_ENABLED);
            }

            @Override
            public void configure(CaptchaOptions captchaOptions) {
                captchaOptions.setLength(6);
                captchaOptions.setCaptchaTimeout(60 * 1000);
            }

            @Override
            public void configure(LoginAttemptOptions loginAttemptOptions) {
                loginAttemptOptions.setMaxAttempts((short) 3);
            }

        };
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TokenSampleApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{TokenSampleApplication.class}, args);
    }

}
