package me.miki.shindo.helpers.bash;

import java.io.IOException;

public class Bash {

    public static void execute(String command) throws IOException {
        Runtime.getRuntime().exec(command);
    }

    public static void addToRegEdit(String value) throws IOException {
        Runtime.getRuntime().exec("Reg.exe add " + value);
    }

    public static void removeFromRegEdit(String value) throws IOException {
        Runtime.getRuntime().exec("Reg.exe rem " + value);
    }
}
