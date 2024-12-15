package com.johnmanko.example.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "quarkus-hello", description = "Hello service.")
        },
        info = @Info(
                version = "1.0.0",
                title = "Hello Service API",
                contact = @Contact(
                        name = "John Manko",
                        url = "https://github.com/johnmanko",
                        email = "redacted"),
                license = @License(name = "GNU GENERAL PUBLIC LICENSE Version 3, 29 June 2007",
                        url = "https://www.gnu.org/licenses/gpl-3.0.txt"))
)
@ApplicationPath("/api/v1")
public class ApplicationResource extends Application {

}