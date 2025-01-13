package pl.edu.pjatk;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@ComponentScan
public class Config {
    @Bean
    RestClient getRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }
}
