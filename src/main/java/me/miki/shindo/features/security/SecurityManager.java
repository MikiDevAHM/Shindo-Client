package me.miki.shindo.features.security;

import me.miki.shindo.features.security.impl.*;

import java.util.ArrayList;

public class SecurityManager {

    private ArrayList<Security> features = new ArrayList<Security>();

    public SecurityManager() {
        features.add(new DemoSecurity());
        features.add(new ExplosionSecurity());
        features.add(new Log4jSecurity());
        features.add(new ParticleSecurity());
        features.add(new ResourcePackSecurity());
        features.add(new TeleportSecurity());
    }

    public ArrayList<Security> getFeatures() {
        return features;
    }
}
