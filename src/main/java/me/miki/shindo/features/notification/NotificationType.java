package me.miki.shindo.features.notification;

public enum NotificationType {
	INFO("icon/info.png"),
	WARNING("icon/warning.png"),
	ERROR("icon/error.png"),
	SUCCESS("icon/success.png"),
	MUSIC("icon/music.png"),;
	
	private String icon;
	
	private NotificationType(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}
}
