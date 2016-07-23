package io.github.nikodc.services.storage;

import io.github.nikodc.commons.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private AtomicLong itemNumerator = new AtomicLong(0l);

    private List<Item> items = new ArrayList<>();

    @Value("${application.minDelay:0}")
    private long minDelay;

    @Value("${application.maxDelay:1000}")
    private long maxDelay;

    @RequestMapping(value = "/items", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<StorageServiceResponse> postItem(
            @RequestBody Item item) {
        try {

            log.info("Saving item: {}", item);

            item.setId(itemNumerator.incrementAndGet());

            items.add(item);

            StorageServiceResponse response = new StorageServiceResponse();
            response.getItems().add(item);
            response.setStorageServiceIp(NetworkUtils.getIpAddress());

            long delay = minDelay + new Double(Math.random() * (maxDelay - minDelay)).longValue();
            log.info("Saving item after a {} ms delay", delay);
            Thread.sleep(delay);
            log.info("Item saved: {} ", item);

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
