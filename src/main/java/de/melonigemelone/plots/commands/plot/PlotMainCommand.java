package de.melonigemelone.plots.commands.plot;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.commands.plot.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlotMainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length >= 1) {
                switch (args[0]) {
                    case "buy":
                    case "kaufen":
                        return BuyPlotCommand.handleCommand(player, label, args);
                    case "rent":
                    case "mieten":
                        return RentPlotCommand.handleCommand(player, label, args);
                    case "info":
                    case "addFriend":
                        return AddFriendCommand.handleCommand(player, label, args);
                    case "delFriend":
                        return DelFriendCommand.handleCommand(player, label, args);
                    case "teleport":
                    case "tp":
                        return TeleportPlotCommand.handleCommand(player, label, args);
                    case "setteleport":
                    case "settp":
                        return SetTeleportCommand.handleCommand(player, label, args);
                    default:
                        return handleHelp(player);
                }
            } else {

            }

        } else {
            sender.sendMessage(GeneralMessages.ONLY_PLAYERS.getMessage(false));
        }
        return false;
    }

    public boolean handleHelp(Player player) {
        String helpMessage = "§8§m     §8[ §eGrundstück - Hilfe §8]§m     " +
                "\n§e/gs kaufen §8- §7Kaufe dir das Grundstück auf dem du stehst" +
                "\n§e/gs mieten §8- §7Miete dir das Grundstück auf dem du stehst" +
                "\n§e/gs find §8- §7Finde ein freies Grundstück" +
                "\n§e/gs info (Name) §8- §7Sieh dir Informationen über ein Grundstück an" +
                "\n§e/gs info (Name) §8- §7Sieh dir Informationen über ein Grundstück an" +
                "\n§e/gs addFriend (Name) §8- §7Füge einen Freund zu deinem Grundstück hinzu" +
                "\n§e/gs delFriend (Name) §8- §7Entferne einen Freund von deinem Grundstück" +
                "\n§e/gs setTeleport §8- §7Setzte den Teleportpunkt von deinem Grundstück" +
                "\n§e/gs teleport (Name) §8- §7Teleportiere dich zu einem Grundstück";
        player.sendMessage(helpMessage);
        return true;
    }
}
