package org.basaraba.flowers.server;

/**
 * represents a server for example netty, undertow, jetty
 */
public interface Server extends AutoCloseable {
    Server start(String host, int port);
}
