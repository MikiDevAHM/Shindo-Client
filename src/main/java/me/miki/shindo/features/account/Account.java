package me.miki.shindo.features.account;

import java.io.File;

public class Account {

    private String name;
    private final String email;
    private final String password;
    private final AccountType type;

    private File skinFile;

    public Account(String name, String email, String password, AccountType type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public AccountType getType() {
        return type;
    }

    public File getSkinFile() {
        return skinFile;
    }

    public void setSkinFile(File skinFile) {
        this.skinFile = skinFile;
    }

}
