package examples.reactive.webflux.annotation.controller;

import examples.reactive.webflux.model.Employee;
import examples.reactive.webflux.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/{id}")
    public Mono<Employee> getEmployeeById(@PathVariable("id") String id) {
        return employeeRepository.findEmployeeById(id);
    }

    @GetMapping
    public Flux<Employee> getAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    @PostMapping("/update")
    public Mono<Employee> updateEmployee(@RequestBody Employee employee) {
        return employeeRepository.updateEmployee(employee);
    }

}