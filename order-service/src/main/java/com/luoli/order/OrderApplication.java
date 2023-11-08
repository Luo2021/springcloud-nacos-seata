package com.luoli.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author liluo
 * @create 2022/6/22 15:58
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.luoli.stock.api"})
@ComponentScan(basePackages = { "com.luoli.stock.api","com.luoli.order"})
@EnableDiscoveryClient
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class);
    }
}
