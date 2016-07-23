package io.github.nikodc.services.write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DirectStorageServiceInvoker implements StorageServiceInvoker {

    private static final Logger log = LoggerFactory.getLogger(DirectStorageServiceInvoker.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.storageServiceUrl}")
    private String storageServiceUrl;

    @Override
    public StorageServiceResponse addItem(String db, Item item) {
        String concreteStorageServiceUrl = getConcreteStorageServiceUrl(db);

        StorageServiceResponse response = restTemplate.postForObject(concreteStorageServiceUrl,
                item, StorageServiceResponse.class);
        return response;
    }

    private String getConcreteStorageServiceUrl(String db) {
        try {
            return String.format(storageServiceUrl, db);
        } catch (Exception e) {
            log.error("Exception getting concrete storage service url, storageServiceUrl: {}, db: {}",
                    storageServiceUrl, db);
            throw new RuntimeException(e);
        }
    }

    @Primary
    @LoadBalanced
    @Bean
    @ConditionalOnConsulEnabled
    RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
