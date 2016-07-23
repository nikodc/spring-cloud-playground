# spring-cloud-playground
Proyecto creado para jugar con [Spring Cloud](http://projects.spring.io/spring-cloud/) para la creación de una nube privada. 

Otras herramientas utilizadas:
  * [Consul](http://consul.io): discovery service y config server
  * [Ribbon](https://github.com/Netflix/ribbon): load balancing
  * [Hystrix](https://github.com/Netflix/Hystrix): circuit breaker
  * [Turbine](https://github.com/Netflix/Turbine): Hystrix stream aggregator
  * [Hystrix Dashboard](https://github.com/Netflix/Hystrix/wiki/Dashboard): monitoreo de Hystrix y Turbine streams
  * [Zuul](https://github.com/Netflix/zuul): frontend y routing
  * [Docker](https://www.docker.com): infraestructura, containers donde corren los componentes

## Arquitectura

El proyecto está compuesto por diversos módulos:
  * **commons**: librería compartida con funcionalidades básicas
  * **storage-service**: servicio REST que simula el acceso a una cierta infraestructura de storage (por ej. una base de datos)
  * **read-service**: servicio REST de lectura de "items" almacenados en algún storage
  * **write-service**: servicio REST de escritura de "items" en algún storage
  * **frontend**: proxy Zuul oficiando de edge service y permitiendo el acceso balanceado a los servicios ```read-storage``` y ```write-storage``` desde le exterior
  * **hystrix-turbine-dashboard**: componente que brinda acceso a los servicios Turbine y Hystrix Dashboard

_TODO: incorporar gráfico mostrando la arquitectura_

## Pre-requisitos

_TODO_

## Build

Para constuir cada componente, y asumiendo que se va a trabajar en el directorio "WORKDIR", ejecutar:

```
> cd $WORKDIR
> git clone https://github.com/nikodc/spring-cloud-playground.git
> cd $WORKDIR/spring-cloud-playground
> mvn package
``` 

Posteriormente, construir las imágenes Docker incluídas:

```
> cd $WORKDIR/storage-service
> mvn docker:build

> cd $WORKDIR/read-service
> mvn docker:build

> cd $WORKDIR/write-service
> mvn docker:build

> cd $WORKDIR/frontend
> mvn docker:build

> cd $WORKDIR/hystrix-turbine-dashboard
> mvn docker:build
```

## Ejecución

### Consul

Crear un contenedor "consul" (exponiendo el puerto 8500 para la UI):

```
docker run -dti -p 8500:8500 --name consul consul agent -dev -ui -client 0.0.0.0
```

Entrar a <http://localhost:8500> y verificar que Consul esté funcionando.

### Servicios de Storage (storage-service)

Crear dos contendores de "storage-service": "storage-service-db1" y "storage-service-db2" (exponiendo los puertos 9001 y 9002 para pruebas directas desde el host de Docker):

```
docker run -dti -p 9001:8080 --name storage-service-db1 --link consul:consul nikodc/storage-service --server.port=8080 --spring.cloud.consul.host=consul --spring.application.name=storage-service-db1 --spring.cloud.consul.discovery.instance-id=storage-service-db1 --application.minDelay=0 --application.maxDelay=500
docker run -dti -p 9002:8080 --name storage-service-db2 --link consul:consul nikodc/storage-service --server.port=8080 --spring.cloud.consul.host=consul --spring.application.name=storage-service-db2 --spring.cloud.consul.discovery.instance-id=storage-service-db2 --application.minDelay=500 --application.maxDelay=1500
```

El parámetro ```--name <nombre-del-servicio>``` es importante en este caso porque ambos servicios no configuran un cluster, sino que representan fuentes de datos disjuntas. Sobrescribiendo el nombre de la aplicación, para hacerlos distintos, servirá para que Consul las identifique como aplicaciones independientes.

Los parámetros ```--application.minDelay=<tiempo-en-ms>``` y ```-application.maxDelay=<tiempo-en-ms>``` configuran un delay simulado del storage. Estos valores servirán para evaluar el comportamiento de Hystrix ante demoras, cortando el circuito hacia los mismos y eventualmente ejecutando lógica de fallback. Modificar a gusto. En el ejemplo, el primer contenedor tendrá delays de entre 0 y 500 ms, mientras que e segundo contenedor tendrá delays entre 500 y 1000 ms.

### Servicios de Lectura y Escritura (read-service y write-service)

Crear dos contenedores para "read-service" (exponiendo puertos 9101 y 9102) y dos para "write-service" (exponiendo puertos 9201 y 9202):

```
docker run -dti -p 9101:8080 --name read-service-1 --link consul:consul nikodc/read-service --server.port=8080 --spring.cloud.consul.host=consul --spring.cloud.consul.discovery.instance-id=read-service-1
docker run -dti -p 9102:8080 --name read-service-2 --link consul:consul nikodc/read-service --server.port=8080 --spring.cloud.consul.host=consul --spring.cloud.consul.discovery.instance-id=read-service-2

docker run -dti -p 9201:8080 --name write-service-1 --link consul:consul nikodc/write-service --server.port=8080 --spring.cloud.consul.host=consul --spring.cloud.consul.discovery.instance-id=write-service-1
docker run -dti -p 9202:8080 --name write-service-2 --link consul:consul nikodc/write-service --server.port=8080 --spring.cloud.consul.host=consul --spring.cloud.consul.discovery.instance-id=write-service-2
```

### Frontend

Crear un contenedor "frontend" (exponiendo el puerto 8080):

```
docker run -dti -p 8080:8080 --name frontend --link consul:consul nikodc/frontend --server.port=8080 --spring.cloud.consul.host=consul --spring.cloud.consul.discovery.instance-id=frontend
```

### Dashboard de Hystrix/Turbine (hystrix-turbine-dashboard):

Crear un contenedor "hystrix-turbine-dashboard" (exponiendo el puerto 8090 para acceder a la webapp):

```
docker run -dti -p 8090:8080 --name htd --link consul:consul nikodc/htd --server.port=8080 --spring.cloud.consul.host=consul --spring.application.name=htd --spring.cloud.consul.discovery.instance-id=htd
```

## Cómo usar?

_TODO_