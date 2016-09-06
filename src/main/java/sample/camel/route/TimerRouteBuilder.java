package sample.camel.route;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 3秒毎に現在時刻を出力する
 */
@Component
public class TimerRouteBuilder extends RouteBuilder {

    private Processor printNowProcessor = exchange -> {
        final String now = LocalDateTime.now().toString();
        exchange.getIn().setBody(String.format("now: %s", now));
    };

    @Override
    public void configure() throws Exception {
        from("timer:test-timer?period=3000")
                .process(this.printNowProcessor)
                .to("log:test-timer")
                .end();
    }
}
