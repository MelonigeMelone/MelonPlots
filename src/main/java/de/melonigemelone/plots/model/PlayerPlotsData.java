package de.melonigemelone.plots.model;

import java.util.List;
import java.util.UUID;

public class PlayerPlotsData {

    private UUID uuid;
    private List<Plot> ownPlots;
    private List<Plot> friendPlots;

    public PlayerPlotsData(UUID uuid, List<Plot> ownPlots, List<Plot> friendPlots) {
        this.uuid = uuid;
        this.ownPlots = ownPlots;
        this.friendPlots = friendPlots;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Plot> getOwnPlots() {
        return ownPlots;
    }

    public void setOwnPlots(List<Plot> ownPlots) {
        this.ownPlots = ownPlots;
    }

    public List<Plot> getFriendPlots() {
        return friendPlots;
    }

    public void setFriendPlots(List<Plot> friendPlots) {
        this.friendPlots = friendPlots;
    }
}
