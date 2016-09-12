package sample.camel.timer;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * n秒毎に現在時刻を出力する
 */
@SpringBootApplication
@Component
public class TimerRouteBuilder extends RouteBuilder {

    public static void main(final String[] args) {
        SpringApplication.run(TimerRouteBuilder.class, args);
    }

    private Processor printNowProcessor = exchange -> {
        final String now = LocalDateTime.now().toString();
        exchange.getIn().setBody(String.format("now: %s", now));
    };

    private long period() {
        return 1000;
    }

    @Override
    public void configure() throws Exception {
        from("timer:test-timer?period=" + period())
                .process(this.printNowProcessor)
                .to("log:test-timer")
                .end();
    }
}
