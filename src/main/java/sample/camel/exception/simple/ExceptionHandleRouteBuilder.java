package sample.camel.exception.simple;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * Created by yamashiro-r on 2016/09/12.
 */
@SpringBootApplication
@Component
public class ExceptionHandleRouteBuilder extends RouteBuilder {

    public static void main(final String[] args) {
        SpringApplication.run(ExceptionHandleRouteBuilder.class, args);
    }

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
    }
}
