package me.miki.shindo.features.notification;

import me.miki.shindo.events.EventTarget;
import me.miki.shindo.events.impl.RenderNotificationEvent;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationHandler {

    private final LinkedBlockingQueue<Notification> notifications;
    private Notification currentNotification;

    public NotificationHandler(LinkedBlockingQueue<Notification> notifications) {
        this.notifications = notifications;
    }

    @EventTarget
    public void onRenderNotification(RenderNotificationEvent event) {

        if (currentNotification != null && !currentNotification.isShown()) {
            currentNotification = null;
        }

        if (currentNotification == null && !notifications.isEmpty()) {
            currentNotification = notifications.poll();
            currentNotification.show();
        }

        if (currentNotification != null) {
            currentNotification.draw();
        }
    }
}
