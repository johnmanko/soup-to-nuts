## Troubleshooting

### Suggested Software

It's recommened you install the following:

* [jq](https://jqlang.github.io/jq/download/)


### Port Forwarding
If you're unable to access the service, try exposing it directly.

In one terminal, run the following:
```shell
kubectl port-forward -n saas-app pod/<POD-NAME> 8080:<CONTAINER-PORT>
```

For instance:
```shell
kubectl port-forward -n saas-app pod/quarkus-hello-service-85b84565db-rcxbr 8080:8080
```

Then, in another terminal, curl the pod:
```shell
curl http://localhost:8080/api/v1/hello
Hello World
```

You can then work backward the test the service:
```shell
kubectl port-forward -n saas-app svc/quarkus-hello-service 8080:80
```

If that works, you know your `kubernetes.yaml` deployment is working.

### Istio Cluster and Pod Sidecar

Ensure that your pods have the Istio sidecar injected:
```shell
kubectl describe pod quarkus-hello-service-85b84565db-2tzd2 -n saas-app
```

You're output should look like the following:
```shell
Events:
  Type    Reason     Age   From               Message
  ----    ------     ----  ----               -------
  Normal  Scheduled  30m   default-scheduler  Successfully assigned saas-app/quarkus-hello-service-85b84565db-2tzd2 to docker-desktop
  Normal  Pulled     30m   kubelet            Container image "docker.io/istio/proxyv2:1.24.1" already present on machine
  Normal  Created    30m   kubelet            Created container istio-init
  Normal  Started    30m   kubelet            Started container istio-init
  Normal  Pulled     30m   kubelet            Container image "localhost:5000/johnmanko/quarkus-helloworld-service:1.0.0-rc6" already present on machine
  Normal  Created    30m   kubelet            Created container quarkus-hello-service
  Normal  Started    30m   kubelet            Started container quarkus-hello-service
  Normal  Pulled     30m   kubelet            Container image "docker.io/istio/proxyv2:1.24.1" already present on machine
  Normal  Created    30m   kubelet            Created container istio-proxy
  Normal  Started    30m   kubelet            Started container istio-proxy
```

And the containers should be:
```shell
Containers:
  quarkus-hello-service:
    Container ID:   docker://4f4fa3d316ef3de95270b1cf7b62433eccd40370d1f776080212225a5ac8f9d1
    Image:          localhost:5000/johnmanko/quarkus-helloworld-service:1.0.0-rc6
    Image ID:       docker-pullable://localhost:5000/johnmanko/quarkus-helloworld-service@sha256:b8a4c093835333c256eca72df97cd24fa384c4150d23d878cdbf1a496ab6806f
  istio-proxy:
    Container ID:  docker://5f68a6a2627846515384d0698a086e0e51f23c2b2a945c2fb94b2f1b4a28f282
    Image:         docker.io/istio/proxyv2:1.24.1
```

If you don't see the Istio container, then you probably need to [tag you namespace](../cluster-config/istio/README.md).

