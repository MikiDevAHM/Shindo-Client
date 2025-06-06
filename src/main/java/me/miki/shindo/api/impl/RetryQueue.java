package me.miki.shindo.api.impl;

import com.google.gson.JsonObject;
import me.miki.shindo.logger.ShindoLogger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RetryQueue {

    private final Queue<JsonObject> queue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public RetryQueue() {
        scheduler.scheduleAtFixedRate(this::retry, 10, 10, TimeUnit.SECONDS);
    }

    public void add(JsonObject json) {
        queue.add(json);
    }

    private void retry() {
        if (queue.isEmpty()) return;

        ShindoLogger.info("[API] Tentando reenviar " + queue.size() + " evento(s) pendente(s)...");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            for (JsonObject json : queue) {
                try {
                    HttpPost post = new HttpPost(ApiSender.API_BASE + "/client-status");
                    post.setHeader("Content-Type", "application/json");
                    post.setEntity(new StringEntity(json.toString(), StandardCharsets.UTF_8));

                    try (CloseableHttpResponse response = client.execute(post)) {
                        int code = response.getStatusLine().getStatusCode();
                        if (code == 200) {
                            queue.remove(json);
                            ShindoLogger.info("[API] Evento reenviado com sucesso.");
                        } else {
                            ShindoLogger.error("[API] Falha no reenvio do evento (HTTP " + code + ").");
                        }
                    }
                } catch (Exception e) {
                    ShindoLogger.error("[API] Erro ao reenviar evento: " + json + " -> " + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            ShindoLogger.error("[API] Erro ao criar HttpClient para retry: " + e.getMessage(), e);
        }
    }

    public void shutdown() {
        scheduler.shutdownNow();
        ShindoLogger.info("[API] RetryQueue encerrada.");
    }
}