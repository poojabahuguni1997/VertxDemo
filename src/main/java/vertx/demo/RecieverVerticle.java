package vertx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class RecieverVerticle extends AbstractVerticle {
    public static final Logger LOGGER = Logger.getLogger(RecieverVerticle.class);

    static {
        BasicConfigurator.configure();
    }

    @Override
    public void start() throws Exception {
        final EventBus eventBus = vertx.eventBus();
        eventBus.consumer(RecieverVerticle.class.getName(), receivedMessage -> {
            LOGGER.debug("Received message: " + receivedMessage.body());
            receivedMessage.reply("Got Message");
        });
        LOGGER.info("Receiver ready!");
    }
}
