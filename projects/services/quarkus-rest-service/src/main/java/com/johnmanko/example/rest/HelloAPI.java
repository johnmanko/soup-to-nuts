package com.johnmanko.example.rest;

import com.johnmanko.example.rest.models.GreetingPlan;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/hello")
@RequestScoped
@Tag(ref = "quarkus-hello")
public class HelloAPI {

    @ConfigProperty(name = "greeting.template")
    String greetingTemplate;

    @ConfigProperty(name = "GREETING_SUBJECT")
    String greetingSubject;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponseSchema(
            value = String.class,
            responseDescription = "Returns 'Hello World'",
            responseCode = "200")
    @Operation(
            summary = "Gets a hardcoded string",
            description = "Retrieves a hardcoded string of 'Hello World'",
            operationId = "quarkus-hello-get"
    )
    public String sayHello() {
        return "Hello World";
    }

    @Path("/greeting")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponseSchema(
            value = String.class,
            responseDescription = "Returns a greeting defined by service",
            responseCode = "200")
    @Operation(
            summary = "Gets a greeting defined by service",
            description = "Builds and returns a greeting using configuration parameters defined at service",
            operationId = "quarkus-hello-get-greeting"
    )
    public String greeting() {
        return GreetingPlan.buildGreeting(this.greetingTemplate, this.greetingSubject);
    }

    @Path("/greeting/{subject}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponseSchema(
            value = String.class,
            responseDescription = "Returns a greeting defined by service addressed to the supplied subject",
            responseCode = "200")
    @Operation(
            summary = "Gets a greeting defined by service addressed to the supplied subject",
            description = "Builds and returns a greeting using configuration parameters defined at service, but addresses the greeting to the supplied subject",
            operationId = "quarkus-hello-get-greeting-subject"
    )
    public String greetingTo(@PathParam("subject") String subject) {
        return GreetingPlan.buildGreeting(this.greetingTemplate, subject);
    }

    @Path("/greeting/{subject}/plan")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponseSchema(
            value = GreetingPlan.class,
            responseDescription = "Returns a GreetingPlan",
            responseCode = "200")
    @Operation(
            summary = "Gets a GreetingPlan",
            description = "Returns a GreetingPlan that describes the components used to build a greeting",
            operationId = "quarkus-hello-get-greeting-subject-plan"
    )
    public GreetingPlan greetingPlan(@PathParam("subject") String subject) {
        return new GreetingPlan(this.greetingTemplate, subject);
    }

}
