package br.com.moreira.desafiopetize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("br.com.moreira.desafiopetize.domain.repositories")
public class DesafioPetizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesafioPetizeApplication.class, args);
    }

}
