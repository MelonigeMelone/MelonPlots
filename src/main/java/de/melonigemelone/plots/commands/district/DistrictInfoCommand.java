package de.melonigemelone.plots.commands.district;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.api.system.handler.config.ConfigValue;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.DistrictHandler;
import de.melonigemelone.plots.model.District;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DistrictInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("melonplots.command.districtInfo")) {
                player.sendMessage(GeneralMessages.NO_PERM.getMessage(true));
                return true;
            }

            if(args.length != 1) {
                player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%", "/districtInfo (Name) "));
                return true;
            }
            DistrictHandler districtHandler = MelonPlots.getInstance().getDistrictHandler();
            String name = args[0];
            if(!districtHandler.existsDistrictWithName(name)) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs existiert kein Stadtteil mit diesem Namen!");
                return true;
            }
            District district = districtHandler.getDistrictFromName(name);

            String teleportMessage;
            if(district.hasTeleportLocation()) {
                teleportMessage = "§aGesetzt";
            } else {
                teleportMessage = "§cNicht Gesetzt";
            }

            String info = "\n§7ID: §e" + district.getId() +
                    "\n§7Name: §e" + district.getName() +
                    "\n§7Kaufpreis pro Block: §e" + district.getBuyPrice() + ConfigValue.ECONOMY_SYMBOL.getObject() +
                    "\n§7Mietpreis pro Block: §e" + district.getRentPrice() + ConfigValue.ECONOMY_SYMBOL.getObject() +
                    "\n§7TeleportPunkt:" + teleportMessage;

            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§7Stadtteil Info:" + info);

        } else {
            sender.sendMessage(GeneralMessages.ONLY_PLAYERS.getMessage(false));
        }
        return false;
    }
}
