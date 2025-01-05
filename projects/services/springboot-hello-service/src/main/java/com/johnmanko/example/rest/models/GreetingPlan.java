package com.johnmanko.example.rest.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

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

    @Schema(description = "The template string used for building the greeting")
    @NotNull
    private String greetingTemplate;
    @Schema(description = "The target subject of the greeting")
    @NotNull
    private String greetingSubject;
    @Schema(description = "The built greeting")
    @NotNull
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
