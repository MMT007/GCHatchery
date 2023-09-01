package mmt007.plugins.gchatchery.Commands.Subcommands;

import mmt007.plugins.gchatchery.Commands.Subcommand;
import mmt007.plugins.gchatchery.MenuMngr.Menu;
import mmt007.plugins.gchatchery.MenuMngr.MenuMngr;
import mmt007.plugins.gchatchery.MenuMngr.Menus.addRecipeMenu;
import mmt007.plugins.gchatchery.MenuMngr.Models.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class addSubCommand extends Subcommand {
    @Override
    public @NotNull String getName() {return "add";}

    @Override
    public @NotNull String getDescription() {return "Adds A Recipe";}

    @Override
    public @NotNull String getSyntax() {return "/hatchery add <Name>";}

    @Override
    public void peform(Player plr, String[] args) {
        PlayerMenuUtility pmu = MenuMngr.getPlayerMenuUtility(plr);
        new addRecipeMenu(pmu).openInventory(args[1]);
    }
}
