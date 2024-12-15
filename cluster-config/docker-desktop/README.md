## Default Kubernetes

Docker Desktop doesn't allow you to customize the Kubernetes version, CNI or Load Balancer.  Therefor, you can't install Cilium.  You can install Istio, and it just extends the default CNI.

## Customer Kubernetes

In order to use a custom k8s cluster with Docker Desktop, you need to disable the default installation.  This will allow you to install Kind and Cilium.