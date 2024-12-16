# Overview

There are several ways to install and use a local kubernetes cluster, but I'll focus on a few. For instance, you may wish to install Cilium, which will require disabling the default CNI.  Rancher Desktop and kind allow such customizations, but Docker Desktop doesn't allow any Kubernetes customization.  The sample application in this repo should work in any setup.

Here are the following cluster options and the state of CNI and Kubernetes customization:

| Cluster Kind | Kubernetes Version | Custom CNI | Multi-node | Load Balancer |
| --- | --- | --- | --- | --- |
| [minikube](https://minikube.sigs.k8s.io/docs/) | [--kubernetes-version](https://minikube.sigs.k8s.io/docs/commands/start) | [--cni](https://minikube.sigs.k8s.io/docs/commands/start) (but [version locked](https://github.com/kubernetes/minikube/releases)) | [yes](https://minikube.sigs.k8s.io/docs/commands/node/) | [minikube tunnel](https://minikube.sigs.k8s.io/docs/commands/tunnel/) |
| [kind](https://kind.sigs.k8s.io/) | [nodes.image (yaml config)](https://kind.sigs.k8s.io/docs/user/configuration/#kubernetes-version) | [networking.disableDefaultCNI](https://kind.sigs.k8s.io/docs/user/configuration/#disable-default-cni) | [yes](https://kind.sigs.k8s.io/docs/user/configuration/#nodes) | [sudo cloud-provider-kind](https://kind.sigs.k8s.io/docs/user/loadbalancer/) |
| [Docker Desktop](https://docs.docker.com/desktop/features/kubernetes/) | No | No | No | Included (can't disable) |
| [k3s](https://k3s.io/) | [INSTALL_K3S_VERSION](https://docs.k3s.io/reference/env-variables?_highlight=install_k3s_version) | [--disable=flannel](https://docs.k3s.io/networking/basic-network-options) | No | [HAProxy or Nginx](https://docs.k3s.io/datastore/cluster-loadbalancer?_highlight=load#setup-load-balancer) |
| [Rancher Desktop](https://rancherdesktop.io/) | via [UI](https://docs.rancherdesktop.io/ui/preferences/kubernetes/#kubernetes-version) | lima's [override.yaml](https://docs.cilium.io/en/stable/gettingstarted/k8s-install-default/) |  | Traefik (can disabled via UI) |


# References

Using the following guides:

* [Install Rancher Desktop](https://rancherdesktop.io/)
* [Install Homebrew](https://brew.sh/)
* [Cilium Installation Using Rancher Desktop](https://docs.cilium.io/en/stable/installation/rancher-desktop/)
* [Kubernetes Ingress Support](https://docs.cilium.io/en/latest/network/servicemesh/ingress/)
* [Command Reference: rdctl](https://docs.rancherdesktop.io/references/rdctl-command-reference/)
* [Istio](https://istio.io/latest/docs/setup/platform-setup/)

# Install Cluster

## Cluster Type

* [kind](kind/kind.md)
* [rancher-desktop](rancher-desktop/rancher-desktop.md)
* [docker-desktop](docker-desktop/docker-desktop.md)

## Add included manifests

Be sure to `kubectl apply` the included manifests.  They can be found in the directory `cluster-config/manifests`

Example:
```shell
kubectl apply -f cluster-config/manifests/ns-saas-app.yaml
```
## CRDs

Gateway API are common to all installation, so once you have your cluster type selected and installed, be sure to install the following CRDs.

Use the provided script:

```shell
sh ./cluster-config/gateway-setup.sh
```

or install manually:
```shell
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.2.0/config/crd/standard/gateway.networking.k8s.io_gatewayclasses.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.2.0/config/crd/standard/gateway.networking.k8s.io_gateways.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.2.0/config/crd/standard/gateway.networking.k8s.io_httproutes.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.2.0/config/crd/standard/gateway.networking.k8s.io_referencegrants.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.2.0/config/crd/standard/gateway.networking.k8s.io_grpcroutes.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.2.0/config/crd/experimental/gateway.networking.k8s.io_tlsroutes.yaml
```

Verify:
```shell
kubectl get crd gatewayclasses.gateway.networking.k8s.io gateways.gateway.networking.k8s.io httproutes.gateway.networking.k8s.io referencegrants.gateway.networking.k8s.io tlsroutes.gateway.networking.k8s.io
NAME                                        CREATED AT
gatewayclasses.gateway.networking.k8s.io    2024-08-22T03:13:09Z
gateways.gateway.networking.k8s.io          2024-08-22T03:13:19Z
httproutes.gateway.networking.k8s.io        2024-08-22T03:13:28Z
referencegrants.gateway.networking.k8s.io   2024-08-22T03:13:37Z
tlsroutes.gateway.networking.k8s.io         2024-08-22T03:13:55Z
```

Optionally, install the experimental CRDs.  Cilium requires these due to a bug.  See Cilium [docs](https://docs.cilium.io/en/latest/network/servicemesh/gateway-api/gateway-api/).
```shell
kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.2.0/experimental-install.yaml
```

## Registry

Some installations require a local [registry](https://hub.docker.com/_/registry), such as knative.  You can [install one on Docker](https://stackoverflow.com/questions/57167104/how-to-use-local-docker-image-in-kubernetes-via-kubectl):

```shell
docker run -d -p 5000:5000 --restart always --name registry registry:2
```

And then build and push your image:
```shell
docker build . -t localhost:5000/my-image
docker push localhost:5000/my-image
```

Your pod manifest will reference the image as such:
```shell
apiVersion: v1
kind: Pod
metadata:
  name: my-pod
  labels:
    app: my-app
spec:
  containers:
    - name: app
      image: localhost:5000/my-image
      imagePullPolicy: IfNotPresent
```

## CNI/Service Mesh

* [cilium](cilium/cilium.md)
* [istio](istio/istio.md)

# Troubleshooting

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
kubectl proxy http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
```

Available at [http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/](http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/).

or apply an HTTPRoute:
```shell
kubectl apply -f cluster-config/http-dashboard.yaml
```

Available at [https://localhost/dashboard](https://localhost/dashboard).

## MacOS

MacOS has a problem with routing local traffic to your cluster, even with Docker Desktop as the host platform. You can read more about it at [stackoverflow](https://stackoverflow.com/questions/77385016/how-to-correctly-expose-a-kubernetes-deployment-running-locally-in-kind-on-macos).  

The TL;DR is to install a helper utility, [docker-mac-net-connect](https://github.com/chipmk/docker-mac-net-connect), that you keep running while accessing the cluster.

Install
```shell
brew install chipmk/tap/docker-mac-net-connect
```

Run as service:
```shell
sudo brew services start chipmk/tap/docker-mac-net-connect
```

Run in terminal:
```shell
sudo docker-mac-net-connect
```

## Linux

The is a [known bug](https://github.com/docker/desktop-linux/issues/209) with running Ubuntu 24.04 or greater and pre Docker Desktop 4.35.0 that won't allow you to access the containers.  This isn't a problem directly related to a k8s cluster, but it affects Docker itself.

A workaround (restart Docker Desktop):
```shell
sudo sysctl -w kernel.apparmor_restrict_unprivileged_userns=0
```

# Vagrant

For a more comprehensive and hands on Kubernetes installation process, check out the [vagrant-k8s-cluster](https://github.com/johnmanko/vagrant-k8s-cluster/tree/master).