package com.johnmanko.example.rest;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This is an alternative method for accessing properties, but
 * we're making use of it in this project.
 *
 * Default scope in Spring is Singleton.  Optionally, you can
 * specify the @Scope of a bean.  For beans such as configurations (like this),
 * it's best to keep it a Singleton.
 */
@Component
@ConfigurationProperties // Optionally, add a prefix config (prefix = "app.greeting")
public class GreetingProperties {

    private String GREETING_SUBJECT;
    private String greeting;

    @PostConstruct
    public void postConstruct() {
        // whatevs
    }

    @PreDestroy
    public void preDestroy() {
        // For "prototype" scoped beans, Spring does not call the destroy method.
    }

    public String getGREETING_SUBJECT() {
        return GREETING_SUBJECT;
    }

    public void setGREETING_SUBJECT(String GREETING_SUBJECT) {
        this.GREETING_SUBJECT = GREETING_SUBJECT;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
