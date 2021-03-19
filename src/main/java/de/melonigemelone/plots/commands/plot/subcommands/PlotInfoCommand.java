package de.melonigemelone.plots.commands.plot.subcommands;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.api.server.playerdata.PlayerDataAPI;
import de.melonigemelone.api.server.playerdata.model.PlayerData;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.Plot;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlotInfoCommand {

    public static boolean handleCommand(Player player, String label, String[] args) {
        PlotHandler plotHandler = MelonPlots.getInstance().getPlotHandler();
        if(args.length == 1) {
            Plot plot = plotHandler.getPlotPlayerStandsOn(player);
            if(plot == null) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDu musst auf einem Grundstück stehen!");
                return true;
            }

            //Open Inventory
            return true;


        } else if(args.length == 2) {
            Plot plot = plotHandler.getPlotFromName(args[1]);
            if(plot == null) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs konnte kein Grundstück mit diesem Namen gefunden werden!");
                return true;
            }

            //Open Inventory
            return true;

        } else {
            player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%", "/gs info (Name)"));
            return true;
        }


    }
}
