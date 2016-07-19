# WebApp
## Cómo se ejecuta?
```
docker run -dti -p 8080:8080 --name webapp --link consul:consul nikodc/webapp --server.port=8080 --spring.cloud.consul.host=consul --spring.cloud.consul.discovery.instance-id=webapp
```