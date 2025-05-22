package me.miki.shindo.features.notification;

import me.miki.shindo.features.nanovg.font.Icon;

public enum NotificationType {
    INFO(Icon.INFO),
    WARNING(Icon.ALERT_TRIANGLE),
    ERROR(Icon.X_CIRCLE),
    SUCCESS(Icon.CHECK),
    MUSIC(Icon.MUSIC);

    ;

    private final String icon;

    NotificationType(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
