package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.IChatComponent;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ChatLine
{
    /** GUI Update Counter value this Line was created at */
    private final int updateCounterCreated;
    private final IChatComponent lineString;

    HashSet<WeakReference<ChatLine>> chatLines = new HashSet<>();
    private NetworkPlayerInfo playerInfo;

    /**
     * int value to refer to existing Chat Lines, can be 0 which means unreferrable
     */
    private final int chatLineID;

    public ChatLine(int p_i45000_1_, IChatComponent p_i45000_2_, int p_i45000_3_)
    {
        this.lineString = p_i45000_2_;
        this.updateCounterCreated = p_i45000_1_;
        this.chatLineID = p_i45000_3_;

        chatLines.add(new WeakReference<>((ChatLine) (Object) this));
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
        if (netHandler == null) return;
        Map<String, NetworkPlayerInfo> nicknameCache = new HashMap<>();

        try {
            for (String word : p_i45000_2_.getFormattedText().split("(§.)|\\W")) {

                if (word.isEmpty()) {
                    continue;
                }

                playerInfo = netHandler.getPlayerInfo(word);

                if (playerInfo == null) {
                    playerInfo = getPlayerFromNickname(word, netHandler, nicknameCache);
                }

                if (playerInfo != null) {
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    public IChatComponent getChatComponent()
    {
        return this.lineString;
    }

    public int getUpdatedCounter()
    {
        return this.updateCounterCreated;
    }

    public int getChatLineID()
    {
        return this.chatLineID;
    }

    private static NetworkPlayerInfo getPlayerFromNickname(String word, NetHandlerPlayClient connection, Map<String, NetworkPlayerInfo> nicknameCache) {

        if (nicknameCache.isEmpty()) {
            for (NetworkPlayerInfo p : connection.getPlayerInfoMap()) {

                IChatComponent displayName = p.getDisplayName();

                if (displayName != null) {
                    String nickname = displayName.getUnformattedTextForChat();

                    if (word.equals(nickname)) {
                        nicknameCache.clear();
                        return p;
                    }

                    nicknameCache.put(nickname, p);
                }
            }
        } else {
            return nicknameCache.get(word);
        }

        return null;
    }

    public NetworkPlayerInfo getPlayerInfo() {
        return playerInfo;
    }
}
