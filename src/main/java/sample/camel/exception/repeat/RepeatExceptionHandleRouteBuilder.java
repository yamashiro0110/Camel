package sample.camel.exception.repeat;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by yamashiro-r on 2016/09/12.
 */
@SpringBootApplication
@Component
public class RepeatExceptionHandleRouteBuilder extends RouteBuilder {
    private static Logger logger = LoggerFactory.getLogger(RepeatExceptionHandleRouteBuilder.class);

    public static void main(final String[] args) {
        SpringApplication.run(RepeatExceptionHandleRouteBuilder.class, args);
    }

    private Processor setListProcessor = exchange -> {
        exchange.getIn().setBody(Arrays.asList("hoge", "fuga", "piyo"));
    };

    private Processor throwExceptionProcessor = exchange -> {
        throw new IllegalStateException("例外を発生させます");
    };

    private Processor catchExceptionHandler = exchange -> {
        String body = exchange.getIn().getBody(String.class);
        logger.error("例外が発生しました body: {}", body);
    };

    @Override
    public void configure() throws Exception {
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
                .process(this.catchExceptionHandler)
                .doFinally()
                .to("log:例外処理の終了(each)")
                .end();
    }
}
