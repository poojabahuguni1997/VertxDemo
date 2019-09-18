package vertx.demo;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

public class VertxDemo {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(request -> {
//            for(int i =0;i<10;i++){
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            request.response().end("hello world!");
        });

        server.listen(8080);

    }
}
