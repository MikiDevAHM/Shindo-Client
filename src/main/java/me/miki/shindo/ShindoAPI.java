package me.miki.shindo;

import me.miki.shindo.api.ClientApiManager;
import net.minecraft.client.Minecraft;

import java.util.Objects;

public class ShindoAPI {

    String uuid = Minecraft.getMinecraft().getSession().getPlayerID();
    String username = Minecraft.getMinecraft().getSession().getUsername();
    String accountType = uuid.equals(username) ? "CRACKED" : "PREMIUM";

    private ClientApiManager apiManager = new ClientApiManager(uuid, username, accountType);
    public void start() {
        apiManager.notifyEvent("join");
    }

    public void stop() {
        apiManager.notifyEvent("leave");
        apiManager.shutdown();
    }



    public boolean isStaff(String uuid) {
        //ShindoLogger.info("[API] UUID: " + uuid);
        //ShindoLogger.info("[API] STAFF: " + apiManager.isStaff(uuid));
        return apiManager.isStaff(uuid);
    }

    public boolean isDiamond(String uuid) {
        //ShindoLogger.info("[API] UUID: " + uuid);
        //ShindoLogger.info("[API] DIAMOND: " + apiManager.isDiamond(uuid));
        return apiManager.isDiamond(uuid);
    }

    public boolean isGold(String uuid) {
        //ShindoLogger.info("[API] UUID: " + uuid);
        //ShindoLogger.info("[API] GOLD: " + apiManager.isGold(uuid));
        return apiManager.isGold(uuid);
    }

    public boolean isOnline(String uuid) {
        //ShindoLogger.info("[API] UUID: " + uuid);
        //ShindoLogger.info("[API] ONLINE: " + apiManager.isOnline(uuid));
        return  apiManager.isOnline(uuid);
    }

    public boolean hasPrivilege(String uuid, String privilege) {
        //ShindoLogger.info("[API] UUID: " + uuid);
        //ShindoLogger.info("[API] PRIVILEGE: " + apiManager.hasPrivilege(uuid, privilege));
        return apiManager.hasPrivilege(uuid, privilege);

    }

    public String getName (String uuid) {
        //ShindoLogger.info("[API] UUID: " + uuid);
        //ShindoLogger.info("[API] getName: " + apiManager.getName(uuid));
        return apiManager.getName(uuid);
    }

    public String getAccountType (String uuid) {
        //ShindoLogger.info("[API] UUID: " + uuid);
        //ShindoLogger.info("[API] getAccountType: " + apiManager.getAccountType(uuid));
        return apiManager.getAccountType(uuid);
    }

    public boolean isUUIDBad() {
        return Objects.equals(uuid, username);
    }

}