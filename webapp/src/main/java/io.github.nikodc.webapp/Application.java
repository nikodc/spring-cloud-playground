package io.github.nikodc.webapp;

import io.github.nikodc.commons.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${application.serviceUrl}")
    private String serviceUrl;

    @RequestMapping("/")
    @ResponseBody
    public ResponseEntity<WebAppResponse> index() {
        try {

            ServiceResponse serviceResponse = callService();

            WebAppResponse response = new WebAppResponse();
            response.setServiceResponse(serviceResponse);
            response.setWebappIp(NetworkUtils.getIpAddress());

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception getting current host's IP address");
            throw new RuntimeException(e);
        }
    }

    private ServiceResponse callService() {
        ServiceResponse serviceResponse = restTemplate.getForObject(serviceUrl, ServiceResponse.class);
        return serviceResponse;
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

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
