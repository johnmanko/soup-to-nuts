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

