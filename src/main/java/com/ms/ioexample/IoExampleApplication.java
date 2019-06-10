package com.ms.ioexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@RestController
public class IoExampleApplication {

    private final AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        SpringApplication.run(IoExampleApplication.class, args);
    }

    @GetMapping("/3second")
    public String threeSecond() throws InterruptedException {
        Thread.sleep(3000);
        return "success - " + this.atomicInteger.incrementAndGet();
    }
}
