package io.github.nikodc.services.write;

import io.github.nikodc.commons.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@RestController
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    StorageServiceInvoker storageServiceInvoker;

    @RequestMapping(value = "/{db}/items", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<WriteServiceResponse> index(
            @PathVariable("db") String db,
            @RequestBody Item item) {

        StorageServiceResponse storageServiceResponse = callStorageService(db, item);

        WriteServiceResponse response = new WriteServiceResponse();
        response.getItems().addAll(storageServiceResponse.getItems());
        response.setReadServiceIp(getIpAddress());
        response.setStorageServiceIp(storageServiceResponse.getStorageServiceIp());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String getIpAddress() {
        try {
            return NetworkUtils.getIpAddress();
        } catch (Exception e) {
            log.error("Exception getting current host's IP address");
            throw new RuntimeException(e);
        }
    }

    private StorageServiceResponse callStorageService(String db, Item item) {
        StorageServiceResponse response = storageServiceInvoker.addItem(db, item);
        return response;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
