package de.melonigemelone.plots.model;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.melonigemelone.api.server.economy.EconomyAPI;
import de.melonigemelone.api.server.economy.model.EconomyData;
import de.melonigemelone.api.server.messages.GeneralMessages;
import de.melonigemelone.api.server.playerdata.PlayerDataAPI;
import de.melonigemelone.api.system.handler.config.ConfigValue;
import de.melonigemelone.plots.MelonPlots;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;


import java.util.Date;
import java.util.UUID;

public class Plot {

    private int id;
    private String regionId;
    private final String regionName;
    private int districtId;
    private String ownerUUID;
    private PlotStatus plotStatus;
    private PlotType plotType;
    private ProtectedRegion protectedRegion;
    private double price;
    private long end;
    private double sellPrice;
    private int taskID;

    public Plot(int id, String regionId, String regionName, int districtId, String ownerUUID, PlotStatus plotStatus, PlotType plotType, double price, long end, double sellPrice) {
        this.id = id;
        this.regionId = regionId;
        this.regionName = regionName;
        this.districtId = districtId;
        this.ownerUUID = ownerUUID;
        this.plotStatus = plotStatus;
        this.plotType = plotType;
        this.price = price;
        this.end = end;
        this.sellPrice = sellPrice;
    }

    public Plot(String regionId, String regionName, int districtId, String ownerUUID, PlotStatus plotStatus, PlotType plotType, double price, long end, double sellPrice) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.districtId = districtId;
        this.ownerUUID = ownerUUID;
        this.plotStatus = plotStatus;
        this.plotType = plotType;
        this.price = price;
        this.end = end;
        this.sellPrice = sellPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public boolean isOwner(String uuid) {
        return this.ownerUUID.equalsIgnoreCase(uuid);
    }

    public boolean hasOwner() {
        return plotStatus.equals(PlotStatus.BUYABLE_HAS_OWNER) || plotStatus.equals(PlotStatus.RENTABLE_HAS_OWNER) || plotStatus.equals(PlotStatus.OWNER_SELLS);
    }

    public void setOwner(String uuid) {
        DefaultDomain ownerDomain = new DefaultDomain();
        ownerDomain.addPlayer(uuid);
        protectedRegion.setOwners(ownerDomain);
        ownerUUID = uuid;
        updatePlotMessage();
        updatePlotInDataBase();
    }

    public PlotStatus getPlotStatus() {
        return plotStatus;
    }

    public PlotType getPlotType() {
        return plotType;
    }

    public ProtectedRegion getProtectedRegion() {
        return protectedRegion;
    }

    public void setProtectedRegion(ProtectedRegion protectedRegion) {
        this.protectedRegion = protectedRegion;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getCurrentPrice() {
        if(plotStatus.equals(PlotStatus.OWNER_SELLS)) {
            return sellPrice;
        } else {
            return price;
        }
    }

    public boolean isBuyableFor(Player player) {
        if(player.getUniqueId().toString().equalsIgnoreCase(ownerUUID)) {
            return false;
        }
        return plotStatus.equals(PlotStatus.BUYABLE) || plotStatus.equals(PlotStatus.OWNER_SELLS);
    }

    public boolean isRentableFor(Player player) {
        if(plotStatus.equals(PlotStatus.BUYABLE_HAS_OWNER) && ownerUUID.equalsIgnoreCase(player.getUniqueId().toString())) {
            return true;
        }
        return plotStatus.equals(PlotStatus.RENTABLE);
    }

    public void createPlot(Region region) {
        World world = region.getWorld();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);
        ProtectedRegion protectedRegion;
        if(region instanceof Polygonal2DRegion){
            protectedRegion = new ProtectedPolygonalRegion(regionId,((Polygonal2DRegion) region).getPoints(),((Polygonal2DRegion) region).getMinimumY(),((Polygonal2DRegion) region).getMaximumY());
        }
        else{
            protectedRegion = new ProtectedCuboidRegion(regionId, ((CuboidRegion) region).getPos1(), ((CuboidRegion) region).getPos2());
        }
        this.protectedRegion = protectedRegion;
        updatePlotMessage();
        protectedRegion.setFlag(Flags.TELE_LOC,new Location(world,region.getCenter()));
        applyDefaultFlags();

        protectedRegion.setPriority(10);
        regions.addRegion(protectedRegion);
    }

    public void applyDefaultFlags() {
        protectedRegion.setFlag(Flags.PVP, StateFlag.State.DENY);
        protectedRegion.setFlag(Flags.LAVA_FLOW, StateFlag.State.DENY);
        protectedRegion.setFlag(Flags.WATER_FLOW, StateFlag.State.DENY);
        protectedRegion.setFlag(Flags.TNT, StateFlag.State.DENY);
        protectedRegion.setFlag(Flags.DENY_MESSAGE,"");
        protectedRegion.setFlag(Flags.USE.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);
    }

    public void updatePlotMessage() {
        String message = "";
        switch (plotStatus) {
            case BUYABLE_HAS_OWNER:
            case RENTABLE_HAS_OWNER:
                message = GeneralMessages.PREFIX.getMessage(false) + "§7Die Region §e" + regionName + " §7gehört §e" + PlayerDataAPI.getPlayerDataFromUUID(UUID.fromString(ownerUUID)).getName();
                break;
            case BUYABLE:
                message = GeneralMessages.PREFIX.getMessage(false) + "§7Die Region §e" + regionName + " §7kann gekauft werden für §e" + price + ConfigValue.ECONOMY_SYMBOL.getObject();
                break;
            case RENTABLE:
                message = GeneralMessages.PREFIX.getMessage(false) + "§7Die Region §e" + regionName + " §7kann gemietet werden für §e" + price + ConfigValue.ECONOMY_SYMBOL.getObject() + " §7/ pro Tag";
                break;
            case OWNER_SELLS:
                message = GeneralMessages.PREFIX.getMessage(false) + "§7Die Region §e" + regionName + " §7kann für §e" + sellPrice + ConfigValue.ECONOMY_SYMBOL.getObject() + " §7von §e" + PlayerDataAPI.getPlayerDataFromUUID(UUID.fromString(ownerUUID)).getName() + " §7gekauft werden";
                break;
        }

        protectedRegion.setFlag(Flags.GREET_MESSAGE, message);
    }

    public void addFriend(UUID uuid) {
        protectedRegion.getMembers().addPlayer(uuid);
    }

    public void removeFriend(UUID uuid) {
        protectedRegion.getMembers().removePlayer(uuid);
    }

    public boolean isFriend(UUID uuid) {
        return protectedRegion.getMembers().contains(uuid);
    }

    public boolean hasTeleportPoint() {
        Location location = protectedRegion.getFlag(Flags.TELE_LOC);
        return location != null;
    }

    public void setTeleportPoint(org.bukkit.Location location) {
        protectedRegion.setFlag(Flags.TELE_LOC,  BukkitAdapter.adapt(location));
    }

    public void teleportPlayer(Player player) {
        Location location = protectedRegion.getFlag(Flags.TELE_LOC);
        if(location == null) {
            return;
        }
        org.bukkit.Location loc = BukkitAdapter.adapt(location);
        player.teleport(loc);
        player.setFallDistance(-100);
    }

    public void resetRegion(boolean flags) {
        protectedRegion.getOwners().clear();
        protectedRegion.getMembers().clear();
        if(flags) {
            applyDefaultFlags();
        }
    }

    public void delete() {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World world = new BukkitWorld(Bukkit.getWorld("map"));
        RegionManager regions = container.get(world);
        regions.removeRegion(regionId, RemovalStrategy.REMOVE_CHILDREN);
    }

    public void updatePlotInDataBase() {
        MelonPlots.getInstance().getMelonPlotsMysql().updatePlot(this);
    }

    public void playerBuysPlot(Player player) {
        if(plotStatus.equals(PlotStatus.OWNER_SELLS)) {
            EconomyData economyData = EconomyAPI.getEconomyDataFromUUID(UUID.fromString(ownerUUID));
            economyData.addMoney(sellPrice);
            resetRegion(false);
        }
        plotStatus = PlotStatus.BUYABLE_HAS_OWNER;
        setOwner(player.getUniqueId().toString());
    }

    public void sellPlot() {
        plotStatus = PlotStatus.BUYABLE;
        resetRegion(true);
    }

    public void playerResellsPlot(double sellPrice) {
        this.sellPrice = sellPrice;
        plotStatus = PlotStatus.OWNER_SELLS;
    }

    public void playerStopsResellPlot() {
        this.sellPrice = -1;
        plotStatus = PlotStatus.BUYABLE_HAS_OWNER;
    }

    public void playerRentsPlot(Player player, int days) {
        if(ownerUUID.equalsIgnoreCase(player.getUniqueId().toString())) {
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast die Miete für das Grundstück erfolgreich verlängert!");
            addTimeToCurrentRent(days);
        } else {
            plotStatus = PlotStatus.RENTABLE_HAS_OWNER;
            player.sendMessage(GeneralMessages.PREFIX.getMessage(false) + "§aDu hast dir das Grundstück erfolgreich gemietet!");
            rent(player, days);
        }
    }

    public void rent(Player player, int days) {
        plotStatus = PlotStatus.BUYABLE_HAS_OWNER;
        setOwner(player.getUniqueId().toString());
    }

    public void unrent() {
        plotStatus = PlotStatus.RENTABLE;
        resetRegion(true);
        stopRentTimer();
    }

    public void addTimeToCurrentRent(int days) {
       stopRentTimer();
       startRentTimer();
    }

    public boolean isRentOver() {
        return System.currentTimeMillis() >= end;
    }

    public void startRentTimer() {
        taskID = Bukkit.getScheduler().runTaskLaterAsynchronously(MelonPlots.getInstance(), new Runnable() {
            @Override
            public void run() {
                rentIsOver();
            }
        }, end-System.currentTimeMillis()).getTaskId();
    }

    public void stopRentTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void rentIsOver() {
        plotStatus = PlotStatus.RENTABLE;
        resetRegion(true);
    }

}
