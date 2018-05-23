package org.basaraba.utils;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test utility for getting random ports to prevent errors which may occor when OS or usrs do not properly close sockets
 */
public final class Utilities {
    private static final int SOCKET_MIN = 49_152;
    private static final int SOCKET_MAX = 65_535;
    private Utilities() {throw new IllegalStateException("no instances");}

    private static final int MAX_ATTEMPTS = 5;
    public static synchronized int findFreePort() {
        int attempts = 0;
        while (++attempts < MAX_ATTEMPTS) {
            int port = ThreadLocalRandom.current().nextInt(SOCKET_MIN, SOCKET_MAX);
            try (ServerSocket socket = new ServerSocket(port)) {
                socket.close();
                return port;
            } catch (IOException ex) {
                throw new RuntimeException("problem getting port");
            }
        }
        throw new RuntimeException("Could not find free port after: " + MAX_ATTEMPTS + " number of attempts.");
    }
}
