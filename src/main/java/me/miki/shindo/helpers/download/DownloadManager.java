package me.miki.shindo.helpers.download;

import me.miki.shindo.Shindo;
import me.miki.shindo.helpers.download.file.DownloadFile;
import me.miki.shindo.helpers.download.file.DownloadZipFile;
import me.miki.shindo.helpers.file.FileManager;
import me.miki.shindo.helpers.multithreading.Multithreading;
import me.miki.shindo.helpers.network.HttpHelper;

import java.io.File;
import java.util.ArrayList;

public class DownloadManager {

    private ArrayList<DownloadFile> downloadFiles = new ArrayList<DownloadFile>();

    private boolean downloaded;

    public DownloadManager() {
        FileManager fileManager = Shindo.getInstance().getFileManager();

        downloaded = false;

        //downloadFiles.add(new DownloadFile("https://website.xyz/image.png", "image.png", new File(fileManager.getExternalDir(), "image"), size))

        Multithreading.runAsync(() -> startDownloads());
    }

    private void startDownloads() {

        for(DownloadFile df : downloadFiles) {

            if(!df.getOutputDir().exists()) {
                df.getOutputDir().mkdirs();
            }

            if(df instanceof DownloadZipFile) {

                DownloadZipFile dzf = (DownloadZipFile) df;

                if(FileUtils.getDirectorySize(dzf.getOutputDir()) != dzf.getUnzippedSize()) {

                    File outputFile = new File(dzf.getOutputDir(), dzf.getFileName());

                    HttpHelper.downloadFile(dzf.getUrl(), outputFile);
                    FileUtils.unzip(outputFile, dzf.getOutputDir());
                    outputFile.delete();
                }
            } else {

                File outputFile = new File(df.getOutputDir(), df.getFileName());

                if(outputFile.length() != df.getSize()) {
                    HttpHelper.downloadFile(df.getUrl(), outputFile);
                }
            }
        }

        checkFiles();
    }

    private void checkFiles() {

        for(DownloadFile df : downloadFiles) {

            if(df instanceof DownloadZipFile) {

                DownloadZipFile dzf = (DownloadZipFile) df;

                if(FileUtils.getDirectorySize(dzf.getOutputDir()) != dzf.getUnzippedSize()) {
                    startDownloads();
                }
            } else {

                File outputFile = new File(df.getOutputDir(), df.getFileName());

                if(outputFile.length() != df.getSize()) {
                    startDownloads();
                }
            }
        }

        downloaded = true;
    }

    public boolean isDownloaded() {
        return downloaded;
    }
}
