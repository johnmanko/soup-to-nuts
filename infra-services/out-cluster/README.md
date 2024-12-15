# Overview

The following services are install, along with exposed ports:

| Service | Port |
| --- | --- |
| postgres | 5432 |
| adminer | 5433 |
| valkey | 6379 |
| mongo | 27017 |
| mongo-express | 27018 |


# Bring up stack

To reinitialise databases, bring down the stack (`docker-compose down`) and remove the managed volumes.

> [!CAUTION]
> For systems like MacOs, docker compose may not be installed.  It's recommended you install via [brew](https://formulae.brew.sh/formula/docker-compose#default). 

```shell
brew install docker-compose
```

Bring up the stack in the background to keep it running (`-d`):
```shell
docker compose up -d 
```

