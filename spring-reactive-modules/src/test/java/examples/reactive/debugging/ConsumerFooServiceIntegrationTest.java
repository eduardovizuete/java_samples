package examples.reactive.debugging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import examples.reactive.debugging.consumer.model.Foo;
import examples.reactive.debugging.consumer.service.FooService;
import examples.reactive.debugging.utils.ListAppender;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsumerFooServiceIntegrationTest {

    FooService service = new FooService();

    @BeforeEach
    void clearLogList() {
        Hooks.onOperatorDebug();
        ListAppender.clearEventList();
    }

    @Test
    void givenFooWithNullId_whenProcessFoo_thenLogsWithDebugTrace() {
        Foo one = new Foo(1, "nameverylong", 8);
        Foo two = new Foo(null, "nameverylong", 4);
        Flux<Foo> flux = Flux.just(one, two);

        service.processFoo(flux);

        Collection<String> allLoggedEntries = ListAppender.getEvents()
                .stream()
                .map(ILoggingEvent::getFormattedMessage)
                .collect(Collectors.toList());

        Collection<String> allSuppressedEntries = ListAppender.getEvents()
                .stream()
                .map(ILoggingEvent::getThrowableProxy)
                .flatMap(t -> Optional.ofNullable(t)
                        .map(IThrowableProxy::getSuppressed)
                        .map(Arrays::stream)
                        .orElse(Stream.empty()))
                .map(IThrowableProxy::getClassName)
                .collect(Collectors.toList());

        //assertThat(allLoggedEntries)
        //        .anyMatch(entry -> entry.contains("The following error happened on processFoo method!"))
        //        .anyMatch(entry -> entry.contains("| onSubscribe"))
        //        .anyMatch(entry -> entry.contains("| cancel()"));

        MatcherAssert.assertThat(allLoggedEntries, Matchers.anyOf(
                Matchers.contains("The following error happened on processFoo method!"),
                Matchers.contains("| onSubscribe"),
                Matchers.contains("| cancel()")
        ));

        //assertThat(allSuppressedEntries)
        //        .anyMatch(entry -> entry.contains("reactor.core.publisher.FluxOnAssembly$OnAssemblyException"));
        MatcherAssert.assertThat(allSuppressedEntries, Matchers.anyOf(
                Matchers.contains("reactor.core.publisher.FluxOnAssembly$OnAssemblyException")
        ));
    }

}
