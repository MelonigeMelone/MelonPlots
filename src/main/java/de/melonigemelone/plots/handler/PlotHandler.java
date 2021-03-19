package de.melonigemelone.plots.handler;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.handler.mysql.MelonPlotsMysql;
import de.melonigemelone.plots.model.District;
import de.melonigemelone.plots.model.Plot;
import de.melonigemelone.plots.model.PlotStatus;
import de.melonigemelone.plots.model.PlotType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class PlotHandler {

    private HashMap<String, Plot> plots = new HashMap<>();
    private String plotPrefix = "mplots_";
    private MelonPlotsMysql melonPlotsMysql;

    public PlotHandler(MelonPlotsMysql melonPlotsMysql) {
        this.melonPlotsMysql = melonPlotsMysql;
        loadPlotsFromDataBase();
    }

    public void loadPlotsFromDataBase() {
        List<Plot> plots = melonPlotsMysql.loadPlots();
        DistrictHandler districtHandler = MelonPlots.getInstance().getDistrictHandler();

        for(Plot plot : plots) {
            String regionId = plot.getRegionId();
            plot.setProtectedRegion(getProtectedRegionFromRegionId(regionId));
            this.plots.put(plot.getRegionName().toLowerCase(), plot);
            districtHandler.getDistrictFromId(plot.getDistrictId()).getPlots().add(plot);
        }
    }

    public Plot getPlotFromName(String name) {
        return plots.get(name.toLowerCase());
    }

    public boolean existsPlotWithName(String name) {
        return plots.containsKey(name.toLowerCase());
    }

    public void createPlot(String name, District district, PlotStatus plotStatus, PlotType plotType, Region region) {
        String regionId = plotPrefix + district.getName().toLowerCase() + "_" + name;
        double districtPrice;
        if(plotStatus.equals(PlotStatus.RENTABLE)) {
            districtPrice = district.getRentPrice();
        } else  {
            districtPrice = district.getBuyPrice();
        }
        double price = region.getLength()*region.getWidth() * districtPrice;
        Plot plot = new Plot(regionId, name,district.getId(), null, plotStatus, plotType, price, -1, -1);
        melonPlotsMysql.createPlot(plot);
        plots.put(name.toLowerCase(), plot);
        plot.createPlot(region);
        district.getPlots().add(plot);
    }

    public void deletePlot(Plot plot) {
        MelonPlots.getInstance().getDistrictHandler().getDistrictFromId(plot.getId()).getPlots().remove(plot);
        plot.delete();
        melonPlotsMysql.deletePlot(plot);
        plots.remove(plot.getRegionName().toLowerCase());
    }

    public ProtectedRegion getProtectedRegionFromRegionId(String id) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World world = new BukkitWorld(Bukkit.getWorld("map"));
        RegionManager regions = container.get(world);
        return regions.getRegion(id);
    }

    public Plot getPlotPlayerStandsOn(Player player) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        World world = new BukkitWorld(player.getWorld());
        RegionManager regions = container.get(world);
        BlockVector3 pt = BlockVector3.at(player.getLocation().getBlockX(),player.getLocation().getBlockY(),player.getLocation().getBlockZ());
        ApplicableRegionSet applicableRegionSet = regions.getApplicableRegions(pt);

        for (ProtectedRegion protectedRegion : applicableRegionSet) {
            String id = protectedRegion.getId();
            if(id.startsWith(plotPrefix)) {
                return getPlotFromRegionID(id);
            }
        }
        return null;
    }

    public Plot getPlotFromRegionID(String id) {
        for(Plot plot : plots.values()) {
            if(plot.getRegionId().equalsIgnoreCase(id)) {
                return plot;
            }
        }
        return null;
    }

    public HashMap<String, Plot> getPlots() {
        return plots;
    }
}
