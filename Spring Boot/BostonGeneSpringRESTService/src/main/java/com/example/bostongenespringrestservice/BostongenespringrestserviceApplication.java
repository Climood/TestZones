package com.example.bostongenespringrestservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
/*
Установка Spring Security

Для настройки Spring Security, вам для начала нужно добавить некоторые дополнительные зависимости в вашу сборку.
 */

/*
https://spring.io/guides/gs/accessing-data-rest/
 */
//@EnableJpaRepositories убрал JpaRepository
@SpringBootApplication
    public class BostongenespringrestserviceApplication  {

    //@Autowired
    //UserRepository userRepository;
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(BostongenespringrestserviceApplication.class, args);
        RepositoryRestConfiguration restConfiguration = ctx.getBean(RepositoryRestConfiguration.class);
        restConfiguration.setReturnBodyOnCreate(true);
        /*
        Обратите внимание, как предыдущая POST операция включает заголовок Location. Он содержит URI новосозданного ресурса.
 Spring Data REST также имеет два метода RepositoryRestConfiguration.setReturnBodyOnCreate(…)
 и setReturnBodyOnCreate(…), которыми вы можете настраивать фреймворк для немедленного возврата
  представления только что созданного ресурса.
         */
    }
}

/*w 


@SpringBootApplication is a convenience annotation that adds all of the following:

    @Configuration tags the class as a source of bean definitions for the application context.

    @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.

    Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.

    @ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.

Spring Boot automatically spins up Spring Data JPA to create a concrete implementation of the UserRepository and configure it to talk to a back end in-memory database using JPA.
 Spring Data REST builds on top of Spring MVC. It creates a collection of Spring MVC controllers,
 JSON converters, and other beans needed to provide a RESTful front end.
 These components link up to the Spring Data JPA backend. Using Spring Boot this is all autoconfigured;
 if you want to investigate how that works, you could start by looking at the RepositoryRestMvcConfiguration in Spring Data REST.
 */

/*
Для сборки на основе Maven:



For a Maven-based build:

pom.xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.ldap</groupId>
        <artifactId>spring-ldap-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-ldap</artifactId>
    </dependency>
    <dependency>
        <groupId>com.unboundid</groupId>
        <artifactId>unboundid-ldapsdk</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

These dependencies add Spring Security and UnboundId, an open source LDAP server. With that in place, you can then use pure Java to configure your security policy.


Эти зависимости добавляют Spring Security и ApacheDS, LDAP сервер с открытым исходным кодом. С этого момента вы можете использовать Java для настройки вашей политики безопасности.
 */
