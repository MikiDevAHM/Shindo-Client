package me.miki.shindo;

import me.miki.shindo.events.EventManager;

import me.miki.shindo.features.color.ColorManager;
import me.miki.shindo.features.download.DownloadManager;
import me.miki.shindo.features.file.FileManager;
import me.miki.shindo.features.mods.ModManager;
import me.miki.shindo.features.music.MusicManager;
import me.miki.shindo.features.nanovg.NanoVGManager;
import me.miki.shindo.features.notification.NotificationManager;
import me.miki.shindo.features.options.OptionManager;
import me.miki.shindo.features.profile.ProfileManager;
import me.miki.shindo.features.screenshot.ScreenshotManager;
import me.miki.shindo.features.security.SecurityManager;
import me.miki.shindo.features.settings.SettingManager;
import me.miki.shindo.helpers.CpsHelper;
import me.miki.shindo.helpers.OptifineHelper;
import me.miki.shindo.ui.hudeditor.HudEditor;
import net.minecraft.client.Minecraft;

/**
 * @author MikiDevAHM
 * @description Shindo Client Main Class
 */
public class Shindo {

    // INFO STUFF HERE
    public static final String NAME     =      "Shindo";
    public static final String VERSION  =      "v1.0.4";
    public static final String AUTHOR   =  "MikiDevAHM";

    // IMPORTANT CONSTANTS HERE
    private static final Shindo INSTANCE = new Shindo();
    private final Minecraft mc = Minecraft.getMinecraft();

    // IMPORTANT VARIABLES AND FIELDS HERE
    private ShindoHandler shindoHandler;
    private ShindoAPI shindoAPI;

    private NanoVGManager nanoVGManager;
    private FileManager fileManager;
    private DownloadManager downloadManager;
    private ModManager modManager;
    private SettingManager settingManager;
    private OptionManager optionManager;
    private MusicManager musicManager;
    private ProfileManager profileManager;
    private NotificationManager notificationManager;
    private SecurityManager securityManager;
    private ColorManager colorManager;
    private ScreenshotManager screenshotManager;
    private CpsHelper cpsHelper;

    private HudEditor hudEditor;




    public static Shindo getInstance() {
        return INSTANCE;
    }

    // MAIN METHODS
    public void startup() {

        OptifineHelper.disableFastRender();
        OptifineHelper.removeOptifineZoom();
        registerEvents(
                fileManager = new FileManager(),
                downloadManager = new DownloadManager(),
                cpsHelper = new CpsHelper(),
                settingManager = new SettingManager(),
                modManager = new ModManager(),
                optionManager = new OptionManager(),
                hudEditor = new HudEditor(),
                colorManager = new ColorManager(),
                profileManager = new ProfileManager(),
                musicManager = new MusicManager(),
                notificationManager = new NotificationManager(),
                securityManager = new SecurityManager(),
                screenshotManager = new ScreenshotManager(),
                shindoHandler = new ShindoHandler(),
                shindoAPI = new ShindoAPI()
        );

        shindoHandler.init();
        shindoAPI.init();

        modManager.getMod("GlobalSettings").setToggled(true);

        mc.updateDisplay();
        EventManager.register(this);
    }

    public void shutdown() {
        profileManager.save();
        EventManager.unregister(this);
    }

    // GETTERS
    public ShindoAPI getShindoAPI() {
        return shindoAPI;
    }

    public ShindoHandler getShindoHandler() {
        return shindoHandler;
    }

    public NanoVGManager getNanoVGManager() {
        return nanoVGManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }


    public SettingManager getSettingManager() {
        return settingManager;
    }

    public ModManager getModManager() {
        return modManager;
    }

    public OptionManager getOptionManager() {
        return optionManager;
    }

    public ColorManager getColorManager() {
        return colorManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public ScreenshotManager getScreenshotManager() {
        return screenshotManager;
    }

    public CpsHelper getCpsHelper() {
        return cpsHelper;
    }

    public HudEditor getHudEditor() {
        return hudEditor;
    }


    private void registerEvents(Object... events) {
        for (Object event : events) {
            EventManager.register(event);
        }
    }
}