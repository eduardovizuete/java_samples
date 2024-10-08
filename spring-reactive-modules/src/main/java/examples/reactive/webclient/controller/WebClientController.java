package examples.reactive.webclient.controller;

import examples.reactive.webclient.model.Foo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.HashMap;

@RestController
public class WebClientController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/resource")
    public Map<String, String> getResource() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "value");
        return response;
    }

    @PostMapping("/resource")
    public Mono<String> postStringResource(@RequestBody Mono<String> bodyString) {
        return bodyString.map(body -> "processed-" + body);
    }

    @PostMapping("/resource-override")
    public Mono<String> postStringResourceOverride(@RequestBody Mono<String> bodyString) {
        return bodyString.map(body -> "override-processed-" + body);
    }

    @PostMapping("/resource-foo")
    public Mono<String> postFooResource(@RequestBody Mono<Foo> bodyFoo) {
        return bodyFoo.map(foo -> "processedFoo-" + foo.getName());
    }

    @PostMapping(value = "/resource-multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFormUpload(@RequestPart("key1") String value1, @RequestPart("key2") String value2) {
        return "processed-" + value1 + '-' + value2;
    }

}
