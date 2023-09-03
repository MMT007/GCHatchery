package mmt007.plugins.gchatchery.Commands;

import mmt007.plugins.gchatchery.Commands.Subcommands.addSubCommand;
import mmt007.plugins.gchatchery.Commands.Subcommands.giveSubCommand;
import mmt007.plugins.gchatchery.Commands.Subcommands.removeSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {
    ArrayList<Subcommand> subcommands = new ArrayList<>();

    //Registers SubCommands
    public CommandManager(){
        subcommands.add(new addSubCommand());
        subcommands.add(new removeSubCommand());
        subcommands.add(new giveSubCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player plr) {
            //Checks For Permission
            if(!plr.hasPermission("mmt007.plugins.GCHatchery.Use")) {return false;}
            for (Subcommand subc : subcommands) {
                //Gets Command With Same Name
                if (args[0].equalsIgnoreCase(subc.getName())) {
                    subc.peform(plr, args);
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> commands = new ArrayList<>();

        if(sender instanceof Player) {
            if (args.length == 1) {
                for (Subcommand subc : subcommands) {
                    commands.add(subc.getName());
                }
            }
            if(args.length == 2 && args[1].equalsIgnoreCase("give")){
                for(Player plr : Bukkit.getOnlinePlayers()){
                    commands.add(plr.getName());
                }
            }
        }

        return commands;
    }
}
