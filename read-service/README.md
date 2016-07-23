# Service
## Cómo se ejecuta?
```
docker run -dti -p 8081:8080 --name service1 --link consul:consul nikodc/service --server.port=8080 --spring.cloud.consul.host=consul --spring.cloud.consul.discovery.instance-id=service1
```

