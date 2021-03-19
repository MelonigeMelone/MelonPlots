package de.melonigemelone.plots.commands.district;

import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.DistrictHandler;
import de.melonigemelone.plots.model.District;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DistrictTelportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("melonplots.command.districtTeleport")) {
                player.sendMessage(GeneralMessages.NO_PERM.getMessage(true));
                return true;
            }

            if(args.length != 1) {
                player.sendMessage(GeneralMessages.COMMAND_USAGE.getMessage(true).replaceAll("%command%", "/districtTeleport (Name) "));
                return true;
            }
            DistrictHandler districtHandler = MelonPlots.getInstance().getDistrictHandler();
            String name = args[0];
            if(!districtHandler.existsDistrictWithName(name)) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cEs existiert kein Stadtteil mit diesem Namen!");
                return true;
            }
            District district = districtHandler.getDistrictFromName(name);
            if(!district.hasTeleportLocation()) {
                player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§cDer Stadtteil hat keine TeleportLocation!");
                return true;
            }
            player.teleport(district.getTeleportLocation());
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu wurdest zum dem Stadtteil teleportiert!");

        } else {
            sender.sendMessage(GeneralMessages.ONLY_PLAYERS.getMessage(false));
        }
        return false;
    }
}
