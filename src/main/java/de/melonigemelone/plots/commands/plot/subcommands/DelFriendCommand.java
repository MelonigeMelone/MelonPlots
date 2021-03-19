package de.melonigemelone.plots.commands.plot.subcommands;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.api.server.playerdata.PlayerDataAPI;
import de.melonigemelone.api.server.playerdata.model.PlayerData;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.model.Plot;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DelFriendCommand {

    public static boolean handleCommand(Player player, String label, String[] args) {
        if(args.length != 2) {
            player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%","/gs delFriend (Name)"));
            return true;
        }

        PlotHandler plotHandler = MelonPlots.getInstance().getPlotHandler();
        Plot plot = plotHandler.getPlotPlayerStandsOn(player);
        if(plot == null) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDu musst auf einem Grundstück stehen!");
            return true;
        }

        if(!plot.isOwner(player.getUniqueId().toString())) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDieses Grundstück gehört dir nicht!");
            return true;
        }

        String targetName = args[1];
        PlayerData playerData = PlayerDataAPI.getPlayerDataFromName(targetName);
        if(playerData == null) {
            player.sendMessage(GeneralMessages.NO_PLAYER_FOUND.getMessage(true));
            return true;
        }

        UUID targetUUID = player.getUniqueId();
        if(!plot.isFriend(targetUUID)) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDer Spieler ist kein Freund auf deinem Grunstück!");
            return true;
        }

        plot.removeFriend(targetUUID);
        player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast den Spieler erfolgreich als Freund entfernt!");
        return true;
    }
}
