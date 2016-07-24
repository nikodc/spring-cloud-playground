#!/bin/bash
CONSUL_LOCATION=http://localhost:8500

if [ ! -v $1 ]; then CONSUL_LOCATION=$1; fi

echo "-----------------------"
echo "spring-cloud-playground"
echo "-----------------------"
echo "Sending configuration to Consul at $CONSUL_LOCATION"
echo "Executing: curl -X PUT --data-binary @read-service.config $CONSUL_LOCATION/v1/kv/config/read-service/data"
echo "Response:"
curl -X PUT --data-binary @read-service.config $CONSUL_LOCATION/v1/kv/config/read-service/data
echo
echo "Done"

echo "Executing: curl -X PUT --data-binary @write-service.config $CONSUL_LOCATION/v1/kv/config/write-service/data"
echo "Response:"
curl -X PUT --data-binary @write-service.config $CONSUL_LOCATION/v1/kv/config/write-service/data
echo
echo "Done"

echo "Executing: curl -X PUT --data-binary @frontend.config $CONSUL_LOCATION/v1/kv/config/frontend/data"
echo "Response:"
curl -X PUT --data-binary @frontend.config $CONSUL_LOCATION/v1/kv/config/frontend/data
echo
echo "Done"