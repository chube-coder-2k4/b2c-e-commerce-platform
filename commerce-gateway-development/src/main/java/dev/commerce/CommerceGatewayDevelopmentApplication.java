package dev.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommerceGatewayDevelopmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceGatewayDevelopmentApplication.class, args);
    }

}
