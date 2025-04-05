package me.miki.shindo.features.security;

import me.miki.shindo.Shindo;

public class Security {

    public Security() {
        Shindo.getInstance().getEventManager().register(this);
    }
}
