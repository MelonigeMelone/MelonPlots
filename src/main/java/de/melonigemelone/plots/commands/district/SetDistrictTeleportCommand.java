package de.melonigemelone.plots.commands.district;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.DistrictHandler;
import de.melonigemelone.plots.model.District;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDistrictTeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("melonplots.command.setDistrictTeleport")) {
                player.sendMessage(GeneralMessages.NO_PERM.getMessage(true));
                return true;
            }

            if(args.length != 1) {
                player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%", "/setDistrictTeleport (Name) "));
                return true;
            }
            DistrictHandler districtHandler = MelonPlots.getInstance().getDistrictHandler();
            String name = args[0];
            if(!districtHandler.existsDistrictWithName(name)) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs existiert kein Stadtteil mit diesem Namen!");
                return true;
            }
            District district = districtHandler.getDistrictFromName(name);
            district.setTeleportLocation(player.getLocation());
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast erfolgreich die Teleport Location von dem Stadtteil gesetzt!");

        } else {
            sender.sendMessage(GeneralMessages.ONLY_PLAYERS.getMessage(false));
        }
        return false;
    }
}
