package vertx.demo;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class BlockingCode{
    public static final Logger LOGGER = Logger.getLogger(BlockingCode.class);

    static {
        BasicConfigurator.configure();
    }

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        HttpServer server = vertx.createHttpServer();

        server.requestHandler(request -> {
            HttpServerResponse response = request.response();
            vertx.executeBlocking(handler -> {

                LOGGER.info("Excecuting Blocking Code");
                for(int i =0;i<10;i++){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.complete("success");
            }, false,res -> {

                if(res.succeeded()){
                    response.end(res.result().toString());
                }

            });

        });

        server.listen(8080);

    }
}
