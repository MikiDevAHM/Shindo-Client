package me.miki.shindo;

import me.miki.shindo.events.EventManager;
import me.miki.shindo.features.mods.ModManager;
import me.miki.shindo.features.options.OptionManager;
import me.miki.shindo.features.security.SecurityManager;
import me.miki.shindo.features.settings.SettingManager;
import me.miki.shindo.helpers.CpsHelper;
import me.miki.shindo.helpers.MessageHelper;
import me.miki.shindo.helpers.download.DownloadManager;
import me.miki.shindo.helpers.file.FileManager;
import me.miki.shindo.helpers.font.FontHelper;
import me.miki.shindo.helpers.lang.LanguageManager;
import me.miki.shindo.ui.hudeditor.HudEditor;
import net.minecraft.client.Minecraft;

/**
 * @description Shindo Client Main Class
 * @author MikiDevAHM
 */
public class Shindo {

    // IMPORTANT CONSTANTS HERE
    private static final Shindo INSTANCE = new Shindo();
    private final Minecraft mc =Minecraft.getMinecraft();


    // INFO STUFF HERE
    public static final String NAME =    "Shindo";
    public static final String VERSION = "v1";
    public static final String AUTHOR =  "MikiDevAHM";

    // IMPORTANT VARIABLES AND FIELDS HERE
    private ShindoHandler shindoHandler;
    private FileManager fileManager;
    private DownloadManager downloadManager;
    private LanguageManager languageManager;
    private SettingManager settingManager;
    private ModManager modManager;
    private OptionManager optionManager;
    private HudEditor hudEditor;
    private SecurityManager securityManager;
    private FontHelper fontHelper;
    private CpsHelper cpsHelper;
    private MessageHelper messageHelper;

    // MAIN METHODS
    public void startup() {


        registerEvents(
                fileManager = new FileManager(),
                downloadManager = new DownloadManager(),
                languageManager = new LanguageManager(),
                cpsHelper = new CpsHelper(),
                settingManager = new SettingManager(),
                modManager = new ModManager(),
                optionManager = new OptionManager(),
                hudEditor = new HudEditor(),
                fontHelper = new FontHelper(),
                messageHelper = new MessageHelper(),
                securityManager = new SecurityManager(),
                shindoHandler = new ShindoHandler()
        );

        fontHelper.init();

        EventManager.register(this);
    }

    public void shutdown() {

    }

    // GETTERS
    public static Shindo getInstance() { return INSTANCE; }

    public FileManager getFileManager() { return fileManager; }

    public DownloadManager getDownloadManager() { return downloadManager; }

    public LanguageManager getLanguageManager() { return languageManager; }

    public SettingManager getSettingManager() { return settingManager; }

    public ModManager getModManager() { return modManager; }

    public OptionManager getOptionManager() { return optionManager; }

    public HudEditor getHudEditor() { return hudEditor; }

    public FontHelper getFontHelper() { return fontHelper; }

    public CpsHelper getCpsHelper() { return cpsHelper; }

    public MessageHelper getMessageHelper() { return messageHelper; }

    public SecurityManager getSecurityManager() { return securityManager; }

    public ShindoHandler getShindoHandler() { return shindoHandler; }


    private void registerEvents(Object... events) {
        for (Object event : events) {
            EventManager.register(event);
        }
    }







}