package examples.reactive.webflux.functional;

import examples.reactive.webflux.functional.config.EmployeeFunctionalConfig;
import examples.reactive.webflux.model.Employee;
import examples.reactive.webflux.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EmployeeSpringFunctionalApplication.class)
class EmployeeSpringFunctionalIntegrationTest {

    @Autowired
    private EmployeeFunctionalConfig config;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    void givenEmployeeId_whenGetEmployeeById_thenCorrectEmployee() {
        WebTestClient client = WebTestClient.bindToRouterFunction(config.getEmployeeByIdRoute())
                .build();

        Employee employee = new Employee("1", "Employee 1");

        BDDMockito.given(employeeRepository.findEmployeeById("1"))
                .willReturn(Mono.just(employee));

        client.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Employee.class)
                .isEqualTo(employee);
    }

    @Test
    void whenGetAllEmployees_thenCorrectEmployees() {
        WebTestClient client = WebTestClient.bindToRouterFunction(config.getAllEmployeesRoute())
                .build();

        List<Employee> employees = Arrays.asList(
                new Employee("1", "Employee 1"),
                new Employee("2", "Employee 2")
        );

        Flux<Employee> employeeFlux = Flux.fromIterable(employees);

        BDDMockito.given(employeeRepository.findAllEmployees()).willReturn(employeeFlux);

        client.get()
                .uri("/employees")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Employee.class)
                .isEqualTo(employees);
    }

    @Test
    void whenUpdateEmployee_thenEmployeeUpdated() {
        WebTestClient client = WebTestClient.bindToRouterFunction(config.updateEmployeeRoute())
                .build();

        Employee employee = new Employee("1", "Employee 1 Updated");

        client.post()
                .uri("/employees/update")
                .body(Mono.just(employee), Employee.class)
                .exchange()
                .expectStatus()
                .isOk();

        Mockito.verify(employeeRepository).updateEmployee(employee);
    }

}