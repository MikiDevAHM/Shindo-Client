package me.miki.shindo;

import me.miki.shindo.api.ClientApiManager;
import me.miki.shindo.api.utils.SSLBypass;
import net.minecraft.client.Minecraft;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ShindoAPI {

    String username = Minecraft.getMinecraft().getSession().getUsername();
    String uuid;
    String accountType;

    {
        UUID profileId = Minecraft.getMinecraft().getSession().getProfile().getId();
        if (profileId == null) {
            uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8)).toString();
            accountType = "CRACKED";
        } else {
            uuid = profileId.toString();
            accountType = "PREMIUM";
        }
    }

    private ClientApiManager apiManager = new ClientApiManager(uuid, username, accountType);

    public void start() {
        SSLBypass.disableCertificateValidation();
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

}