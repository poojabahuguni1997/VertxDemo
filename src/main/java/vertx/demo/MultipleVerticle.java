package vertx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class MultipleVerticle extends AbstractVerticle {

    public static final Logger LOGGER = Logger.getLogger(MultipleVerticle.class);
    private HttpServer server;
    static {
        BasicConfigurator.configure();
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(MultipleVerticle.class.getName(), new DeploymentOptions().setInstances(3));
    }

    @Override
    public void start(Future<Void> fut) {
        server = vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        });

        // Now bind the server:
        server.listen(8080, res -> {
            if (res.succeeded()) {
                LOGGER.info("HTTP server running on port " + 8080);
                fut.complete();
            } else {
                fut.fail(res.cause());
            }
        });
    }

}
