# Istio's Service Mesh

> [!INFO]
> This guide only covers the Service Mesh aspect of Istio, not the Istio CNI plugin.  The CNI plugin is not compatible with Kind's CNI.
>
> Kind's Default CNI:
> Kind (Kubernetes IN Docker) uses a minimal network configuration that is optimized for local development and testing. It does not deploy a fully-featured CNI plugin by default. 
> Istio's CNI plugin requires integration with a standard Kubernetes CNI-compliant network (e.g., Calico, Cilium, Flannel) because it hooks into the container network lifecycle to manage traffic redirection via iptables rules.
>
> Istio's CNI Plugin:
> Istio's CNI replaces the need for the istio-init container in the data plane by directly modifying network configurations in the pod's network namespace. For this to work, a compatible CNI plugin must be installed and operational.

## Installation

Install `istioctl` using [homebrew](https://formulae.brew.sh/formula/istioctl#default)

```
brew install istioctl
```

## Istio CNI

Optionally, install the [Istio CNI](https://istio.io/latest/docs/setup/additional-setup/cni/) node agent.   The Istio CNI plugin operates as an additional CNI plugin. Ensure your cluster has a primary CNI plugin installed (e.g., Calico, Flannel, etc.).


## Install and Configure Istio 

### Istio

[Install Istio](https://istio.io/latest/docs/setup/getting-started/#install) using a "demo" profile.  Read more about the installation [here](https://istio.io/latest/docs/setup/getting-started/#bookinfo).

The included `demo-profile-no-gateways.yaml` was obtained from Istio:
```
wget https://raw.githubusercontent.com/istio/istio/release-1.24/samples/bookinfo/demo-profile-no-gateways.yaml
istioctl install -f demo-profile-no-gateways.yaml
```

`demo-profile-no-gateways-no-cni.yaml`:
```yaml
# IOP configuration used to install the demo profile without gateways.
apiVersion: install.istio.io/v1alpha1
kind: IstioOperator
spec:
  profile: demo
  components:
    cni:
      enabled: false
    ingressGateways:
    - name: istio-ingressgateway
      enabled: false
    egressGateways:
    - name: istio-egressgateway
      enabled: false
```

#### Istio CNI

If you want to enable Istio CNI, add the following `spec` configuration to the above yaml.  Be sure to install a compatible CNI (not Kind's default CNI).

IstioOperator config (`demo-profile-no-gateways.yaml`):
```yaml
spec:
  components:
    cni:
      enabled: true
      excludeNamespaces:
        - istio-system
        - kube-system
```

Add a namespace label to instruct Istio to automatically inject Envoy sidecar proxies when you deploy your application later:

```shell
kubectl label namespace default istio-injection=enabled
```

## Gateway

See [../README.md](../README.md) for Gateway API CRD installation.

### Sample Gateways

Read about Istio's [implementation](https://istio.io/latest/docs/tasks/traffic-management/ingress/gateway-api/) of the Kubernetes [Gateway API](https://gateway-api.sigs.k8s.io/).

For reasons of consistency across the possible combinations of cluster setup, create a namespac name `ingress`.  The Istio instruction give the following, but we'll change it:

Do not create this namespace:
```shell
kubectl create namespace istio-ingress
```

Instead, use the included file, but these instructions are also noted in the [general cluster](../README.md) instructions:
`cluster-config/manifests/ns-ingress.yaml`:
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: ingress
```

```
kubectl apply -f cluster-config/manifest/ns-ingress.yaml
```

For additional namespaces that will be using Istio sidecar injection, you'll need to tag them. You'll need to stop all running pods in those namespaces, too.

```shell
kubectl label namespace saas-app istio-injection=enabled --overwrite
kubectl label namespace saas-control istio-injection=enabled --overwrite
```
