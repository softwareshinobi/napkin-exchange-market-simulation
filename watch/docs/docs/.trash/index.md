# Kompose - Convert your Docker Compose file to Kubernetes or OpenShift

An official Kubernetes project, located at github.com/kubernetes/kompose
Go from Docker Compose to Kubernetes.

<img src="images-pictures/cover-image.png" alt= “” wiaadth="225" heighat="225">

Kompose is a conversion tool for Docker Compose to container orchestrators such as Kubernetes (or OpenShift).

``` bash 
$ kompose convert -f docker-compose.yaml

$ kubectl apply -f .

$ kubectl get po
NAME                            READY     STATUS              RESTARTS   AGE
frontend-591253677-5t038        1/1       Running             0          10s
redis-master-2410703502-9hshf   1/1       Running             0          10s
redis-replica-4049176185-hr1lr  1/1       Running             0          10s
```

[Installation Guide](developer-guides/installation-guide.md)

## Get started on Kubernetes immediately

So easy your human companion could do it too!

Why do cats (and developers) like Kompose?

Developers love to simplify their development environment with Docker Compose.

With Kompose, you can now push the same file to a production container orchestrator!

[Getting Started](developer-guides/getting-started-guide.md)

## Built for container engineers

Our conversions are not always 1-1 from Docker Compose to Kubernetes, but we will help get you 99% of the way there!

### The awesome features

* Compatibility with multiple versions of Docker Compose
* A conversion matrix that outlines all compatible values and versions
* An in-depth user guide to use advanced features such as LoadBalancer, Service and TLS
* Labels that provide the extra 1% needed to get to 1-1 conversion

[User Guide](developer-guides/user-guide.md)
