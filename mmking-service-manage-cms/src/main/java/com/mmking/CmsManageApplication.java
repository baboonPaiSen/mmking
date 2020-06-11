package com.mmking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EntityScan(value = "com.mmking.framework.domain.cms")
@ComponentScan(basePackages = {"com.mmking.api", "com.mmking.manage_cms", "com.mmking.config", "com.mmking.framework"})
public class CmsManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsManageApplication.class, args);

    }

    @Bean
    public RestTemplate restTemplate () {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

}
