# quarkus-rest-service

## TL;DR

Apply the Kubernetes manifests in `k8s`.

Build the project
```shell
quarkus build --native -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker
```

Package the project
```shell
docker build -f src/main/docker/Dockerfile.native -t localhost:5000/johnmanko/quarkus-helloworld-service:1.0.0 .
```

Deploy the project (to local Docker.  Please read [Deploying to local Kubernetes](https://quarkus.io/guides/deploying-to-kubernetes#deploying-to-local-kubernetes) for information on clusters such as KinD.)
```shell
kubectl apply -f target/kubernetes/minikube.yml 
```

Curl the endpoint:
```shell
curl http://localhost:8080/api/v1/q-hello/
Hello World%
```

> Quarkus RESTful service

This project uses Quarkus to demonstrate a simple REST service.  

This project demonstrates the following features:

* A simple [REST](https://quarkus.io/guides/rest) service with GET endpoints
* Unit [Testing](https://quarkus.io/guides/getting-started-testing) using `@QuarkusTest`, [JUnit](https://junit.org/junit5/) and [Rest Assured](https://rest-assured.io/) and [JaCoCo](https://quarkus.io/guides/tests-with-coverage).
* [OpenAPI](https://quarkus.io/guides/openapi-swaggerui) annotations on the REST service
* Added [OpenTelemetry](https://quarkus.io/guides/opentelemetry) support
* [Kubernetes](https://quarkus.io/guides/deploying-to-kubernetes) support, ie: generating of Kubernetes manifests for production and dev

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

When running this app using dev services (`quarkus dev`), you can view the [SwaggerUI](http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui) to test the endpoints.

Alternatively, GET the endpoints using curl:

```
❯ curl http://localhost:8080/api/v1/q-hello/
Hello World%
❯ curl http://localhost:8080/api/v1/q-hello/greeting
What's up, Doc%                                                                                                                   ❯ curl http://localhost:8080/api/v1/q-hello/greeting/Moe
What's up, Moe%                                                                                                                   ❯ curl http://localhost:8080/api/v1/q-hello/greeting/Moe/plan
com.johnmanko.example.rest.models.GreetingPlan@78152e14%                                                                          ❯ curl http://localhost:8080/api/v1/q-hello/greeting/Moe/plan
{"greetingTemplate":"What's up, %s","greetingSubject":"Moe","greeting":"What's up, Moe"}%                                         ❯ curl http://localhost:8080/api/v1/q-hello/greeting/Moe/plan | jq .
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100    88  100    88    0     0  18585      0 --:--:-- --:--:-- --:--:-- 22000
{
  "greetingTemplate": "What's up, %s",
  "greetingSubject": "Moe",
  "greeting": "What's up, Moe"
}

```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-rest-service-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
