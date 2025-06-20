package me.miki.shindo.api.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.miki.shindo.logger.ShindoLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ApiChecker {

    private final Set<String> failedLookups = ConcurrentHashMap.newKeySet();

    private static final long CACHE_EXPIRATION_MS = 30_000; // 30 segundos

    private final Map<String, CacheEntry> userCache = new ConcurrentHashMap<>();

    private static final Set<String> globalFailedLookups = ConcurrentHashMap.newKeySet();

    public static boolean globalFailedLookup(String uuid) {
        return globalFailedLookups.contains(uuid);
    }

    private void markFailed(String uuid) {
        failedLookups.add(uuid);
        globalFailedLookups.add(uuid);
    }


    private static class CacheEntry {
        JsonObject data;
        long timestamp;

        CacheEntry(JsonObject data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_EXPIRATION_MS;
        }
    }

    public JsonObject getUserInfo(String uuid) {
        CacheEntry cached = userCache.get(uuid);
        if (cached != null && !cached.isExpired()) {
            return cached.data;
        }

        JsonObject json = fetchUserInfo(uuid);
        if (json != null) {
            userCache.put(uuid, new CacheEntry(json));
        }

        return json != null ? json : defaultJson();
    }

    private JsonObject fetchUserInfo(String uuid) {
        try {
            URL url = new URL(ApiSender.API_BASE + "/client-status?uuid=" + uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                ShindoLogger.error("[API] Erro ao obter informações para UUID " + uuid + ": HTTP " + responseCode);
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JsonObject json = JsonParser.parseString(response.toString()).getAsJsonObject();

                ensureDefault(json, "online", false);
                ensureDefault(json, "name", (String) null);
                ensureDefault(json, "accountType", (String) null);

                if (!json.has("privileges") || !json.get("privileges").isJsonArray()) {
                    json.add("privileges", new JsonArray());
                }

                ShindoLogger.info(response.toString());
                return json;
            }

        } catch (Exception e) {
            ShindoLogger.error("[API] Erro ao obter dados do usuário " + uuid + ": " + e.getMessage(), e);
            return null;
        }
    }


    private JsonObject defaultJson() {
        JsonObject json = new JsonObject();
        json.addProperty("online", false);
        json.addProperty("name", (String) null);
        json.addProperty("accountType", (String) null);
        json.add("privileges", new JsonArray()); // Corrigido: JsonArray, não JsonObject
        return json;
    }

    private void ensureDefault(JsonObject obj, String key, Boolean defaultValue) {
        if (!obj.has(key)) {
            obj.addProperty(key, defaultValue);
        }
    }

    private void ensureDefault(JsonObject obj, String key, String defaultValue) {
        if (!obj.has(key)) {
            obj.addProperty(key, defaultValue);
        }
    }

    public void invalidateUser(String uuid) {
        userCache.remove(uuid);
    }

    public boolean hasFailedLookup(String uuid) {
        return failedLookups.contains(uuid);
    }
}

