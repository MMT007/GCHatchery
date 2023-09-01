package mmt007.plugins.gchatchery.Commands;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Subcommand {
    @NotNull
    public abstract String getName();
    @NotNull
    public abstract String getDescription();
    @NotNull
    public abstract String getSyntax();
    public abstract void peform(Player plr, String[] args);
}
