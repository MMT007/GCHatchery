package mmt007.plugins.gchatchery;

import mmt007.plugins.gchatchery.Commands.CommandManager;
import mmt007.plugins.gchatchery.MenuMngr.MenuMngr;
import mmt007.plugins.gchatchery.RecipeMngr.RecipeMngr;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public final class GCHatchery extends JavaPlugin {
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        RecipeMngr.loadRecipes();

        getServer().getPluginManager().registerEvents(new EventMngr(),this);
        getServer().getPluginManager().registerEvents(new MenuMngr(), this);

        Objects.requireNonNull(getCommand("hatchery")).setExecutor(new CommandManager());

        checkConfigFile(this);
    }

    @Override
    public void onDisable() {

    }

    public static Plugin getPlugin() {return plugin;}


    //Checks If Config File Is Present
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void checkConfigFile(Plugin main){
        File configFile = new File(main.getDataFolder(),"config.yml");

        if(!configFile.exists()){
            try {
                configFile.getParentFile().mkdir();
                configFile.createNewFile();

                InputStream is = main.getResource("config.yml");
                FileConfiguration fConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(is)
                );

                fConfig.save(configFile);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
