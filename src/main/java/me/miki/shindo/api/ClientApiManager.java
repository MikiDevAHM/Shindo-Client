package me.miki.shindo.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.miki.shindo.api.impl.ApiChecker;
import me.miki.shindo.api.impl.ApiSender;
import me.miki.shindo.api.impl.RetryQueue;
import me.miki.shindo.logger.ShindoLogger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientApiManager {

    private final String uuid;
    private final String name;
    private final String accountType;

    private final RetryQueue retryQueue;
    private final ApiSender apiSender;
    private final ApiChecker apiChecker;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final Set<String> watchedUUIDs = ConcurrentHashMap.newKeySet();

    // CACHES
    private final Map<String, Boolean> onlineCache = new ConcurrentHashMap<>();
    private final Map<String, JsonArray> privilegesCache = new ConcurrentHashMap<>();
    private final Map<String, String> nameCache = new ConcurrentHashMap<>();
    private final Map<String, String> accountTypeCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lastUpdateTimestamps = new ConcurrentHashMap<>();

    private static final long CACHE_TTL_MS = 5000;

    public ClientApiManager(String uuid, String name, String accountType) {

        this.uuid = uuid;
        this.name = name;
        this.accountType = accountType;

        this.retryQueue = new RetryQueue();
        this.apiSender = new ApiSender(retryQueue);
        this.apiChecker = new ApiChecker();

        startAutoUpdater();
    }

    public void notifyEvent(String eventType) {
        JsonObject json = new JsonObject();
        json.addProperty("uuid", uuid);
        json.addProperty("name", name);
        json.addProperty("accountType", accountType);
        json.addProperty("eventType", eventType);

        apiSender.sendEvent(json);
    }

    private boolean shouldUpdateCache(String uuid) {
        long now = System.currentTimeMillis();
        return !lastUpdateTimestamps.containsKey(uuid) || (now - lastUpdateTimestamps.get(uuid)) > CACHE_TTL_MS;
    }

    public void updateCache(String uuid) {
        if (!shouldUpdateCache(uuid)) return;

        try {
            JsonObject json = apiChecker.getUserInfo(uuid);
            if (json != null) {
                // Somente atualiza o cache se os dados forem válidos
                if (json.has("online")) {
                    onlineCache.put(uuid, json.get("online").getAsBoolean());
                }

                if (json.has("name") && !json.get("name").isJsonNull()) {
                    nameCache.put(uuid, json.get("name").getAsString());
                }

                if (json.has("accountType") && !json.get("accountType").isJsonNull()) {
                    accountTypeCache.put(uuid, json.get("accountType").getAsString());
                }

                if (json.has("privileges") && json.get("privileges").isJsonArray()) {
                    privilegesCache.put(uuid, json.getAsJsonArray("privileges"));
                }

                lastUpdateTimestamps.put(uuid, System.currentTimeMillis());
            }
        } catch (Exception e) {
            ShindoLogger.warn("[API] Falha ao atualizar cache do UUID " + uuid + ": " + e.getMessage());
            // Não limpa cache — aguarda próxima tentativa de atualização automática
        }
    }

    public boolean isOnline(String uuid) {
        watchUUID(uuid);
        return onlineCache.getOrDefault(uuid, false);
    }

    public String getName(String uuid) {
        watchUUID(uuid);
        return nameCache.get(uuid);
    }

    public String getAccountType(String uuid) {
        watchUUID(uuid);
        return accountTypeCache.get(uuid);
    }

    public boolean hasPrivilege(String uuid, String privilegeKey) {
        watchUUID(uuid); // Garante que o cache esteja populado
        JsonArray privs = privilegesCache.get(uuid);

        if (privs == null) return false;

        for (int i = 0; i < privs.size(); i++) {
            if (privs.get(i).getAsString().equalsIgnoreCase(privilegeKey)) {
                return true;
            }
        }

        return false;
    }

    public boolean isStaff(String uuid) {
        return hasPrivilege(uuid, "Staff");
    }

    public boolean isDiamond(String uuid) {
        return hasPrivilege(uuid, "Diamond");
    }

    public boolean isGold(String uuid) {
        return hasPrivilege(uuid, "Gold");
    }

    private void watchUUID(String uuid) {
        watchedUUIDs.add(uuid);
    }

    private void startAutoUpdater() {
        scheduler.scheduleAtFixedRate(() -> {
            for (String uuid : watchedUUIDs) {
                updateCache(uuid);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void clearCache() {
        onlineCache.clear();
        privilegesCache.clear();
        nameCache.clear();
        accountTypeCache.clear();
        lastUpdateTimestamps.clear();
        watchedUUIDs.clear();
    }

    public void shutdown() {
        apiSender.shutdown();
        retryQueue.shutdown();
        scheduler.shutdownNow();
    }
}