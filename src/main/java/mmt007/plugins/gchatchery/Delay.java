package mmt007.plugins.gchatchery;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.nio.Buffer;

public class Delay implements Listener {
    private static Plugin main = GCHatchery.getPlugin();
    private int id = -1;

    public Delay(Runnable runnable){
        this(runnable,0);
    }
    public Delay(Runnable runnable, long delay){
        if(main.isEnabled()){
            id = Bukkit.getScheduler().scheduleSyncDelayedTask(main,runnable,delay);
        }else{
            runnable.run();
        }
    }
}
