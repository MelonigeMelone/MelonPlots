package de.melonigemelone.plots.commands.district;

import de.melonigemelone.api.lib.MathsMethods;
import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.DistrictHandler;
import de.melonigemelone.plots.model.District;
import de.melonigemelone.plots.model.DistrictType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateDistrictCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("melonplots.command.createdistrict")) {
                player.sendMessage(GeneralMessages.NO_PERM.getMessage(true));
                return true;
            }

            if(args.length != 4) {
                player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%", "/createDistrict (Name) (NORMAL,HOTEL) (Kaufpreis) (Mietpreis)"));
                return true;
            }
            DistrictHandler districtHandler = MelonPlots.getInstance().getDistrictHandler();
            String name = args[0];
            if(districtHandler.existsDistrictWithName(name)) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs existiert bereits ein Stadtteil mit diesem Namen!");
                return true;
            }
            String type = args[1];
            if(!(type.equalsIgnoreCase("NORMAL") || type.equalsIgnoreCase("HOTEL"))) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs gibt kein Typ mit diesem Namen!");
                return true;
            }
            DistrictType districtType = DistrictType.valueOf(type);
            if(!(MathsMethods.isDouble(args[2]) && MathsMethods.isDouble(args[3]))) {
                player.sendMessage(GeneralMessages.WRONG_VALUE.getMessage(true));
                return true;
            }
            double buyPrice = Double.parseDouble(args[2]);
            double rentPrice = Double.parseDouble(args[3]);
            District district = new District(name, districtType, buyPrice, rentPrice, null);
            districtHandler.createDistrict(district);
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast erfolgreich den Stadtteil erstellt!");

        } else {
            sender.sendMessage(GeneralMessages.ONLY_PLAYERS.getMessage(false));
        }
        return false;
    }
}
