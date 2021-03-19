package de.melonigemelone.plots.handler;

import de.melonigemelone.plots.model.PlayerPlotsData;
import de.melonigemelone.plots.model.Plot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerPlotsDataHandler {

    private final PlotHandler plotHandler;
    private HashMap<UUID, PlayerPlotsData> playerPlotsDataHashMap = new HashMap<>();


    public PlayerPlotsDataHandler(PlotHandler plotHandler) {
        this.plotHandler = plotHandler;
    }

    public void loadPlayerData(UUID uuid) {
        List<Plot> ownPlotsList = new ArrayList<>();
        List<Plot> friendPlotsList = new ArrayList<>();
        for(Plot plot : plotHandler.getPlots().values()) {
            if(plot.hasOwner()) {
                if(plot.getOwnerUUID().equals(uuid)) {
                    ownPlotsList.add(plot);
                } else if(plot.isFriend(uuid)) {
                    friendPlotsList.add(plot);
                }
            }
        }
        playerPlotsDataHashMap.put(uuid, new PlayerPlotsData(uuid, ownPlotsList, friendPlotsList));
    }

    public PlayerPlotsData getPlayerData(UUID uuid) {
        return playerPlotsDataHashMap.get(uuid);
    }

    public PlayerPlotsData removePlayerData(UUID uuid) {
        return playerPlotsDataHashMap.remove(uuid);
    }
}
