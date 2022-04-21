import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

public class SimpleWebserver {

    public static void main(String... args) {
        new SimpleWebserver().run();

    }

    private void run() {
        var server = SimpleFileServer.createFileServer(new InetSocketAddress(9000),
        Path.of( System.getProperty("user.dir") + "/data"),
        SimpleFileServer.OutputLevel.VERBOSE);
//        server.createContext("/demo",
//                HttpHandlers.handleOrElse(r ->r.getRequestMethod().equalsIgnoreCase("GET"), getHandler, otherHandler));

        server.start();
    }


    HttpHandler getHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().write("This was a GET request".getBytes());
            exchange.getResponseBody().close();
        }
    };

    HttpHandler otherHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().write("This will handle non-GET requests, like POST, PUT, DELETE".getBytes());
            exchange.getResponseBody().close();
        }
    };

    Filter beforeFilter = Filter.beforeHandler("Called before the request is processed",
            exchange -> System.out.println("Before filter, path = " + exchange.getRequestURI()));
}
