package de.melonigemelone.plots.commands.plot.subcommands;

import de.melonigemelone.api.lib.minecraft.player.MelonPlayer;
import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.Plot;
import org.bukkit.entity.Player;

public class BuyPlotCommand {

    public static boolean handleCommand(Player player, String label, String[] args) {
        PlotHandler plotHandler = MelonPlots.getInstance().getPlotHandler();
        Plot plot = plotHandler.getPlotPlayerStandsOn(player);
        if(plot == null) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDu musst auf einem Grundstück stehen!");
            return true;
        }

        if(!plot.isBuyableFor(player)) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDu kannst dieses Grundstück nicht kaufen!");
            return true;
        }

        MelonPlayer melonPlayer = MelonPlayer.fromPlayer(player);
        double money = plot.getCurrentPrice();
        if(!melonPlayer.hasMoney(money)) {
            player.sendMessage(GeneralMessages.NOT_ENOUGH_MONEY.getMessage(true));
            return true;
        }

        melonPlayer.removeMoney(money);
        plot.playerBuysPlot(player);
        player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast dir das Grundstück erfolgreich gekauft!");
        return true;
    }
}
