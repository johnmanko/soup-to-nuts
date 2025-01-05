package com.johnmanko.example.rest;

import com.johnmanko.example.rest.models.GreetingPlan;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * This class and annotation is only needed for targets that exist in external libraries.
 * Although GreetingPlan is part of this project, it's included here for demonstration purposes.
 */
@RegisterForReflection(targets = {GreetingPlan.class})
public class ReflectionConfiguration {
}
