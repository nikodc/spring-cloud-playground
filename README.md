# spring-cloud-playground
Proyecto creado para jugar con [Spring Cloud](http://projects.spring.io/spring-cloud/) para la creación de una nube privada. 

Otras herramientas utilizadas:
  * [Consul](http://consul.io): discovery service y config server
  * [Ribbon](https://github.com/Netflix/ribbon): load balancing
  * [Zuul](https://github.com/Netflix/zuul): frontend y routing
  * [Docker](https://www.docker.com): infraestructur, containers donde corren los componentes

## Arquitectura
El proyecto está compuesto por diversos módulos:
  * **commons**: librería compartida con funcionalidades básicas
  * **service**: servicio REST interno (no accesible directamente desde fuera de la nube) en el marco de una arquitectura basada en microservices
  * **webapp**: servicio REST que oficia de frontend hacia fuera de la nube

### service

Este componente expone una API REST simple que consta tan solo con una operación:

GET http://<host>:<port>

Como respuesta se obtiene un JSON con esta estructura:
```
{
  "requestId": <id del request>,
  "serviceIp": <IP correspondiente al host donde ejecuta el servicio>
}
```

### webapp

Este componente también expone una API REST compuesta por solamente una operación:

GET http://<host>:<port>

La anterior operación selecciona una instancia del servicio "service" y la invoca. Como resultado, genera un JSON con esta estructura:
```
{
  "serviceResponse": {
    "requestId": <id del request>,
    "serviceIp": <IP correspondiente al host donde ejecuta el servicio "service" invocado>
  },
  "webappIp": <IP correspondiente al host donde ejecuta el servicio>
}
```

La respuesta incluye la IP del host donde corre el servicio "webapp" junto con la respuesta obtenida como resultado de la invocación al servicio "service". De esa forma, será posible apreciar el balanceo que "webapp" hace con respecto a las invocaciones realizadas a "service".

## Pre-requisitos

TODO

## Build

Para constuir cada componente, y asumiendo que se va a trabajar en el directorio "WORKDIR", ejecutar:

```
> cd $WORKDIR
> git clone https://github.com/nikodc/spring-cloud-playground.git
> cd $WORKDIR/spring-cloud-playground
> mvn package
``` 

Posteriormente, construir las imágenes Docker correspondientes a "service" y "webapp" (de nombre "nikodc/service" y "nikodc/webap" respectivamente):

```
> cd $WORKDIR/service
> mvn docker:build
> cd $WORKDIR/webapp
> mvn docker:build
```

## Ejecución

TODO
