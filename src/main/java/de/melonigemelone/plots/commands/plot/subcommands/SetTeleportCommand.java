package de.melonigemelone.plots.commands.plot.subcommands;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.Plot;
import org.bukkit.entity.Player;

public class SetTeleportCommand {

    public static boolean handleCommand(Player player, String label, String[] args) {
        PlotHandler plotHandler = MelonPlots.getInstance().getPlotHandler();
        Plot plot = plotHandler.getPlotPlayerStandsOn(player);
        if(plot == null) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDu musst auf einem Grundstück stehen!");
            return true;
        }

        if(!plot.isOwner(player.getUniqueId().toString())) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDir gehört dieses Grundstück nicht!");
            return true;
        }


        plot.setTeleportPoint(player.getLocation());
        player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast den Teleportpunkt von dem Grundstück erfolgreich gesetzt!");
        return true;
    }
}
