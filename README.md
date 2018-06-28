# Overview

The sample to show how to use the [spring-security-token](https://github.com/melthaw/spring-security-token)


# Prerequisite

Before start, please make sure the Mongodb & Redis are installed and started on your machine.

Here is the way to quick start the mongodb & redis if you are using docker.

```
docker-compose -f docker-compose.env.yml up -d
```

# Get Started


## How to build the project ?

```sh
gradle clean build -x test
```

or 

```sh
gradlew clean build -x test
```

> remember pass `-x test` if you only want verify the source code compilation.

## How to test the project ?

The spring-security-token backend depends on outside server before it gets started.

* db
* db_seed


So please remember start the required db before test.

```
 docker-compose -f docker-compose.test.yml up
```

Finally start the test.


```
gradle clean test
```

or 

```
gradlew clean test
```


## How to debug the project in IDE ?

The spring-security-token backend depends on outside server before it gets started.

* db
* db_seed
* ...

So please remember start the required db before debug.

```
docker-compose -f docker-compose.dev.yml up
```


Add new Gradle configuration in IDE , and run it in debug mode.

> The configuration should match the following CMDs.

```
cd mes/server
gradle clean bootRun -PjvmArgs="-Dspring.profiles.active=development"
```

or
 
```
cd mes/server
../../gradlew clean bootRun -PjvmArgs="-Dspring.profiles.active=development"
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

```
gradle clean bootRun -PjvmArgs="-Dspring.profiles.active=development"
```


* for Test

```
gradle clean test 
```

or

```
gradle clean test -PjvmArgs="-Dspring.profiles.active=test" 
```

> The `-Dspring.profiles.active=test` is not mandatory, since we have specified the active profile in Test class
>
> ```
>   @ActiveProfiles(profiles = "test")
>   public abstract class AbstractTest { }
> ```


* for Production

```sh
java -jar server-1.0.0-SNAPSHOT.jar 
```

or

```sh
java -jar server-1.0.0-SNAPSHOT.jar -Dspring.profiles.active=production 
```





