package me.miki.shindo.features.notification;

import me.miki.shindo.events.EventManager;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {

    private final LinkedBlockingQueue<Notification> notifications = new LinkedBlockingQueue<Notification>();

    public NotificationManager() {
        EventManager.register(new NotificationHandler(notifications));
    }

    public void post(String title, String message, NotificationType type) {
        notifications.add(new Notification(title, message, type));
    }
}
