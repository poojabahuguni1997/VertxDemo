package vertx.demo;

import io.vertx.core.*;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

public class VertxWebDemo extends AbstractVerticle {
    public static final Logger LOGGER = Logger.getLogger(VertxWebDemo.class);

    static {
        BasicConfigurator.configure();
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(VertxWebDemo.class.getName());
    }

    @Override
    public void start(Future<Void> fut) {
        // Create a router object.
        Router router = Router.router(vertx);

        // Bind "/" to our hello message - so we are still compatible.
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello from my first Vert.x 3 application</h1>");
        });

        router.route("/get/*").handler(BodyHandler.create());
        router.get("/get/json").handler(this::getJson);
        //path param
        router.get("/get/message/:message").handler(this::getMessage);
        router.get("/get/async").handler(this::getAsync);

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        8080,
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }



    private void getJson(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("Content-Type", "application/json");
        JSONObject result = new JSONObject();
        result.put("error","null") ;
        result.put("result","success") ;
        response.end(io.vertx.core.json.Json.encodePrettily(result));
    }

    private void getMessage(RoutingContext routingContext) {
        String message = routingContext.request().getParam("message");// path param
        String message1 = routingContext.request().getParam("msg");// query param
        System.out.println(message);
        System.out.println(message1);
        routingContext.response().end("Success");
    }

    private void getAsync(RoutingContext routingContext) {

        this.isInteger(routingContext, ar -> {
            if(ar.succeeded()){
                routingContext.response().end(ar.result());
            }else{
                routingContext.response().end(ar.cause().getMessage());
            }
        });

    }

    void isInteger(RoutingContext rc, Handler<AsyncResult<String>> handler){

        String value = rc.request().getParam("value");

        try {
            int num = Integer.parseInt(value);
            handler.handle(Future.succeededFuture(value));
        }catch (Exception e){
            handler.handle(Future.failedFuture(e.fillInStackTrace()));
        }


    }
}