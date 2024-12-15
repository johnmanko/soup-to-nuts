
# Cilium

## Cluster Type Considerations

### Kind

When creating a cluster, you need to do so with the default CNI disabled.  From the root of this repo:

```shell
kind create cluster --config=cluster-config/kind/cluster-no-cni.yaml
```

[Preload](https://docs.cilium.io/en/latest/installation/kind/) the Cilium image into each worker node in the kind cluster:

```shell
docker pull quay.io/cilium/cilium:v1.16.4
kind load docker-image quay.io/cilium/cilium:v1.16.4 
```

### Rancher Desktop

You need to disable the default CNI disabled. Please see [rancher-desktop](../rancher-desktop/rancher-desktop.md).

## Installation

### Gateway API

See the cluster [README](../README.md) for Gateway API CRD installation instructions.

Additionally, the following is required until a Cilium bug is fixed.  See Cilium [docs](https://docs.cilium.io/en/latest/network/servicemesh/gateway-api/gateway-api/).
```shell
kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.2.0/experimental-install.yaml
```

We want to use cilium's ingress/gateway api, so install using the following:

From cli:

```shell
cilium install --version 1.16.2 \
     —-set ipv4.enabled=true \
     --set ipv6.enabled=false \
     --set kubeProxyReplacement=true \
     --set loadBalancer.enabled=true \
     --set gatewayAPI.enabled=true \
     --set hubble.enabled=true \
     --set hubble.ui.enabled=true \
     --set hubble.relay.enabled=true \
     --set l7Proxy=true \
     --set egressGateway.enabled=true \
     --set bpf.masquerade=true
```

or use the include config:

```shell
cilium install --version 1.16.4 -f cluster-config/cilium/config-dev.yaml
```

If you already ran `cilium install` without the ingress flags, you can set them afterward:

```shell
cilium config set kubeProxyReplacement true
cilium config set ingressController.enabled true
cilium config set ingressController.loadbalancerMode dedicated
```

View you configuration:

```shell
cilium config view
```

```
enable-l7-proxy                                   true
external-envoy-proxy                              true
kube-proxy-replacement                            true
```

## Enable Hubble

```
cilium hubble enable
cilium hubble enable --ui
```

## Verify and Test

```shell
cilium status --wait
```

Output:
```
    /¯¯\
 /¯¯\__/¯¯\    Cilium:             OK
 \__/¯¯\__/    Operator:           OK
 /¯¯\__/¯¯\    Envoy DaemonSet:    OK
 \__/¯¯\__/    Hubble Relay:       OK
    \__/       ClusterMesh:        disabled

DaemonSet              cilium             Desired: 1, Ready: 1/1, Available: 1/1
Deployment             hubble-relay       Desired: 1, Ready: 1/1, Available: 1/1
Deployment             hubble-ui          Desired: 1, Ready: 1/1, Available: 1/1
DaemonSet              cilium-envoy       Desired: 1, Ready: 1/1, Available: 1/1
Deployment             cilium-operator    Desired: 1, Ready: 1/1, Available: 1/1
Containers:            cilium             Running: 1
                       hubble-ui          Running: 1
                       cilium-envoy       Running: 1
                       cilium-operator    Running: 1
                       hubble-relay       Running: 1
Cluster Pods:          8/8 managed by Cilium
Helm chart version:
Image versions         hubble-ui          quay.io/cilium/hubble-ui:v0.13.1@sha256:e2e9313eb7caf64b0061d9da0efbdad59c6c461f6ca1752768942bfeda0796c6: 1
                       hubble-ui          quay.io/cilium/hubble-ui-backend:v0.13.1@sha256:0e0eed917653441fded4e7cdb096b7be6a3bddded5a2dd10812a27b1fc6ed95b: 1
                       cilium-envoy       quay.io/cilium/cilium-envoy:v1.29.7-39a2a56bbd5b3a591f69dbca51d3e30ef97e0e51@sha256:bd5ff8c66716080028f414ec1cb4f7dc66f40d2fb5a009fff187f4a9b90b566b: 1
                       cilium-operator    quay.io/cilium/operator-generic:v1.16.0-rc.2@sha256:0392b4ea5b3c53acfc5d202f3b3cc1fafe79ae2e64af0eb47e2c2775709091b0: 1
                       hubble-relay       quay.io/cilium/hubble-relay:v1.16.0-rc.2@sha256:a12082f420adf138338b39e6de37e086b747e0d486cecf9447c44c8a8542909e: 1
                       cilium             quay.io/cilium/cilium:v1.16.0-rc.2@sha256:e7a463fb48043db42560a11a883a48c6170a0ae5847ebfb8a7dee8726dbf94c7: 1

```

```shell
kubectl get pods --all-namespaces -o wide
```

Output:
```
NAMESPACE     NAME                                      READY   STATUS    RESTARTS   AGE   IP             NODE                   NOMINATED NODE   READINESS GATES
kube-system   cilium-bl6t8                              1/1     Running   0          44m   192.168.5.15   lima-rancher-desktop   <none>           <none>
kube-system   cilium-envoy-2586v                        1/1     Running   0          44m   192.168.5.15   lima-rancher-desktop   <none>           <none>
kube-system   cilium-operator-68597f6d67-86zhs          1/1     Running   0          44m   192.168.5.15   lima-rancher-desktop   <none>           <none>
kube-system   coredns-576bfc4dc7-cws5n                  1/1     Running   0          45m   10.0.0.232     lima-rancher-desktop   <none>           <none>
kube-system   hubble-relay-8497849bbf-bbshs             1/1     Running   0          43m   10.0.0.243     lima-rancher-desktop   <none>           <none>
kube-system   hubble-ui-59bb4cb67b-7fs5l                2/2     Running   0          43m   10.0.0.68      lima-rancher-desktop   <none>           <none>
kube-system   local-path-provisioner-86f46b7bf7-65mrf   1/1     Running   0          45m   10.0.0.131     lima-rancher-desktop   <none>           <none>
kube-system   metrics-server-557ff575fb-xkbgx           1/1     Running   0          45m   10.0.0.90      lima-rancher-desktop   <none>           <none>

```

Test installation:

```shell
cilium connectivity test
```

Output should end with something similar:
```
✅ [cilium-test] All 56 tests (234 actions) successful, 44 tests skipped, 0 scenarios skipped.
```