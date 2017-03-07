package com.example.api.config.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.Assert;

/**
 * Main Application class - Run SpringBoot
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
//Swagger hotfix
@SpringBootApplication(exclude = {HypermediaAutoConfiguration.class})

@ComponentScan(basePackages = {"com.example.api"})
public class ApplicationAPI extends SpringBootServletInitializer{
	//http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ApplicationAPI.class, args);
        Assert.notNull(ctx,"Null Spring context");
    }

}
