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

`demo-profile-no-gateways.yaml`:
```yaml
# IOP configuration used to install the demo profile without gateways.
apiVersion: install.istio.io/v1alpha1
kind: IstioOperator
spec:
  profile: demo
  components:
    ingressGateways:
    - name: istio-ingressgateway
      enabled: false
    egressGateways:
    - name: istio-egressgateway
      enabled: false
```

#### Instio CNI

If you want to enable Istio CNI, add the following `spec` configuration to the above yaml.  Be sure to install a compatible CNI (not Kind's default CBI).

IstioOperator config:
```yaml
spec:
  values:
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

Create a namespace:

```shell
kubectl create namespace istio-ingress
```

or using the included file:

`ns-istio-ingress.yaml`:
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: istio-ingress
```

```
kubectl apply -f cluster-config/istio/ns-istio-ingress.yaml
```


