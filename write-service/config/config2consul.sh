#!/bin/bash
echo "Ejecutando: curl -X PUT --data-binary @consul.config http://localhost:8500/v1/kv/config/write-service/data"
echo "Resultado:"
curl -X PUT --data-binary @consul.config http://localhost:8500/v1/kv/config/write-service/data
echo