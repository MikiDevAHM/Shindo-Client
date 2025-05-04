package me.miki.shindo.features.chat;


import java.util.ArrayList;

public class ChatManager {

    private ArrayList<Chat> chatList = new ArrayList<>();

    public ChatManager() { init(); }

    public void init() {
        addChat(new Chat("Options"));
        addChat(new Chat("Draw Background", true));
        addChat(new Chat("Chat Heads", false));
        addChat(new Chat("Compact Chat", false));
        addChat(new Chat("Infinite Chat", false));

        addChat(new Chat("Smooth Chat", false));
        addChat(new Chat("Smooth Speed", 10, 4));
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