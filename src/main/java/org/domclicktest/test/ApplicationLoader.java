package org.domclicktest.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ApplicationLoader {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationLoader.class, args);
    }

    @Bean
    public CommandLineRunner demo(AccountRepository repository) {
        return (args) -> {
            repository.save(createAccount("42578101111111111111", BigDecimal.TEN));
            repository.save(createAccount("42578102222222222222", BigDecimal.ZERO));
        };
    }

    private Account createAccount(String number, BigDecimal amount) {
        Account account = new Account();
        account.setAmount(amount);
        account.setNumber(number);
        return account;
    }
}
