package me.miki.shindo.helpers.file;

import me.miki.shindo.helpers.file.filter.PngFileFilter;
import me.miki.shindo.helpers.file.filter.SoundFileFilter;
import me.miki.shindo.helpers.logger.ShindoLogger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.swing.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class FileHelper {

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }

            return directory.delete();
        }
        return false;
    }

    public static long getDirectorySize(File directory) {

        long size = 0;

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        size += file.length();
                    } else if (file.isDirectory()) {
                        size += getDirectorySize(file);
                    }
                }
            }
        } else if (directory.isFile()) {
            size = directory.length();
        }

        return size;
    }

    public static void unzip(final File file, final File dest) {
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                final File f = new File(dest, ze.getName());
                if (ze.isDirectory()) {
                    f.mkdirs();
                } else {
                    final FileOutputStream fos = new FileOutputStream(f);
                    final byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File selectImageFile() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileFilter(new PngFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    public static File selectSoundFile() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileFilter(new SoundFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {

        FileInputStream input = null;
        FileOutputStream output = null;

        try {

            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    public static void downloadFile(String url, File output) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    try (InputStream inputStream = entity.getContent();
                         OutputStream outputStream = new FileOutputStream(output)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
            httpClient.close();
        } catch (Exception e) {
            ShindoLogger.error("Failed to download file: " + url, e);
        }
    }

    public static String getBaseName(String fileName) {

        if (fileName == null) {
            return "null";
        }

        int point = fileName.lastIndexOf(".");

        if (point != -1) {
            return fileName.substring(0, point);
        }

        return fileName;
    }

    public static String getBaseName(File file) {
        return getBaseName(file.getName());
    }

    public static String getExtension(String fileName) {

        if (fileName == null) {
            return null;
        }

        int lastIndexOf = fileName.lastIndexOf(".");

        if (lastIndexOf == -1) {
            return "null";
        }

        return fileName.substring(lastIndexOf + 1);
    }

    public static String getExtension(File file) {
        return getExtension(file.getName());
    }

    public static boolean isAudioFile(String fileName) {

        if (fileName == null) {
            return false;
        }

        String ext = getExtension(fileName);

        return ext.equals("mp3") || ext.equals("wav") || ext.equals("ogg");
    }

    public static boolean isAudioFile(File file) {
        return isAudioFile(file.getName());
    }

    public static boolean isImageFile(String fileName) {

        if (fileName == null) {
            return false;
        }

        String ext = getExtension(fileName);

        return ext.equals("jpeg") || ext.equals("png") || ext.equals("jpg");
    }

    public static boolean isImageFile(File file) {
        return isImageFile(file.getName());
    }
}
