package me.miki.shindo.api.impl;

import com.google.gson.JsonObject;
import me.miki.shindo.logger.ShindoLogger;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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

        for (JsonObject json : queue) {
            try {
                URL url = new URL(ApiSender.API_BASE + "/client-status");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);

                try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
                    writer.write(json.toString());
                    writer.flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    queue.remove(json);
                    ShindoLogger.info("[API] Evento reenviado com sucesso.");
                } else {
                    ShindoLogger.error("[API] Falha no reenvio do evento (HTTP " + responseCode + ").");
                }

            } catch (Exception e) {
                ShindoLogger.error("[API] Erro ao reenviar evento: " + json + " -> " + e.getMessage(), e);
            }
        }
    }

    public void shutdown() {
        scheduler.shutdownNow();
        ShindoLogger.info("[API] RetryQueue encerrada.");
    }
}