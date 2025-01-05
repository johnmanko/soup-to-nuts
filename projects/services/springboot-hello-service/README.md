# Spring Boot RESTful Hello World service

A simple Hello World REST service for Spring Boot

## Features Covered

This project uses SpringBoot to demonstrate a simple REST service.  Check out all [Spring Guides](https://spring.io/guides) for more information.

This project demonstrates the following features:

* SpringBoot [DevTools](https://docs.spring.io/spring-boot/reference/using/devtools.html) to live reload of testing changes during development.
* SpringBoot [REST](https://spring.io/guides/gs/rest-service) (and [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot)) services.
* [Testing the Web Layer](https://spring.io/guides/gs/testing-web) using [@SpringBootTest](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html#page-title).  Read more at [Spring Framework Testing](https://docs.spring.io/spring-framework/reference/testing.html).
* [Creating API Documentation with Restdocs](https://spring.io/guides/gs/testing-restdocs) and [Spring REST Docs](https://spring.io/projects/spring-restdocs)
* [RestDocs](https://spring.io/guides/gs/testing-restdocs) and [OpenAPI](https://springdoc.org/) support
* Kubernetes manifests via [jkube](https://eclipse.dev/jkube/docs/kubernetes-maven-plugin/)
* [GraalVM Native Images]([GraalVM](https://docs.spring.io/spring-boot/reference/packaging/native-image/introducing-graalvm-native-images.html))
* [Your First GraalVM Native Application](https://docs.spring.io/spring-boot/how-to/native-image/developing-your-first-application.html)

If you want to learn more about SpringBoot, please visit its website: [https://docs.spring.io](https://docs.spring.io/spring-boot/tutorial/first-application/index.html).

See also [Common Application Properties](https://docs.spring.io/spring-boot/appendix/application-properties/index.html).

## TL;DR

```
./mvnw clean package
docker build -t localhost:5000/johnmanko/springboot-hello-service:1.0.0 .
kubectl apply -f ../../manifests/hello-world.cm.yaml 
kubectl apply -f target/classes/META-INF/jkube/kubernetes.yml
kubectl apply -f k8s/hello-world.route.yaml
curl http://localhost/sb-hello/greeting
```

## Build and Run project

### Configure IntelliJ Community Edition:

To add SpringBoot DevTools support in IntelliJ IDE, you need to make a couple configuration changes:

Under **Settings** (or **Preferences**) &#8594; **Build, Execution, Deployment** &#8594; **Compiler**:

* Check `Build project automatically`

Under **Settings** (or **Preferences**) &#8594; **Advanced Settings**:

* Check `Allow auto-make to start even if developed application is currently running`

Build the project ([Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot))
```shell
./mvnw clean complie
```

Optionally, build the project using [GraalVM](https://docs.spring.io/spring-boot/reference/packaging/native-image/introducing-graalvm-native-images.html):

```shell
./mvnw spring-boot:build-image -Pnative
```
Then, you can run the app like any other container:

```shell
docker run --rm -p 8080:8080 springboot-hello-service:1.0.0
```

### Running tests

```shell
./mvnw test
```

### Packaging and running the application

The application can be packaged using:

```shell script
./mvnw clean package
```

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw spring-boot:run
```

## Docker image

Create the Docker image.  

> [!NOTE]
> The `pom.xml` has a hardcoded version number for property `jkube.image`, which is used for generating the Kubernetes manifests.  You can change that to `${project.version}`, but then change the following command to reflex the project version number.

```shell
docker build -t localhost:5000/johnmanko/springboot-hello-service:1.0.0 .
```

#### Native vs JVM

> [!NOTE]
> There is a noticeable size and performance difference between non-native JVM builds and their native counterparts.

```shell
$ docker image ls --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}" | grep spring
localhost:5000/johnmanko/springboot-hello-service    1.0.0          462MB
localhost:5000/johnmanko/springboot-hello-service    1.0.0-native   273MB
```

## Kubernetes

### Generating Kubernetes Manifests

When you build the project, the jkube generated Kubernetes manifests will be created in `target/classes/META-INF/jkube`.

Additionally, `src/main/jkube` contains template manifests that are used by jkube for the bases of its generation.  Configurations in those files take precedence.

You can directly regenerate the manifests by running:

```shell
./mvnw k8s:resource
```

### Applying resources

This project uses the same ConfigMap used by project `quarkus-rest-service`.  If you haven't already, install that map:

```shell
kubectl apply -f ../../manifests/hello-world.cm.yaml 
```

Apply the service and deployment:
```shell
kubectl apply -f target/classes/META-INF/jkube/kubernetes.yml
```

Apply the HTTPRoute:
```shell
kubectl apply -f k8s/hello-world.route.yaml 
```

Test your installation:
```shell
$ curl http://localhost/sb-hello/greeting
Feliz navidad, SpringBoot!%   
```

Or using Kind, use the external IP of the LoadBalancer service after using starting `cloud-provider-kind` and then `docker-mac-net-connect`:
```shell
curl http://<load-balancer-ip>/sb-hello
```
## Activate a Profile

The file `application.properties` support various [profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html).  To activate a profile with SpringBoot, you need to set the property `spring.profiles.active`.

```properties
spring.profiles.active=dev
```

Hardcoding that property in `application.properties` is not very useful.  

You can optionally set the value on the cli:

```shell
export MAVEN_OPTS="-Dspring.profiles.active=dev"
mvn clean install
```

Alternatively (and preferred), you can activate a Maven profile using `pom.xml` profiles (which this project already has configured in the pom):

Option 1: Command Line

```shell
mvn clean install -Pdev
```

This activates the dev profile.

Option 2: Settings File (`~/.m2/settings.xml`) Add a `<activeProfiles>` section to specify the default active profile:

```xml
<settings>
    <activeProfiles>
        <activeProfile>prod</activeProfile>
    </activeProfiles>
</settings>
```

Option 3: Environment Variable Set the MAVEN_OPTS environment variable to include the profile:

```shell
export MAVEN_OPTS="-Pprod"
```

Option 4: Set the `<activeByDefault>` property in your pom for one profile to make it the default when no profile is explicitly activated:

```xml
		<profile>
			<id>dev</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<properties>
				<spring.profiles.active>dev</spring.profiles.active>
			</properties>
		</profile>
```