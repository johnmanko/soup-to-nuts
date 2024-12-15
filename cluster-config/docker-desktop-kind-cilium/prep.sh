#!/bin/sh

# Kubernetes Cloud Provider for KIND
# https://github.com/kubernetes-sigs/cloud-provider-kind?tab=readme-ov-file#install

kubectl label node kind-control-plane node.kubernetes.io/exclude-from-external-load-balancers-node/kind-control-plane unlabeled
