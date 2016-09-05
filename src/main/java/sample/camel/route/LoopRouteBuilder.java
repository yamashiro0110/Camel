package sample.camel.route;

import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

import static java.util.Arrays.asList;

/**
 * Created by yamashiro-r on 2016/09/05.
 */
@Component
public class LoopRouteBuilder extends RouteBuilder {

    private Predicate predicate = exchange -> {
        Queue queue = exchange.getIn().getBody(Queue.class);
        return !queue.isEmpty();
    };

    private Processor setListProcessor = exchange -> {
        exchange.getIn().setBody(new Queue(), Queue.class);
    };

    private Processor printProcessor = exchange -> {
        Queue queue = exchange.getIn().getBody(Queue.class);
        System.out.println(queue.dequeue());
        exchange.getIn().setBody(queue, Queue.class);
    };

    @Override
    public void configure() throws Exception {
        from("timer:test-loop?period=3000")
                .process(this.setListProcessor)
                .loopDoWhile(this.predicate)
                .process(this.printProcessor);
    }

    private class Queue {
        private LinkedList<String> list = new LinkedList<>(asList("hoge", "fuga", "piyo"));

        boolean isEmpty() {
            return this.list.isEmpty();
        }

        String dequeue() {
            if (this.isEmpty()) throw new IllegalStateException();
            final String value = this.list.getFirst();
            this.list.removeFirst();
            return value;
        }
    }

}
