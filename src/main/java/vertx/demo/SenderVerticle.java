package vertx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SenderVerticle extends AbstractVerticle {
    public static final Logger LOGGER = Logger.getLogger(SenderVerticle.class);

    static {
        BasicConfigurator.configure();
    }


    @Override
    public void start(Future<Void> fut) {
        final Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html").end("<h1>Hello from non-clustered messenger example!</h1>");
        });
        router.post("/send").handler(this::sendMessage);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config()
                        .getInteger("http.server.port", 8080), result -> {
                    if (result.succeeded()) {
                        LOGGER.info("HTTP server running on port " + 8080);
                        fut.complete();
                    } else {
                        LOGGER.error("Could not start a HTTP server", result.cause());
                        fut.fail(result.cause());
                    }
                });
    }

    private void sendMessage(RoutingContext routingContext) {
        final EventBus eventBus = vertx.eventBus();
        final String message = routingContext.request().getParam("message");
        eventBus.send(RecieverVerticle.class.getName(), message, reply -> {
            if (reply.succeeded()) {
                LOGGER.info("Received reply: " + reply.result().body());
            } else {
                LOGGER.info("No reply");
            }
        });
        routingContext.response().end(message);
    }
}
