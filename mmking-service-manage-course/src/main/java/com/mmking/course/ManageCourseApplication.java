package com.mmking.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.mmking.framework.domain.course")//扫描实体类
@ComponentScan(basePackages = {"com.mmking.framework","com.mmking.course","com.mmking.api","com.mmking.config"})
public class ManageCourseApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManageCourseApplication.class, args);
    }
}