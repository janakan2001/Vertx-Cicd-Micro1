package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      String authorization = req.getHeader("Authorization");

      if (authorization == null || !authorization.startsWith("Bearer ")) {
        req.response()
          .setStatusCode(401)
          .putHeader("content-type", "text/plain")
          .end("Unauthorized: Missing or invalid Authorization header inside MicroService1");
        return;
      }

      String token = authorization.substring(7);

      if ("my-secret-token".equals(token)) {
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x! You are authorized Inside MicroService 1.");
      } else {
        req.response()
          .setStatusCode(401)
          .putHeader("content-type", "text/plain")
          .end("Unauthorized: Invalid token");
      }
    }).listen(8889).onComplete(http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8889");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
