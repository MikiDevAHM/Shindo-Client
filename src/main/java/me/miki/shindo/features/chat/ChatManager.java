package me.miki.shindo.features.chat;


import java.awt.*;
import java.util.ArrayList;

public class ChatManager {
    private ArrayList<Chat> chatList = new ArrayList<>();

    public ChatManager() { init(); }

    public void init() {
        addChat(new Chat("Chat Configs"));
        addChat(new Chat("Compact Chat", false));
        addChat(new Chat("More Chat History", false));
        addChat(new Chat("Keep Chat History", false));
        addChat(new Chat("Background Color", new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));

        addChat(new Chat("Chat Pings"));
        addChat(new Chat("Highlight", false));
        addChat(new Chat("Highlight Background", false));
        addChat(new Chat("Background Color", new Color(255, 255, 255), new Color(255, 0, 0), 0, new float[]{0, 0}));

        String[] mode = { "Gold", "Green", "Blue", "Red", "Yellow", "Magenta", "Cyan", "White" };
        addChat(new Chat("Ping Text Color", "Gold", 0, mode));
        addChat(new Chat("Own Messages", false));
        addChat(new Chat("Bold", false));
        addChat(new Chat("Ping Sound", false));
        addChat(new Chat("Ping Sound Volume", 100, 50));


    }

    public ArrayList<Chat> getChat() { return chatList; }

    public void addChat(Chat chat) { chatList.add(chat); }

    public Chat getChatByName(String name) {
        for (Chat chat : chatList) {
            if (chat.getName().equalsIgnoreCase(name)) {
                return chat;
            }
        }
        return null;
    }
}