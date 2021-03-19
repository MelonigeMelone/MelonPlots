package de.melonigemelone.plots.commands.plot.subcommands;

import de.melonigemelone.api.lib.MathsMethods;
import de.melonigemelone.api.lib.minecraft.player.MelonPlayer;
import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.Plot;
import org.bukkit.entity.Player;

public class RentPlotCommand {

    public static boolean handleCommand(Player player, String label, String[] args) {
        if(args.length != 2) {
            player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%", "/gs mieten (Tage)"));
            return true;
        }

        PlotHandler plotHandler = MelonPlots.getInstance().getPlotHandler();
        Plot plot = plotHandler.getPlotPlayerStandsOn(player);
        if(plot == null) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDu musst auf einem Grundstück stehen!");
            return true;
        }

        if(!plot.isRentableFor(player)) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDieses Grundstück kann nicht gemietet werden!");
            return true;
        }

        if(!MathsMethods.isInteger(args[1])) {
            player.sendMessage(GeneralMessages.WRONG_VALUE.getMessage(true));
            return true;
        }
        int days = Integer.parseInt(args[1]);

        MelonPlayer melonPlayer = MelonPlayer.fromPlayer(player);
        double money = plot.getCurrentPrice()* days;
        if(!melonPlayer.hasMoney(money)) {
            player.sendMessage(GeneralMessages.NOT_ENOUGH_MONEY.getMessage(true));
            return true;
        }

        melonPlayer.removeMoney(money);
        plot.playerRentsPlot(player, days);
        return true;
    }
}
