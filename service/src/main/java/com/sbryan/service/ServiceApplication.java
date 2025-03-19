package com.sbryan.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
//
//@Component
//@Slf4j
//class DataInitializer implements CommandLineRunner {
//
//    private final ItemElasticRepository posts;
//
//    public DataInitializer(ItemElasticRepository posts) {
//        this.posts = posts;
//    }
// Для хорошей работы с эластик нужно много свободной памяти на компьютере, иначе эластик переключится в другое состояние.
// в другом состоянии он будет некорректно работать.
//    @Override
//    public void run(String[] args) {
//        log.info("start data initialization  ...");
//        this.posts
//                .deleteAll()
//                .thenMany(
//                        Flux
//                                .just("Post one", "Post two")
//                                .flatMap(
//                                        title -> this.posts.save(ItemField.builder().id(title).name("content of " + title).build())
//                                )
//                )
//                .log()
//                .subscribe(
//                        null,
//                        null,
//                        () -> log.info("done initialization...")
//                );
//
//    }
//}
