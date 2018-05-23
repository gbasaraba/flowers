package org.basaraba.flowers.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

public class ServerNettyImpl implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerNettyImpl.class);
    private final RouterFunction<?> route;
    private HttpServer nettyServer;
    private int port;
    private String host;
    private final String name;

    public ServerNettyImpl(String name, RouterFunction<?> route) {
        this.route = route;
        this.name = name;
    }

    @Override
    public Server start(String host, int port) {
        this.port = port;
        this.host = host;
        this.startReactorServer();
        return this;
    }

    @Override
    public void close() throws Exception {
        //TODO(GregB)
    }

    private void startReactorServer() {
        HttpHandler httpHandler = toHttpHandler(this.route);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        this.   nettyServer = HttpServer.create(host, port);
        LOGGER.error("Starting server {} {}:{}", this.name, this.host, this.port);
        this.nettyServer.newHandler(adapter).block();
    }
}