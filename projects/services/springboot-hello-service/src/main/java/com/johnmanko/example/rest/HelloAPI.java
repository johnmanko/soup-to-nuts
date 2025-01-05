package com.johnmanko.example.rest;

import com.johnmanko.example.rest.models.GreetingPlan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class HelloAPI {

    @Value("${greeting.template}")
    String greetingTemplate;

    @Value("${GREETING_SUBJECT}")
    String greetingSubject;

    @GetMapping(
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ApiResponse(
            description = "Returns 'Hello World'",
            responseCode = "200",
            content = @Content(
                    schema = @Schema(implementation = String.class)
            )
    )
    @Operation(
            summary = "Gets a hardcoded string",
            description = "Retrieves a hardcoded string of 'Hello World'",
            operationId = "springboot-hello-get"
    )
    public String sayHello() {
        return "Hello World!";
    }

    @GetMapping(
            path = "/greeting",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ApiResponse(
            description = "Returns a greeting defined by service",
            responseCode = "200",
            content = @Content(
                    schema = @Schema(implementation = String.class)
            )
    )
    @Operation(
            summary = "Gets a greeting defined by service",
            description = "Builds and returns a greeting using configuration parameters defined at service",
            operationId = "springboot-hello-get-greeting"
    )
    public String greeting() {
        return GreetingPlan.buildGreeting(this.greetingTemplate, this.greetingSubject);
    }

    @GetMapping(
        path = "/greeting/{subject}",
        produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ApiResponse(
            description = "Returns a greeting defined by service addressed to the supplied subject",
            responseCode = "200",
            content = @Content(
                    schema = @Schema(implementation = String.class)
            )
    )
    @Operation(
            summary = "Gets a greeting defined by service addressed to the supplied subject",
            description = "Builds and returns a greeting using configuration parameters defined at service, but addresses the greeting to the supplied subject",
            operationId = "springboot-hello-get-greeting-subject"
    )
    public String greetingTo(@PathVariable("subject") String subject) {
        return GreetingPlan.buildGreeting(this.greetingTemplate, subject);
    }

    @GetMapping(
        path = "/greeting/{subject}/plan",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponse(
            description = "Returns a GreetingPlan",
            responseCode = "200",
            content = @Content(
                    schema = @Schema(implementation = GreetingPlan.class)
            )
    )
    @Operation(
            summary = "Gets a GreetingPlan",
            description = "Returns a GreetingPlan that describes the components used to build a greeting",
            operationId = "springboot-hello-get-greeting-subject-plan"
    )
    public GreetingPlan greetingPlan(@PathVariable("subject") String subject) {
        return new GreetingPlan(this.greetingTemplate, subject);
    }
}
