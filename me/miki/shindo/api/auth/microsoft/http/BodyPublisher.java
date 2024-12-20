package me.miki.shindo.api.auth.microsoft.http;

public class BodyPublisher {
    String body;

    public BodyPublisher(String body) {
        this.body = body;
    }

    public static BodyPublisher ofString(String body) {
        return new BodyPublisher(body);
    }

	public String getBody() {
		return body;
	}
}
