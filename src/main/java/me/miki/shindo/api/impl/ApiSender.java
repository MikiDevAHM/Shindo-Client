package me.miki.shindo.api.impl;

import com.google.gson.JsonObject;
import me.miki.shindo.logger.ShindoLogger;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
            try {
                ShindoLogger.info("[API] Enviando evento: " + json.toString());

                URL url = new URL(API_BASE + "/client-status");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                con.setConnectTimeout(3000);
                con.setReadTimeout(3000);

                try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8)) {
                    writer.write(json.toString());
                    writer.flush();
                }

                int code = con.getResponseCode();
                if (code != 200) {
                    throw new Exception("Código de resposta HTTP: " + code);
                }

                ShindoLogger.info("[API] Evento enviado com sucesso.");

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