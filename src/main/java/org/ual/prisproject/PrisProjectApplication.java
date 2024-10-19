package org.ual.prisproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = {"Controller", "repository"})  // Ajusta según tu estructura
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "entidades")  // Asegúrate de que Spring escanee tus entidades
public class PrisProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrisProjectApplication.class, args);
    }
}
