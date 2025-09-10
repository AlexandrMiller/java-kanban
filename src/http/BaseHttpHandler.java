package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    public BaseHttpHandler() {

    }

    public void sendText(HttpExchange e, String text, int statusCode) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        e.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
        e.sendResponseHeaders(statusCode,response.length);
        e.getResponseBody().write(response);
        e.close();
    }
}