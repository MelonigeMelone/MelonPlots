package de.melonigemelone.plots.commands.plot;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.DistrictHandler;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.District;
import de.melonigemelone.plots.model.Plot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeletePlotCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("melonplots.command.deleteplot")) {
                player.sendMessage(GeneralMessages.NO_PERM.getMessage(true));
                return true;
            }

            if(args.length != 1) {
                player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%", "/deletePlot (Name)"));
                return true;
            }
            PlotHandler plotHandler = MelonPlots.getInstance().getPlotHandler();
            String name = args[0];
            if(!plotHandler.existsPlotWithName(name)) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs existiert kein Plot mit diesem Namen!");
                return true;
            }
            Plot plot = plotHandler.getPlotFromName(name);

            plotHandler.deletePlot(plot);
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast erfolgreich den Plot gelöscht!");

        } else {
            sender.sendMessage(GeneralMessages.ONLY_PLAYERS.getMessage(false));
        }
        return false;
    }
}
