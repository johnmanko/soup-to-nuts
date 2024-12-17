## Installation

Read more about installing and configuring Kind in their [docs](https://kind.sigs.k8s.io/).

Install Kind using [homebrew](https://formulae.brew.sh/formula/kind#default): 

```
brew install kind
```

### Reaching Cluster Services

See [Setting Up An Ingress Controller](https://kind.sigs.k8s.io/docs/user/ingress) for more information, but generally the following will apply.  You have two options to access your cluster internals: **Cloud Provider LoadBalancer** or configuring your Kind cluster to `extraPortMappings`.

### Cloud Provider LoadBalancer

Setup LoadBalancer [Cloud Provider KIND](https://kind.sigs.k8s.io/docs/user/loadbalancer/) for kind:

```
go install sigs.k8s.io/cloud-provider-kind@latest
```

This will install the binary in $GOBIN (typically ~/go/bin); you can make it available elsewhere if appropriate:

```
sudo install ~/go/bin/cloud-provider-kind /usr/local/bin
```

### `extraPortMappings`

We'll be usung Cluster Provider for Kind for accessing your cluster, but there is the option of exposing the control plance node with extra ports.

Directly expose ports using `extraPortMappings` under the `control-plane` `role` (add this to the cluster.yaml` below).  Read the Kind [documentation](https://kind.sigs.k8s.io/docs/user/quick-start/#mapping-ports-to-the-host-machine) for more info.

```yaml
- role: control-plane
  extraPortMappings:
  - containerPort: 30000
    hostPort: 80
    listenAddress: "127.0.0.1"
    protocol: TCP
  - containerPort: 30001
    hostPort: 443
    listenAddress: "127.0.0.1"
    protocol: TCP
  - containerPort: 30002
    hostPort: 15021
    listenAddress: "127.0.0.1"
    protocol: TCP
```

## Create Cluster

```shell
kind create cluster --config=cluster.yaml -n training-cluster
```

Check your kubernetes contexts
```shell
$ kubectl config get-contexts
CURRENT   NAME                    CLUSTER                 AUTHINFO                NAMESPACE
          default                 default                 default
*         kind-training-cluster   kind-training-cluster   kind-training-cluster
          rancher-desktop         rancher-desktop         rancher-desktop
```

If you aren't already using your kind context, switch to it now:
```shell
kubectl cluster-info --context kind-training-cluster
```

```shell
kubectl label node kind-control-plane node.kubernetes.io/exclude-from-external-load-balancers-node/kind-control-plane unlabeled
```

Create a cluster using a [configuration file](https://kind.sigs.k8s.io/docs/user/configuration/):

> [!CAUTION]
> If you plan on using Istio's CNI (which is not a standalone CNI, but rather piggybacks off a primary CNI), you'll also need to disable Kind's default CNI and install another, such Calico, Flannel, or Cilium.  Kind's default CNI is not compatible with Istio.

`cluster-no-cni.yaml` (if installing Cilium or Istio CNI plugin):
```yaml
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
name: cluster
networking:
  disableDefaultCNI: true
nodes:
- role: control-plane
- role: worker
- role: worker
- role: worker
```

```shell
kind create cluster --config=cluster-config/kind/cluster-no-cni.yaml
```
----

Switch `kubectl` context:

```
kubectl config use-context kind-istio-cluster
```

Once the cluster is running, we need to run the `cloud-provider-kind` in a terminal and [keep it running](https://github.com/kubernetes-sigs/cloud-provider-kind?tab=readme-ov-file#install).

```
cloud-provider-kind
```

On MacOS, you'll also need to install and run [docker-mac-net-connect](https://github.com/chipmk/docker-mac-net-connect). See the [general cluster](../README.md) for more info.

You can use a registry installed in Docker (see general cluster info), or  copy docker images into a kind cluster, use the following:

```shell
kind load docker-image <image-name>:<tag> --name <kind-cluster-name>
```
