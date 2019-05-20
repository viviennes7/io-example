package com.ms.ioexample.io;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class IOTest {

    private static final String THREE_SECOND_URL = "http://localhost:8080/3second";

    @Before
    public void setup() {
    }

    @Test
    public void blocking() {
        final RestTemplate restTemplate = new RestTemplate();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 3; i++) {
            final ResponseEntity<String> response = restTemplate.exchange(THREE_SECOND_URL, HttpMethod.GET, HttpEntity.EMPTY, String.class);
            assertThat(response.getBody()).isEqualTo("success");
        }

        stopWatch.stop();

        System.out.println(stopWatch.getTotalTimeSeconds());
    }

    @Test
    public void nonBlocking1() throws InterruptedException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        WebClient.builder()
                .build()
                .get()
                .uri(THREE_SECOND_URL)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(System.out::println)
                .subscribe();

        stopWatch.stop();

        System.out.println(stopWatch.getTotalTimeSeconds());

        Thread.sleep(5000);
    }

    @Test
    public void thread() {
        System.out.println(1);

        new Thread(() -> System.out.println(2))
                .start();

        System.out.println(3);
    }

    @Test
    public void nonBlocking2() throws InterruptedException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        WebClient.builder()
                .build()
                .get()
                .uri(THREE_SECOND_URL)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .doOnTerminate(() -> {
                    stopWatch.stop();
                    System.out.println(stopWatch.getTotalTimeSeconds());
                })
                .subscribe();

        Thread.sleep(5000);
    }

    @Test
    public void nonBlocking3() throws InterruptedException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final Mono<String> webclient1 = WebClient.builder()
                .build()
                .get()
                .uri(THREE_SECOND_URL)
                .retrieve()
                .bodyToMono(String.class);

        final Mono<String> webclient2 = WebClient.builder()
                .build()
                .get()
                .uri(THREE_SECOND_URL)
                .retrieve()
                .bodyToMono(String.class);

        final Mono<String> webclient3 = WebClient.builder()
                .build()
                .get()
                .uri(THREE_SECOND_URL)
                .retrieve()
                .bodyToMono(String.class);


        Thread.sleep(20000);
    }
}
