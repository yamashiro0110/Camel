package sample.camel.split;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * n秒毎にBodyにセットされたListの要素を1つずつ出力する
 */
@SpringBootApplication
@Component
public class SplitRouteBuilder extends RouteBuilder {

    public static void main(final String[] args) {
        SpringApplication.run(SplitRouteBuilder.class, args);
    }

    private Processor setListProcessor = exchange -> {
        final List<String> list = Arrays.asList("hoge", "fuga", "piyo");
        exchange.getIn().setBody(list);
    };

    private long period() {
        return 1000;
    }

    @Override
    public void configure() throws Exception {
        from("timer:test-split?period=" + period())
                .process(this.setListProcessor)
                .split(body())
                .to("log:test-split")
                .end();
    }

}
