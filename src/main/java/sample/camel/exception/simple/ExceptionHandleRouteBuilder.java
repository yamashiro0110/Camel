package sample.camel.exception.simple;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by yamashiro-r on 2016/09/12.
 *
 * @see <a href="http://camel.apache.org/try-catch-finally.html">try-catch-finally</a>
 * @see <a href="http://camel.apache.org/exception-clause.html">exception-clause</a>
 */
@SpringBootApplication
@Component
public class ExceptionHandleRouteBuilder extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandleRouteBuilder.class);

    public static void main(final String[] args) {
        SpringApplication.run(ExceptionHandleRouteBuilder.class, args);
    }

    private Processor throwExceptionProcessor = exchange -> {
        exchange.getIn().setBody("例外を発生させます");
        throw new IllegalStateException("例外を発生させます");
    };

    private Processor exceptionHandleProcessor = exchange -> {
        Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
        Assert.notNull(throwable);
        LOGGER.error("エラーが発生しました", throwable);
        // TODO: exception handling
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
                .process(this.exceptionHandleProcessor)
                .doFinally()
                .to("log:例外処理の終了")
                .end();
    }
}
