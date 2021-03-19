package de.melonigemelone.plots.commands.plot.subcommands;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.Plot;
import org.bukkit.entity.Player;

public class TeleportPlotCommand {

    public static boolean handleCommand(Player player, String label, String[] args) {
        if(args.length != 2) {
            player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%","/gs teleport (Name)"));
            return true;
        }

        PlotHandler plotHandler = MelonPlots.getInstance().getPlotHandler();
        Plot plot = plotHandler.getPlotFromName(args[1]);
        if(plot == null) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs konnte kein Grundstück mit diesem Namen gefunden werden!");
            return true;
        }

        if(!plot.hasTeleportPoint()) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDas Grundstück hat kein Teleportpunkt!");
            return true;
        }

        plot.teleportPlayer(player);
        player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu wurdest erfolgreich zu dem Grundstück teleportiert!");
        return true;
    }
}
