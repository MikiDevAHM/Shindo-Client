package me.miki.shindo.api.auth.microsoft.http;

public class HttpResponse {
    int statusCode;
    String body;

    public HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int statusCode() {
        return statusCode;
    }

    public String body() {
        return body;
    }
}
