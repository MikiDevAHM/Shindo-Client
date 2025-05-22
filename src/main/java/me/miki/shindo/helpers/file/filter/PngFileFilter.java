package me.miki.shindo.helpers.file.filter;

import me.miki.shindo.helpers.file.FileHelper;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class PngFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {

        if (file.isDirectory()) {
            return true;
        }

        String extension = FileHelper.getExtension(file);

        return extension != null && extension.equalsIgnoreCase("png");
    }

    @Override
    public String getDescription() {
        return "Png Images (*.png)";
    }
}
