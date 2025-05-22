package me.miki.shindo.helpers.render;

import me.miki.shindo.Shindo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HeadManager {

    private final File headsDir;

    public HeadManager() {
        // Use o FileManager para pegar a pasta de cache
        this.headsDir = new File(Shindo.getInstance().getFileManager().getCacheDir(), "heads");
        if (!headsDir.exists()) {
            headsDir.mkdirs();
        }
    }

    public File getHeadFor(String username) {
        File headFile = new File(headsDir, username + ".png");

        if (!headFile.exists()) {
            // Baixa do Minotaur.net (garante premium skin e suporte total)
            String url = "https://minotaur.net/heads/" + username + ".png";

            try (InputStream in = new URL(url).openStream();
                 FileOutputStream out = new FileOutputStream(headFile)) {

                byte[] buffer = new byte[4096];
                int n;
                while ((n = in.read(buffer)) > -1) {
                    out.write(buffer, 0, n);
                }

            } catch (IOException e) {
                System.err.println("Erro ao baixar head de " + username + ": " + e.getMessage());
                return null;
            }
        }

        return headFile;
    }
}