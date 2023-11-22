package com.shaurmalay.bot.model.requests;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class HTTPRequestService {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void sendPost(String url, String body) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }
}
