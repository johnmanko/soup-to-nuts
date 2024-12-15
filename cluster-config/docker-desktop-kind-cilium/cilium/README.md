# Required CRDs

Install the Gateway API CRDs (verify latest version)

* https://gateway-api.sigs.k8s.io/
* https://docs.cilium.io/en/stable/network/servicemesh/gateway-api/gateway-api/#gs-gateway-api

```shell
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.1.0/config/crd/standard/gateway.networking.k8s.io_gatewayclasses.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.1.0/config/crd/standard/gateway.networking.k8s.io_gateways.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.1.0/config/crd/standard/gateway.networking.k8s.io_httproutes.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.1.0/config/crd/standard/gateway.networking.k8s.io_referencegrants.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.1.0/config/crd/standard/gateway.networking.k8s.io_grpcroutes.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes-sigs/gateway-api/v1.1.0/config/crd/experimental/gateway.networking.k8s.io_tlsroutes.yaml
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

# Production:

```shell
cilium install --version 1.16.2 -f config-prod.yaml
```

or

```shell
cilium install --version 1.16.2 \
     —-set ipv4.enabled=true \
     --set ipv6.enabled=false \
     --set kubeProxyReplacement=true \
     --set externalTrafficPolicy.local=true \
     --set loadBalancer.enabled=true \
     --set gatewayAPI.enabled=true \
     --set hubble.enabled=true \
     --set hubble.ui.enabled=true \
     --set hubble.relay.enabled=true \
     --set l7Proxy=true \
     --set egressGateway.enabled=true \
     --set bpf.masquerade=true
     ```
 

# Development:

```shell
cilium install --version 1.16.2 -f config-dev.yaml
```

or

```shell
cilium install --version 1.16.2 --set kubeProxyReplacement=true --set gatewayAPI.enabled=true --set l7Proxy=true --set gatewayAPI.hostNetwork.enabled=true
```

or 

```shell
cilium install --version 1.16.2 \
     —-set ipv4.enabled=true \
     --set ipv6.enabled=false \
     --set kubeProxyReplacement=true \
     --set externalTrafficPolicy.local=true \
     --set loadBalancer.enabled=true \
     --set gatewayAPI.enabled=true \
     --set hubble.enabled=true \
     --set hubble.ui.enabled=true \
     --set hubble.relay.enabled=true \
     --set l7Proxy=true \
     --set egressGateway.enabled=true \
     --set bpf.masquerade=true
``` 


