## Default CNI

If you're using the default CNI, there is nothing more you need to do.

## Custom CNI

Configuring Rancher Desktop is done using a YAML configuration file. This step is necessary in order to disable the default CNI and replace it with Cilium.

Next you need to start Rancher Desktop with `containerd` and create a `override.yaml`:

```yaml
env:
  # needed for cilium
  INSTALL_K3S_EXEC: '--flannel-backend=none --disable-network-policy'
provision:
  # needs root to mount
  - mode: system
    script: |
      #!/bin/sh
      set -e

      # needed for cilium
      mount bpffs -t bpf /sys/fs/bpf
      mount --make-shared /sys/fs/bpf

      mkdir -p /run/cilium/cgroupv2
      mount -t cgroup2 none /run/cilium/cgroupv2
      mount --make-shared /run/cilium/cgroupv2/
```

Copy to lima directory:

MacOS
```shell
cp override.yaml ~/Library/Application\ Support/rancher-desktop/lima/_config/override.yaml
```

Linux
```shell
cp override.yaml ~/.local/share/rancher-desktop/lima/_config/override.yaml
```

Disabling flannel may not work from the yaml above.  In that case, use the following commands.  `rdctl` is installed with Rancher Desktop.

```shell
rdctl set --kubernetes.options.flannel=false
```

Traefik can also be disabled, either from the cli or Rancher Desktop UI (on the Kubernetes Preferences panel).

> [!CAUTION]
> If you disable Traefik, you'll need to set up and run an external Load Balancer


```shell
rdctl set --kubernetes.options.traefik=false
```

View settings:
```shell
rdctl list-settings
```

```shell
  "kubernetes": {
    "version": "1.30.2",
    "port": 6443,
    "enabled": true,
    "options": {
      "traefik": false,
      "flannel": false
    },
    "ingress": {
      "localhostOnly": false
    }
  },
```

Now, reset Kunbernetes in Rancher Desktop: "Troubleshoot" -> "Reset Kubernetes".   Restart Rancher Desktop.

## Post Installation

There is a bug ([here](https://github.com/rancher-sandbox/rancher-desktop/issues/2487) and [here](https://github.com/rancher-sandbox/rancher-desktop/issues/2208)) with Rancher Desktop that won't allow download of kubeconfig from the UI.  Instead, you'll need to download it [manually](https://github.com/rancher-sandbox/rancher-desktop/issues/2208#issuecomment-1571537851https://github.com/rancher-sandbox/rancher-desktop/issues/2208#issuecomment-1571537851) through the rancher tools:


```shell
rdctl shell sudo k3s kubectl config view --raw > rancher-desktop.yml
```

You can then merge that file with your `~/.kube/config`, or replace it altogether.  Create a rancher-desktop context.

```yaml
apiVersion: v1
clusters:
- cluster:
    certificate-authority-data: ...
    server: ...
  name: rancher-desktop
contexts:
- context:
    cluster: rancher-desktop
    user: rancher-desktop
  name: rancher-desktop
current-context: rancher-desktop
kind: Config
preferences: {}
users:
- name: rancher-desktop
  user:
    client-certificate-data: ...
    client-key-data: ...
```

Switch contexts:

```shell
kubectl config current-context
kubectl config get-contexts
kubectl config use-context rancher-desktop
```