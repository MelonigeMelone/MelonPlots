package de.melonigemelone.plots.commands.plot.subcommands;

import de.melonigemelone.api.lib.minecraft.player.MelonPlayer;
import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.Plot;
import org.bukkit.entity.Player;

public class SellBuyPlotCommand {

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

        MelonPlayer melonPlayer = MelonPlayer.fromPlayer(player);
        double money = plot.getCurrentPrice();
        if(!melonPlayer.hasMoney(money)) {
            player.sendMessage(GeneralMessages.NOT_ENOUGH_MONEY.getMessage(true));
            return true;
        }

        melonPlayer.addMoney(money);
        plot.playerBuysPlot(player);
        player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast das Grundstück verkauft!");
        return true;
    }
}
