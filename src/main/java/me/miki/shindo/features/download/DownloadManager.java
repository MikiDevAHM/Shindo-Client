package me.miki.shindo.features.download;

import me.miki.shindo.Shindo;
import me.miki.shindo.features.download.file.DownloadFile;
import me.miki.shindo.features.download.file.DownloadZipFile;
import me.miki.shindo.helpers.file.FileHelper;
import me.miki.shindo.features.file.FileManager;
import me.miki.shindo.helpers.Multithreading;
import me.miki.shindo.helpers.network.HttpHelper;

import java.io.File;
import java.util.ArrayList;


public class DownloadManager {

    private final ArrayList<DownloadFile> downloadFiles = new ArrayList<DownloadFile>();

    private boolean downloaded;

    public DownloadManager() {

        FileManager fileManager = Shindo.getInstance().getFileManager();

        downloaded = false;

        downloadFiles.add(new DownloadFile("https://www.dropbox.com/scl/fi/8ytipx5ekzti37cofsk2p/ytdlp.exe?rlkey=kxdld220hhr7mz9dc9vheemcz&st=qoffgkq1&dl=1",
                "ytdlp.exe", new File(fileManager.getExternalDir(), "ytdlp"), 18145266));
        downloadFiles.add(new DownloadZipFile("https://www.dropbox.com/scl/fi/jjk44dfze2uu05qltfi4d/ffmpeg.zip?rlkey=hsb4375u7tav54fht58gsef7t&st=a07r3syl&dl=1",
                "ffmpeg.zip", new File(fileManager.getExternalDir(), "ffmpeg"), 182914028, 472508356));
        Multithreading.runAsync(() -> startDownloads());
    }

    private void startDownloads() {

        for (DownloadFile df : downloadFiles) {

            if (!df.getOutputDir().exists()) {
                df.getOutputDir().mkdirs();
            }

            if (df instanceof DownloadZipFile) {

                DownloadZipFile dzf = (DownloadZipFile) df;

                if (FileHelper.getDirectorySize(dzf.getOutputDir()) != dzf.getUnzippedSize()) {

                    File outputFile = new File(dzf.getOutputDir(), dzf.getFileName());

                    HttpHelper.downloadFile(dzf.getUrl(), outputFile);
                    FileHelper.unzip(outputFile, dzf.getOutputDir());
                    outputFile.delete();
                }
            } else {

                File outputFile = new File(df.getOutputDir(), df.getFileName());

                if (outputFile.length() != df.getSize()) {
                    HttpHelper.downloadFile(df.getUrl(), outputFile);
                }
            }
        }

        checkFiles();
    }

    private void checkFiles() {

        for (DownloadFile df : downloadFiles) {

            if (df instanceof DownloadZipFile) {

                DownloadZipFile dzf = (DownloadZipFile) df;

                if (FileHelper.getDirectorySize(dzf.getOutputDir()) != dzf.getUnzippedSize()) {
                    startDownloads();
                }
            } else {

                File outputFile = new File(df.getOutputDir(), df.getFileName());

                if (outputFile.length() != df.getSize()) {
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