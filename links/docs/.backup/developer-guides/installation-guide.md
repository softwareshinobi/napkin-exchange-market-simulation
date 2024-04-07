# Konsole Installation Guide

We have multiple ways to install Kompose. Our preferred (and most up-to-date) method is downloading the binary from the latest GitHub release.

<img src="/../images-pictures/cover-image.png" alt= “” wiaadth="225" heighat="225">

## Download GitHub Releases

Kompose is released via GitHub, you can see all current releases on the [GitHub release page](https://github.com/kubernetes/kompose/releases).

### Github Release For Linux

```bash

curl -L https://github.com/kubernetes/kompose/releases/download/v1.30.0/kompose-linux-amd64 -o kompose

```

### Github Release For macOS

```bash
curl -L https://github.com/kubernetes/kompose/releases/download/v1.30.0/kompose-darwin-amd64 -o kompose

chmod +x kompose

sudo mv ./kompose /usr/local/bin/kompose
```

### Github Release For Windows

Download from [GitHub](https://github.com/kubernetes/kompose/releases/download/v1.30.0/kompose-windows-amd64.exe) and add the binary to your PATH.

## Go

Installing using `go install` pulls from the main branch with the latest development changes.

```sh
go install github.com/kubernetes/kompose@latest
```

## CentOS

Kompose is in [EPEL](https://fedoraproject.org/wiki/EPEL) CentOS repository.
If you don't have [EPEL](https://fedoraproject.org/wiki/EPEL) repository already installed and enabled you can do it by running `sudo yum install epel-release`

If you have [EPEL](https://fedoraproject.org/wiki/EPEL) enabled in your system, you can install Kompose like any other package.

```bash
sudo yum -y install kompose
```

## Fedora

Kompose is in Fedora 24, 25 and 26 repositories. You can install it just like any other package.

```bash
sudo dnf -y install kompose
```

## macOS

On macOS, you can install the latest release via [Homebrew](https://brew.sh) or [MacPorts](https://www.macports.org/).

```bash
brew install kompose
```

## Windows

Kompose can be installed via [Chocolatey](https://chocolatey.org/packages/kubernetes-kompose)

```console
choco install kubernetes-kompose
```

or using winget

```console
winget install Kubernetes.kompose
```

## Docker

You can build an image from the official repo for [Docker](https://docs.docker.com/engine/reference/commandline/build/) or [Podman](https://docs.podman.io/en/latest/markdown/podman-build.1.html):

```bash
docker build -t kompose https://github.com/kubernetes/kompose.git\#main
```

To run the built image against the current directory, run the following command:

```bash
docker run --rm -it -v $PWD:/opt kompose sh -c "cd /opt && kompose convert"
```