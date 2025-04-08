package me.miki.shindo.helpers.file.filter;

import me.miki.shindo.helpers.file.FileHelper;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class SoundFileFilter extends FileFilter {
	
    @Override
    public boolean accept(File file) {
    	
        if (file.isDirectory()) {
            return true;
        }

        String extension = FileHelper.getExtension(file);
        
        if (extension != null && extension.equalsIgnoreCase("wav")) {
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Sounds (*.wav)";
    }
}
