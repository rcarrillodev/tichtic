package com.rcdev.tichtic;

import com.rcdev.tichtic.urlshortener.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class TichticApplication {


	public static void main(String[] args) {

        SpringApplication.run(TichticApplication.class, args);
	}

    @Bean
    public CommandLineRunner test(UrlService urlService){
        return (args) ->{
            urlService.createUrl("https://rcarrillo.dev");
        };

    }

}
