package examples.reactive.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
public class GreetingController {

    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/")
    public Mono<String> greet(Mono<Principal> principal) {
        logger.debug("/greet received input: {}", principal);

        return principal
                .map(Principal::getName)
                .map(name -> String.format("Hello, %s", name))
                .log();
    }

    @GetMapping("/admin")
    public Mono<String> greetAdmin(Mono<Principal> principal) {
        logger.debug("/admin received input: {}", principal);

        return principal
                .map(Principal::getName)
                .map(name -> String.format("Admin access: %s", name))
                .log();
    }

    @GetMapping("/greetingService")
    public Mono<String> greetingService() {
        logger.debug("/greetingService received input");

        return greetingService.greet().log();
    }

}