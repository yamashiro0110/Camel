package sample.camel.route;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 5秒毎にBodyにセットされたListの要素を1つずつ出力する
 */
@Component
public class SplitRouteBuilder extends RouteBuilder {

    private Processor setListProcessor = exchange -> {
        final List<String> list = Arrays.asList("hoge", "fuga", "piyo");
        exchange.getIn().setBody(list);
    };

    @Override
    public void configure() throws Exception {
        from("timer:test-split?period=5000")
                .process(this.setListProcessor)
                .split(body())
                .to("log:test-split")
                .end();
    }

}
