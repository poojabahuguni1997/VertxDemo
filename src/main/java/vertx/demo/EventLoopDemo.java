package vertx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class EventLoopDemo extends AbstractVerticle {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        System.out.println("First this is printed");

        vertx.setTimer(2000, id -> {
            System.out.println("And two seconds later this is printed");
        });

        System.out.println("Second this is printed");


    }
}
