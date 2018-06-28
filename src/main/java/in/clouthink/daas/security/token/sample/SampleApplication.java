package in.clouthink.daas.security.token.sample;

import in.clouthink.daas.security.token.annotation.EnableToken;
import in.clouthink.daas.security.token.configure.TokenConfigurer;
import in.clouthink.daas.security.token.configure.TokenConfigurerAdapter;
import in.clouthink.daas.security.token.configure.UrlAclBuilder;
import in.clouthink.daas.security.token.configure.UrlAclProviderBuilder;
import in.clouthink.daas.security.token.core.AuthenticationFeature;
import in.clouthink.daas.security.token.core.FeatureConfigurer;
import in.clouthink.daas.security.token.core.LoginAttemptStrategySupport;
import in.clouthink.daas.security.token.core.TokenLifeSupport;
import in.clouthink.daas.security.token.core.acl.HttpMethod;
import in.clouthink.daas.security.token.sample.cust.DigestMetadataSampleProvider;
import in.clouthink.daas.security.token.sample.cust.IdentitySampleProvider;
import in.clouthink.daas.security.token.spi.AuditCallback;
import in.clouthink.daas.security.token.spi.DigestMetadataProvider;
import in.clouthink.daas.security.token.spi.IdentityProvider;
import in.clouthink.daas.security.token.spi.TokenProvider;
import in.clouthink.daas.security.token.spi.impl.mongodb.TokenProviderMongodbImpl;
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
@EnableMongoRepositories({"in.clouthink.daas.security.token.sample.cust", "in.clouthink.daas.security.token.spi.impl.mongodb"})
@EnableScheduling
@EnableToken
public class SampleApplication extends SpringBootServletInitializer {

    //	@Value("${mongodb.host}")
    //	private String mongodbHost;
    //
    //	@Value("${mongodb.port}")
    //	private int mongodbPort;
    //
    //	@Value("${mongodb.database}")
    //	private String mongodbDatabase;
    //
    //	@Value("${redis.host}")
    //	private String redisHost;
    //
    //	@Value("${redis.port}")
    //	private int redisPort;
    //
    //	@Value("${memcached.host}")
    //	private String memcachedHost;
    //
    //	@Value("${memcached.port}")
    //	private int memcachedPort;

    //	@Primary
    //	@Bean
    //	public TokenProvider tokenProvider1() {
    //		// return new TokenProviderMemcachedImpl();
    //		return new TokenProviderRedisImpl();
    //		// return new TokenProviderMongodbImpl();
    //		// return new TokenProviderMemoryImpl();
    //	}

    @Primary
    @Bean
    public TokenProvider tokenProvider2() {
        // return new TokenProviderMemcachedImpl();
        // return new TokenProviderRedisImpl();
        return new TokenProviderMongodbImpl();
        // return new TokenProviderMemoryImpl();
    }

    //    @Primary
    //	@Bean
    //	public TokenProvider compositeTokenProvider() {
    //		return new CompositeTokenProvider(tokenProvider1(), tokenProvider2());
    //	}

    @Primary
    @Bean
    public IdentityProvider identitySampleProvider() {
        return new IdentitySampleProvider();
    }

    @Primary
    @Bean
    public DigestMetadataProvider digestMetadataSampleProvider() {
        return new DigestMetadataSampleProvider();
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
            public void configure(PreAuthenticationFilter filter) {
                filter.setProcessesUrl("/token/**");
            }

            @Override
            public void configure(AuthenticationFilter filter) {
                // filter.setIgnoredProcessesUrl("/token/sample/anonymouse");
                filter.setProcessesUrl("/token/sample/**");
            }

            @Override
            public void configure(AuthorizationFilter filter) {
                // filter.setIgnoredProcessesUrl("/token/sample/anonymouse");
                filter.setProcessesUrl("/token/sample/**");
            }

            @Override
            public void configure(TokenLifeSupport tokenLifeSupport) {
                tokenLifeSupport.setTokenTimeout(60 * 60 * 1000);
                tokenLifeSupport.setRefreshTokenInteval(3 * 60 * 1000);
                //                tokenLifeSupport.disableMultiTokens();
            }

            @Override
            public void configure(UrlAclProviderBuilder builder) {
                builder.add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/sample/helloworld")
                                         .httpMethods(HttpMethod.GET)
                                         .grantRules("ROLE:DUMMY,USERNAME:testuser"))
                       .add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/sample/echo1")
                                         .httpMethods(HttpMethod.POST)
                                         .grantRules("ROLE:DUMMY"))
                       .add(UrlAclBuilder.antPathBuilder()
                                         .url("/token/sample/echo2")
                                         .httpMethods(HttpMethod.POST)
                                         .grantRules("USERNAME:testuser"));
            }

            @Override
            public void configure(LoginAttemptStrategySupport loginAttemptStrategySupport) {
                loginAttemptStrategySupport.setMaxAttempts((short) 3);
            }

            @Override
            public void configure(FeatureConfigurer featureConfigurer) {
                featureConfigurer.enable(AuthenticationFeature.LOGIN_ATTEMPT_ENABLED);
            }
        };
    }

    //	@Bean
    //	public MongoDbFactory mongoDbFactory() throws Exception {
    //		return new SimpleMongoDbFactory(new MongoClient(mongodbHost, mongodbPort), mongodbDatabase);
    //	}
    //
    //	@Bean
    //	public MongoTemplate mongoTemplate() throws Exception {
    //		return new MongoTemplate(mongoDbFactory());
    //	}
    //
    //	@Bean
    //	public RedisConnectionFactory jedisConnectionFactory() {
    //		RedisConnectionFactory result = new JedisConnectionFactory(new JedisShardInfo(redisHost, redisPort));
    //		return result;
    //	}
    //
    //	@Bean
    //	public RedisTemplate redisTemplate() {
    //		RedisTemplate result = new RedisTemplate();
    //		result.setConnectionFactory(jedisConnectionFactory());
    //		result.setKeySerializer(new StringRedisSerializer(Charset.forName("UTF-8")));
    //		return result;
    //	}

    // @Bean
    // public MemcachedClientFactoryBean memcachedClientFactoryBean() {
    // MemcachedClientFactoryBean result = new MemcachedClientFactoryBean();
    // result.setServers(memcachedHost + ":" + memcachedPort);
    // return result;
    // }


    public static void main(String[] args) {
        SpringApplication.run(new Object[]{SampleApplication.class}, args);
    }

}
