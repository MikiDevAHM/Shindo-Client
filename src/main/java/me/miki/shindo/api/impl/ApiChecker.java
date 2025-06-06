package me.miki.shindo.api.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.miki.shindo.logger.ShindoLogger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApiChecker {

    private static final long CACHE_EXPIRATION_MS = 30_000; // 30 segundos

    private final Map<String, CacheEntry> userCache = new ConcurrentHashMap<>();

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
        String url = ApiSender.API_BASE + "/get-user?uuid=" + uuid;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("Accept", "application/json");

            try (CloseableHttpResponse response = client.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != 200) {
                    ShindoLogger.error("[API] Erro ao obter informações para UUID " + uuid + ": HTTP " + statusCode);
                    return null;
                }

                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return null;
                }

                String responseBody = EntityUtils.toString(entity);
                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

                ensureDefault(json, "online", false);
                ensureDefault(json, "name", (String) null);
                ensureDefault(json, "accountType", (String) null);

                if (!json.has("privileges") || !json.get("privileges").isJsonArray()) {
                    json.add("privileges", new JsonArray());
                }

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
}