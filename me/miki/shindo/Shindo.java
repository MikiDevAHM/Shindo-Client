package me.miki.shindo;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import me.miki.shindo.api.config.Config;
import me.miki.shindo.api.config.ConfigLoader;
import me.miki.shindo.api.config.ConfigSaver;
import me.miki.shindo.api.logger.EcoLogManager;
import me.miki.shindo.api.logger.enums.LoggerCasualEnum;
import me.miki.shindo.api.logger.enums.LoggerWarningEnum;
import me.miki.shindo.event.EventManager;
import me.miki.shindo.event.EventTarget;
import me.miki.shindo.event.impl.KeyEvent;
import me.miki.shindo.feature.mod.ModManager;
import me.miki.shindo.feature.option.OptionManager;
import me.miki.shindo.feature.setting.SettingManager;
import me.miki.shindo.ui.hudeditor.HudEditor;
import me.miki.shindo.ui.splash.SplashProgress;
import me.miki.shindo.util.CpsHelper;
import me.miki.shindo.util.MessageHelper;
import me.miki.shindo.util.ScreenHelper;
import me.miki.shindo.util.font.FontHelper;
import net.minecraft.client.Minecraft;

public class Shindo {
	
	private static final EcoLogManager logger = EcoLogManager.getLogger();
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static Shindo INSTANCE = new Shindo();
	
	private  DiscordIPC ipc = new DiscordIPC();

	public static final String NAME = "Shindo";
	public static final String VERSION = "v3";
	public static final String AUTHOR = "MikiDevAHM";

	
	public Config config;
	
	public ModManager mods;
	public EventManager manager;
	public OptionManager options;
	public SettingManager settings;
	
	public HudEditor huds;
	
	public FontHelper font;
	public CpsHelper cps;
	public MessageHelper msg;
	
	public ScreenHelper sh;
	
	public Shindo() {
		manager = new EventManager();
	}

	public void init() {
		logger.clientName = "Shindo Client";
		logger.infoColour1 = "Cyan";
		logger.infoColour2 = "Blue";
		logger.mildWarningColour = "Yellow";
		logger.importantWarningColour = "Red";
		logger.log(LoggerCasualEnum.ProjectInfo, "Iniciando ...");
		
		
		
		SplashProgress.setProgress(0, "[Shindo] Iniciando Discord RPC");
		
		logger.log(LoggerCasualEnum.ProjectInfo, "Iniciando DiscordIPC ...");
		ipc.start();
		logger.log(LoggerCasualEnum.ProjectInfo, "DiscordIPC Carregado com Sucesso! ...");
		

	}

	public void startup() {
		SplashProgress.setProgress(7, "[Shindo] Iniciando...");
		
		Display.setTitle(NAME + " " + VERSION + " | by " + AUTHOR);
		
		SplashProgress.setProgress(8, "[Shindo] Registrando Eventos...");
		
		logger.log(LoggerCasualEnum.ProjectInfo, "Registrando Eventos ...");
		registerEvents
		(

				cps = new CpsHelper(),
				settings = new SettingManager(),
				mods = new ModManager(),
				options = new OptionManager(),
				huds = new HudEditor(),
				font = new FontHelper(),
				msg = new MessageHelper(),
				sh = new ScreenHelper()
				
		);
		
		logger.log(LoggerCasualEnum.ProjectInfo, "Eventos Carregados com Sucesso! ...");
		
		SplashProgress.setProgress(9, "[Shindo] Checando Configurações...");
		
		logger.log(LoggerCasualEnum.ProjectInfo, "Checando Configurações ...");

		try {
            if (!ConfigSaver.configExists()) {
            	logger.logError(LoggerWarningEnum.ProjectWarning, "Configurações não Encontradas! Criando ...");
                ConfigSaver.saveConfig();
            }
            SplashProgress.setProgress(10, "[Shindo] Carregando Configurações...");
            
            logger.log(LoggerCasualEnum.ProjectInfo, "Configurações  Encontradas! Carregando ...");
            ConfigLoader.loadConfig();
            logger.log(LoggerCasualEnum.ProjectInfo, "Configurações Carregadas com Sucesso! ...");
        } catch (Exception e) {
        	logger.logError(LoggerWarningEnum.ProjectError, "Erro ao Carregar Configurações ... " + e.getMessage());
        }
		SplashProgress.setProgress(11, "[Shindo] Carregando Font Manager...");
		
		logger.log(LoggerCasualEnum.ProjectInfo, "Iniciando Font Manager ...");
		font.init();
		logger.log(LoggerCasualEnum.ProjectInfo, "Font Manager Iniciado com Sucesso! ...");
		
		EventManager.register(this);
	}

	public void shutdown() {
		
		logger.log(LoggerCasualEnum.ProjectInfo, "Desligando DiscordIPC ...");
		ipc.shutdown();
		
		logger.log(LoggerCasualEnum.ProjectInfo, "Salvando Configurações ...");
		 try {
             ConfigSaver.saveConfig();
         } catch (Exception e) {
        	 logger.logError(LoggerWarningEnum.ProjectError, "Erro ao Salvar Configurações ... " + e.getMessage());
         }
		 
		 
		EventManager.unregister(this);
	}

	public static Shindo getInstance() { return INSTANCE; }
	
	
	public EventManager getEventManager() { return manager; }
	
	public ModManager getModManager() { return mods; }
	
	public OptionManager getOptionManager() { return options; }
	
	public SettingManager getSettingsManager() { return settings; }
	
	public MessageHelper getMessageHelper() { return msg; }
	
	public FontHelper getFontHelper() { return font; }
	
	public HudEditor getHudEditor() { return huds; }
	
	public CpsHelper getCpsHelper() { return cps; } 
	
	public DiscordIPC getIpc() { return ipc; }
	
	public Config getConfig() { return config; }
	
	
    @EventTarget
    public void onKey(KeyEvent e) {
        if (Keyboard.isKeyDown(Shindo.getInstance().getOptionManager().getOptionByName("ModMenu Keybinding").getKey())) {
            mc.displayGuiScreen(huds);
        }
    }
    
	
    private void registerEvents(Object... events) {
        for (Object event : events) {
            EventManager.register(event);
        }
    }

}
