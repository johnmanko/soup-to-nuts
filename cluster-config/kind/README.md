## Installation

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

```shell
kubectl label node kind-control-plane node.kubernetes.io/exclude-from-external-load-balancers-node/kind-control-plane unlabeled
```


### `extraPortMappings`

Alternatively, directly expose ports using `extraPortMappings` under the `control-plane` `role` (add this to the cluster.yaml` below):

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

Create a cluster using a [configuration file](https://kind.sigs.k8s.io/docs/user/configuration/):

> [!CAUTION]
> If you plan on using Istio's CNI (which is not a standalone CNI, but rather piggybacks off a primary CNI), you'll also need to disable Kind's default CNI and install another, such Calico, Flannel, or Cilium.  Kind's default CNI is not compatible with Istio.

`cluster-no-cni.yaml` (if installing Cilium or Istio for your service mesh):
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

or with a default CNI

```shell
kind create cluster --config=cluster-config/kind/cluster.yaml
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

## Dashboard



Setup Dashboard UI for kind

```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
```

Create a ServiceAccount and ClusterRoleBinding to provide admin access to the newly created cluster.

```
kubectl create serviceaccount -n kubernetes-dashboard admin-user
kubectl create clusterrolebinding -n kubernetes-dashboard admin-user --clusterrole cluster-admin --serviceaccount=kubernetes-dashboard:admin-user
```

To log in to your Dashboard, you need a Bearer Token. Use the following command to store the token in a variable.

```
token=$(kubectl -n kubernetes-dashboard create token admin-user)
echo $token
```

You can access your [Dashboard](https://istio.io/latest/docs/setup/platform-setup/kind/#setup-dashboard-ui-for-kind) using the kubectl command-line tool by running the following command:

```
kubectl proxy [http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/)
```

To copy docker images into a kind cluster, use the following:

```shell
kind load docker-image <image-name>:<tag> --name <kind-cluster-name>
```
