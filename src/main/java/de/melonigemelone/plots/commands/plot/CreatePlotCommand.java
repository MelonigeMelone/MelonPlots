package de.melonigemelone.plots.commands.plot;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import de.melonigemelone.api.lib.MathsMethods;
import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.DistrictHandler;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreatePlotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("melonplots.command.createPlot")) {
                player.sendMessage(GeneralMessages.NO_PERM.getMessage(true));
                return true;
            }

            if(args.length != 4) {
                player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%", "/createPlot (Name) (Stadtteil) (BUYABLE/RENTABLE) (LIVING / CAMPING / FARMING / APARTMENT / SHOP / CLAN)"));
                return true;
            }
            PlotHandler plotHandler = MelonPlots.getInstance().getPlotHandler();
            String name = args[0];
            if(plotHandler.existsPlotWithName(name)) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs existiert bereits ein Plot mit diesem Namen!");
                return true;
            }

            DistrictHandler districtHandler = MelonPlots.getInstance().getDistrictHandler();
            String districtName = args[1];
            if(!districtHandler.existsDistrictWithName(districtName)) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs existiert kein Stadtteil mit diesem Namen!");
                return true;
            }
            District district = districtHandler.getDistrictFromName(districtName);

            String status = args[2];
            if(!(status.equalsIgnoreCase("BUYABLE") || status.equalsIgnoreCase("RENTABLE"))) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs gibt kein PlotStatus mit diesem Namen!");
                return true;
            }
            PlotStatus plotStatus = PlotStatus.valueOf(status);

            if(PlotType.valueOf(args[3]) == null) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs gibt kein PlotTyp mit diesem Namen!");
                return true;
            }
            PlotType plotType = PlotType.valueOf(args[3]);

            LocalSession localSession = MelonPlots.getInstance().getWorldEditPlugin().getSession(player);
            Region region = null;
            try {
                region = localSession.getSelection(localSession.getSelectionWorld());
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }

            if(region == null) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDu musst erst eine Region auswählen!");
                return true;
            }

            plotHandler.createPlot(name, district, plotStatus, plotType, region);
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast erfolgreich einen Plot erstellt!");
        } else {
            sender.sendMessage(GeneralMessages.ONLY_PLAYERS.getMessage(false));
        }
        return false;
    }
}
