# Overview

The sample to show how to use the [spring-security-token](https://github.com/melthaw/spring-security-token)


# Prerequisite

Before start, please make sure the Mongodb & Redis are installed and started on your machine.

Here is the way to quick start the mongodb & redis if you are using docker.

```sh
docker-compose -f docker-compose.test.yml up -d
```

# Get Started


## How to build the project ?

```sh
gradle clean build -x test
```

or 

```sh
./gradlew clean build -x test
```

> remember pass `-x test` if you only want verify the source code compilation.

## How to test the project ?

The spring-security-token backend depends on outside server before it gets started.

* mongodb
* redis
* ...

So please remember start the required db before test.

```sh
docker-compose -f docker-compose.test.yml up
```

Finally start the test.

```sh
gradle clean test
```

or 

```sh
./gradlew clean test
```

### Test Mode

> Refz: https://thepracticaldeveloper.com/2017/07/31/guide-spring-boot-controller-tests/

There are different test modes available for spring boot.

* Server-Side Tests
    * Inside-Server Tests
        * Strategy 1: MockMVC in Standalone Mode
        * Strategy 2: MockMVC with WebApplicationContext
    * Outside-Server Tests
        * Strategy 3: SpringBootTest with a MOCK WebEnvironment value
        * Strategy 4: SpringBootTest with a Real Web Server

We choose `Strategy 4: SpringBootTest with a Real Web Server` to test our sample application.

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public abstract class AbstractTest {

}
```

## How to debug the project in IDE ?

The spring-security-token backend depends on outside service before it gets started.

* mongodb
* redis
* ...

So please remember start the required service before debug.

```sh
docker-compose -f docker-compose.test.yml up
```

Add new Gradle configuration in IDE , and run it in debug mode.

> The configuration should match the following CMDs.

```sh
gradle clean bootRun -PjvmArgs="-Dspring.profiles.active=development"
```

or
 
```sh
./gradlew clean bootRun -PjvmArgs="-Dspring.profiles.active=development"
```

# Appendix - Spring Profile

We designed following profiles to match our DevOps requirements.

name | desc
---|---
development | for development in IDE (e.g. debug) , normally for `gradle bootRun`
test | for unit test on local machine , normally for `gradle test`
production ( **enabled** by default) | for production env , normally for `java -jar`

> Refz
> https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html

**How to use?**

To pass the options `-Dspring.profiles.active=<profile-name>` to JVM , for example:

* for Development

```sh
gradle clean bootRun -PjvmArgs="-Dspring.profiles.active=development"
```


* for Test

```sh
gradle clean test 
```

or

```sh
gradle clean test -PjvmArgs="-Dspring.profiles.active=test" 
```

> The `-Dspring.profiles.active=test` is not mandatory, since we have specified the active profile in Test class
>
> ```java
>   @ActiveProfiles(profiles = "test")
>   public abstract class AbstractTest { }
> ```


