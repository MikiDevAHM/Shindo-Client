package me.eldodebug.soar.management.security;

import me.eldodebug.soar.Shindo;

public class SecurityFeature {

	public SecurityFeature() {
		Shindo.getInstance().getEventManager().register(this);
	}
}
