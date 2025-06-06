package me.miki.shindo.api.impl;

import com.google.gson.JsonObject;
import me.miki.shindo.logger.ShindoLogger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiSender {

    public static final String API_BASE = "https://shindo-api.netlify.app/api";

    private final ExecutorService senderExecutor = Executors.newFixedThreadPool(2);
    private final RetryQueue retryQueue;

    public ApiSender(RetryQueue retryQueue) {
        this.retryQueue = retryQueue;
    }

    public void sendEvent(JsonObject json) {
        if (json == null) {
            ShindoLogger.warn("[API] Tentativa de envio com JSON nulo.");
            return;
        }

        senderExecutor.submit(() -> {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                ShindoLogger.info("[API] Enviando evento: " + json.toString());

                HttpPost post = new HttpPost(API_BASE + "/client-status");
                post.setHeader("Content-Type", "application/json");
                post.setEntity(new StringEntity(json.toString(), StandardCharsets.UTF_8));

                try (CloseableHttpResponse response = client.execute(post)) {
                    int code = response.getStatusLine().getStatusCode();
                    if (code != 200) {
                        throw new Exception("Código de resposta HTTP: " + code);
                    }

                    ShindoLogger.info("[API] Evento enviado com sucesso.");
                }

            } catch (Exception e) {
                ShindoLogger.error("[API] Falha ao enviar evento, adicionando à fila: " + e.getMessage(), e);
                retryQueue.add(json);
            }
        });
    }

    public void shutdown() {
        senderExecutor.shutdownNow();
        ShindoLogger.info("[API] ApiSender encerrado.");
    }
}