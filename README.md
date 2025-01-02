# Purpose

*What this project is*

This repo is going to be a living guide with valid configurations, manifests, instructions and projects.  

Feel free to contribute with PRs to add/correct information, or provide additional sample deployable projects.  

*What this project is NOT*

This is NOT a substitute for each project's (Istio, Cilium, Rancher Desktop) documentation.  Rather, it's simply TL;DR guide to get running quickly.

I highly encourage you to visit the documentation of the installations you plan to use.  You'll gain a greater understanding of each.

* [Cilium](https://cilium.io/) CNI + Service Mesh.
* [Istio](https://istio.io/) CNI Extension (requires primary CNI installed) + Service Mesh.
* [Rancher Desktop](https://rancherdesktop.io/) - A Kubernetes runtime.
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) - Used for running both Docker workloads (see [infra-services/out-cluser](infra-services/out-cluster/README.md)), but includes a basic Kubernetes runtime.
* [Kind](https://kind.sigs.k8s.io/) - A Kubernetes runtime that installs on Docker or Docker Desktop
* [Knative](https://knative.dev/docs/) - A cluster extension to add serverless function support
* [Minikube](https://minikube.sigs.k8s.io/docs/) - A Kubernetes runtime.  Not covered in this guide yet, but pull requests are welcome.
* [Flannel](https://github.com/flannel-io/flannel) - Simple CNI

* [Quarkus](https://quarkus.io/) - A cloud-native Java framework for building cloud-native services and serverless functions
* [Spring Boot](https://spring.io/projects/spring-boot) - An opinionated Java framework for building microservices
* [Hapi.js](https://hapi.dev/) - A Node.js framework for building node-based microservices

* [Kafka](https://kafka.apache.org/) - An open-source distributed event streaming platform, which serve as the basis for events in our cluster.  Note, Knative requires a bus backend.
* [Valkey](https://valkey.io/) - A fork of Redis, which will be used for in-memory caching

* [Homebrew](https://brew.sh/) - Software package manager
* [jq](https://jqlang.github.io/jq/) - A cli utility for parsing, querying and formatting json
* [GraalVM](https://www.graalvm.org/) - GraalVM compiles your Java code to native binary, ideal for cloud deployments that need high performance and low start-up latency
* [Node.js](https://nodejs.org) - A javascript server

# Cluster Formulas

Here are a few formulas to try when creating your cluster.

* Docker Desktop + Docker Bundled Kubernetes + Istio + (MacOS: [docker-mac-net-connect](https://github.com/chipmk/docker-mac-net-connect))
* Docker (or Docker Desktop) + Kind + Istio (without CNI) + [Cloud Provider KIND](https://kind.sigs.k8s.io/docs/user/loadbalancer/)(if using DD) + (MacOS: [docker-mac-net-connect](https://github.com/chipmk/docker-mac-net-connect))
* Docker (or Docker Desktop) + Cilium + [Cloud Provider KIND](https://kind.sigs.k8s.io/docs/user/loadbalancer/)(if using DD) + (MacOS: [docker-mac-net-connect](https://github.com/chipmk/docker-mac-net-connect))
* Docker (or Docker Desktop) + Flannel + Istio (with or without CNI) + [Cloud Provider KIND](https://kind.sigs.k8s.io/docs/user/loadbalancer/)(if using DD) + (MacOS: [docker-mac-net-connect](https://github.com/chipmk/docker-mac-net-connect))

_Please consider adding your own to this list_

# Organization

### Cluster Config

Details for installing and configuring your cluster before deploying services and functions.

* [cluster-config](cluster-config/README.md) - Information on various configurations for running a local Kubernetes cluster
  * [cilium](cluster-config/cilium/README.md) - Cilium related configuration info
  * [docker-desktop](cluster-config/docker-desktop/README.md) - Docker Desktop related configuration info
  * [istio](cluster-config/istio/README.md) - Istio related configuration info
  * [kind](cluster-config/kind/README.md) - Kind related configuration info
  * [manifests](cluster-config/manifests/README.md) - Global Kubernetes manifests you should apply to your cluster
  * [rancher-desktop](cluster-config/rancher-desktop/README.md) - Rancher Desktop related configuration info
  * [knative](cluster-config/knative/README.md)

### Infrastructure Services

Basic configurations for deploying supporting services required for more of one services.

* [infra-services](infra-services/README.md) - Directory for installing supporting services, either in Docker or within your Kubernetes cluster
  * [in-cluster](infra-services/in-cluster/README.md) - Services that could be installed in your cluster.  These are dev/test level configurations and should not be used for production
  * [out-cluster](infra-services/out-cluster/README.md) - Services that could be installed in your local Docker (Desktop).  The provided `compose.yaml` should bring up a local Docker stack of services you can use from your Kubernetes cluster.

### Projects

Same projects that you can deploy to your cluster.  Topics in Quarkus, Spring Boot, Knative functions and more.

* [projects](projects/README.md) - Various source projects you can build and deploy to your cluster.  Most projects have a `k8s` folder with additional manifests, which are configured to use the services and namespaces provided in `./cluster-config/manifests`.
  * [functions](projects/functions/) - Knative functions
  * [manifests](projects/manifests/) - Common manifests that are application related (as opposed to cluster related).  These could be common ConfigMaps, Secrets, etc.
  * [services](projects/services/) - Microservices to be deployed as fully running containers.

