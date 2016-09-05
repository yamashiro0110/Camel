package sample.camel.route;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.time.LocalDateTime;

/**
 * Created by yamashiro-r on 2016/09/05.
 */
//@Component
public class TimerRouteBuilder extends RouteBuilder {

    private Processor processor = exchange -> {
        final String now = LocalDateTime.now().toString();
        exchange.getIn().setBody(String.format("now: %s", now));
    };

    @Override
    public void configure() throws Exception {
        from("timer:test-timer?period=3000")
                .process(this.processor)
                .to("log:test-timer");
    }
}
