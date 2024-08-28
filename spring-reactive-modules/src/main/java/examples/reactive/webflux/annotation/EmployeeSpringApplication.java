package examples.reactive.webflux.annotation;

import examples.reactive.webflux.annotation.client.EmployeeWebClient;
import examples.reactive.webflux.repository.EmployeeRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication()
public class EmployeeSpringApplication {

    @Bean
    EmployeeRepository employeeRepository() {
        return new EmployeeRepository();
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeSpringApplication.class, args);

        EmployeeWebClient employeeWebClient = new EmployeeWebClient();
        employeeWebClient.consume();
    }

}
