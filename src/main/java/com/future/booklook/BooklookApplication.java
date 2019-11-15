package com.future.booklook;

import com.future.booklook.configuration.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class BooklookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooklookApplication.class, args);
    }

}
