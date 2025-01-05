package com.johnmanko.example.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy // We don't want to create an instance of this since it's not really used.
public class BeanProvider {

    /**
     * An example of creating a bean to be managed by Spring.
     * This is useful if the bean isn't a @Component, but a POJO.
     * The method name is what's used for the @Qualifier (ie @Qualifier("s3Client"))
     * during @AutoWired injection.  Alternatively, can specifier the name in the @Bean annotation.
     * This is especially useful when using AWS SDK, which are Java objects we want to inject but
     * are not Spring components.
     */
    //@Bean
    //public S3Client s3Client() {
    //    return S3Client.builder()
    //          .region(Region.US_EAST_1)
    //          .credentialsProvider(ProfileCredentialsProvider.create())
    //          .build();
    //}

}
