package me.miki.shindo;

import me.miki.shindo.events.EventManager;
import me.miki.shindo.features.account.AccountManager;
import me.miki.shindo.features.chat.ChatManager;
import me.miki.shindo.features.download.DownloadManager;
import me.miki.shindo.features.mods.ModManager;
import me.miki.shindo.features.music.MusicManager;
import me.miki.shindo.features.notification.NotificationManager;
import me.miki.shindo.features.options.OptionManager;
import me.miki.shindo.features.patcher.PatcherManager;
import me.miki.shindo.features.profile.ProfileManager;
import me.miki.shindo.features.security.SecurityManager;
import me.miki.shindo.features.settings.SettingManager;
import me.miki.shindo.helpers.CpsHelper;
import me.miki.shindo.helpers.MessageHelper;
import me.miki.shindo.helpers.file.FileManager;
import me.miki.shindo.helpers.font.FontHelper;
import me.miki.shindo.ui.hudeditor.HudEditor;
import net.minecraft.client.Minecraft;

/**
 * @author MikiDevAHM
 * @description Shindo Client Main Class
 */
public class Shindo {

    // INFO STUFF HERE
    public static final String NAME     =      "Shindo";
    public static final String VERSION  =      "v1.0.3";
    public static final String AUTHOR   =  "MikiDevAHM";

    // IMPORTANT CONSTANTS HERE
    private static final Shindo INSTANCE = new Shindo();
    private final Minecraft mc = Minecraft.getMinecraft();

    // IMPORTANT VARIABLES AND FIELDS HERE
    private ShindoHandler shindoHandler;
    private ShindoAPI shindoAPI;
    private AccountManager accountManager;

    private FileManager fileManager;
    private DownloadManager downloadManager;

    private ModManager modManager;
    private SettingManager settingManager;
    private OptionManager optionManager;
    private ChatManager chatManager;
    private PatcherManager patcherManager;
    private MusicManager musicManager;
    private ProfileManager profileManager;
    private NotificationManager notificationManager;
    private SecurityManager securityManager;

    private HudEditor hudEditor;

    private FontHelper fontHelper;
    private CpsHelper cpsHelper;
    private MessageHelper messageHelper;


    public static Shindo getInstance() {
        return INSTANCE;
    }

    // MAIN METHODS
    public void startup() {


        registerEvents(
                fileManager = new FileManager(),
                accountManager = new AccountManager(),
                downloadManager = new DownloadManager(),
                cpsHelper = new CpsHelper(),
                settingManager = new SettingManager(),
                modManager = new ModManager(),
                optionManager = new OptionManager(),
                hudEditor = new HudEditor(),
                fontHelper = new FontHelper(),
                messageHelper = new MessageHelper(),
                securityManager = new SecurityManager(),
                chatManager = new ChatManager(),
                patcherManager = new PatcherManager(),
                musicManager = new MusicManager(),
                profileManager = new ProfileManager(),
                notificationManager = new NotificationManager(),

                shindoHandler = new ShindoHandler(),
                shindoAPI = new ShindoAPI()

        );

        fontHelper.init();
        shindoHandler.init();
        shindoAPI.init();

        modManager.getMod("GlobalSettings").setToggled(true);

        EventManager.register(this);

        mc.updateDisplay();
    }

    public void shutdown() {
        profileManager.save();
        accountManager.save();
        EventManager.unregister(this);
    }

    // GETTERS
    public ShindoAPI getShindoAPI() {
        return shindoAPI;
    }

    public ShindoHandler getShindoHandler() {
        return shindoHandler;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public ModManager getModManager() {
        return modManager;
    }

    public SettingManager getSettingManager() {
        return settingManager;
    }

    public OptionManager getOptionManager() {
        return optionManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public PatcherManager getPatcherManager() {
        return patcherManager;
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public FontHelper getFontHelper() {
        return fontHelper;
    }

    public CpsHelper getCpsHelper() {
        return cpsHelper;
    }

    public MessageHelper getMessageHelper() {
        return messageHelper;
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