package vertx.demo;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class VerticleDemo extends AbstractVerticle {
    public static final Logger LOGGER = Logger.getLogger(VerticleDemo.class);
    private HttpServer server;

    static {
        BasicConfigurator.configure();
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(VerticleDemo.class.getName(), res -> {
            if (res.succeeded()) {
                System.out.println("Deployment is successful");
            } else {
                System.out.println("Deployment failed!");
            }
        });
    }

    @Override
    public void start(Future<Void> startFuture) {
        server = vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        });

        // Now bind the server:
        server.listen(8080, res -> {
            if (res.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(res.cause());
            }
        });
    }

}


