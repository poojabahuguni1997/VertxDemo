package vertx.demo;

import io.vertx.core.*;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainVerticle extends AbstractVerticle {

    public static final Logger logger = Logger.getLogger(MainVerticle.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MainVerticle.class.getName());
    }


    @Override
    public void start(Future<Void> future){

        /** Count of services. */
        final AtomicInteger serviceCount = new AtomicInteger();

        /** List of verticles that we are starting. */
        final List<AbstractVerticle> verticles = Arrays.asList(new SenderVerticle(), new RecieverVerticle());

        verticles.stream().forEach(verticle -> vertx.deployVerticle(verticle, deployResponse -> {

            if (deployResponse.failed()) {
                logger.error("Unable to deploy verticle " + verticle.getClass().getSimpleName(),
                        deployResponse.cause());
            } else {
                logger.info(verticle.getClass().getSimpleName() + " deployed");
                serviceCount.incrementAndGet();
            }
        }));


        /** Wake up in five seconds and check to see if we are deployed if not complain. */
        vertx.setTimer(TimeUnit.SECONDS.toMillis(5), event -> {

            if (serviceCount.get() != verticles.size()) {
                logger.error("Main Verticle was unable to start child verticles");
            } else {
                logger.info("Start up successful");
            }
        });

    }
}
