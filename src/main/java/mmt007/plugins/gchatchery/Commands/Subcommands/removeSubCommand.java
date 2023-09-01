package mmt007.plugins.gchatchery.Commands.Subcommands;

import mmt007.plugins.gchatchery.Commands.Subcommand;
import mmt007.plugins.gchatchery.MenuMngr.MenuMngr;
import mmt007.plugins.gchatchery.MenuMngr.Menus.addRecipeMenu;
import mmt007.plugins.gchatchery.MenuMngr.Models.PlayerMenuUtility;
import mmt007.plugins.gchatchery.RecipeMngr.Models.Recipe;
import mmt007.plugins.gchatchery.RecipeMngr.RecipeMngr;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class removeSubCommand extends Subcommand {
    @Override
    public @NotNull String getName() {return "remove";}

    @Override
    public @NotNull String getDescription() {return "Removes A Recipe";}

    @Override
    public @NotNull String getSyntax() {return "/hatchery remove <Name>";}

    @Override
    public void peform(Player plr, String[] args) {
        if(RecipeMngr.removeRecipe(args[1])){
            plr.sendMessage("§aRecipe Removed!");
        }else{
            plr.sendMessage("§cCould Not Remove Recipe");
        }
    }
}
