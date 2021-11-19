package plgs.diltz.chattags;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public Server Server = this.getServer();
    public static Logger Logger = null;
    public static FileConfiguration Config = null;

    @Override
    public void onEnable() {
        // logger

        Logger = this.getLogger();

        // config

        saveDefaultConfig();
        Config = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "/config.yml"));

        // events

        Server.getPluginManager().registerEvents(new OnPlayerChat(), this);

        //

        Logger.info("Plugin loaded!");
    }

    @Override
    public void onDisable() {
        Logger.info("Plugin unloaded!");
    }


}