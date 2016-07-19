package io.github.nikodc.service;

import io.github.nikodc.commons.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    AtomicLong atomicLong = new AtomicLong();

    @RequestMapping("/")
    @ResponseBody
    public ResponseEntity<ServiceResponse> index() {
        try {

            ServiceResponse response = new ServiceResponse();
            response.setRequestId(atomicLong.getAndIncrement());
            response.setServiceIp(NetworkUtils.getIpAddress());

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception getting current host's IP address");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
