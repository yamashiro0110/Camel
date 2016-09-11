package sample.camel.route;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by yamashiro-r on 2016/09/12.
 */
@Component
public class ExceptionHandleRouteBuilder extends RouteBuilder {

    private Processor setListProcessor = exchange -> {
        exchange.getIn().setBody(Arrays.asList("hoge", "fuga", "piyo"));
    };

    private Processor throwExceptionProcessor = exchange -> {
        exchange.getIn().setBody("例外を発生させます");
        throw new IllegalStateException("例外を発生させます");
    };

    @Override
    public void configure() throws Exception {
        from("timer:test-exception?repeatCount=1")
                .to("log:例外テスト開始")
                .doTry()
                .to("log:例外発生前")
                .process(this.throwExceptionProcessor)
                .to("log:例外発生後")
                .doCatch(Exception.class)
                .to("log:例外をcatchしました")
                .doFinally()
                .to("log:例外処理の終了")
                .end();

        from("timer:test-exception-each?repeatCount=1")
                .to("log:例外テスト開始(each)")
                .process(this.setListProcessor)
                .split(body())
                .doTry()
                .to("log:例外発生前(each)")
                .process(this.throwExceptionProcessor)
                .to("log:例外発生後(each)")
                .doCatch(Exception.class)
                .to("log:例外をcatchしました(each)")
                .doFinally()
                .to("log:例外処理の終了(each)")
                .end();
    }
}
