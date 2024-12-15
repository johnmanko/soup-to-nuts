package com.johnmanko.example.rest.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;


@JsonAutoDetect(
        creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@Schema(name = "GreetingPlan",
        description = "A record describing the Greeting build plan")
public class GreetingPlan implements Serializable {

    @Schema(required = true, description = "The template string used for building the greeting")
    private String greetingTemplate;
    @Schema(required = true, description = "The target subject of the greeting")
    private String greetingSubject;
    @Schema(required = true, description = "The built greeting")
    private String greeting;

    public GreetingPlan() {

    }

    public GreetingPlan(String greetingTemplate, String greetingSubject) {
        this.greetingTemplate = greetingTemplate;
        this.greetingSubject = greetingSubject;
        this.greeting = GreetingPlan.buildGreeting(this.greetingTemplate, this.greetingSubject);
    }

    public static String buildGreeting(String greetingTemplate, String greetingSubject) {
        return String.format(greetingTemplate, greetingSubject);
    }

    public String getGreetingTemplate() {
        return greetingTemplate;
    }

    public void setGreetingTemplate(String greetingTemplate) {
        this.greetingTemplate = greetingTemplate;
    }

    public String getGreetingSubject() {
        return greetingSubject;
    }

    public void setGreetingSubject(String greetingSubject) {
        this.greetingSubject = greetingSubject;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
