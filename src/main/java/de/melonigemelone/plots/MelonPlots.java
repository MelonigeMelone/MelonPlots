package de.melonigemelone.plots;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.melonigemelone.api.lib.mysql.DatabaseConnector;
import de.melonigemelone.api.lib.mysql.MysqlConfig;
import de.melonigemelone.api.lib.mysql.model.MysqlData;
import de.melonigemelone.plots.commands.district.*;
import de.melonigemelone.plots.commands.plot.CreatePlotCommand;
import de.melonigemelone.plots.commands.plot.DeletePlotCommand;
import de.melonigemelone.plots.commands.plot.PlotMainCommand;
import de.melonigemelone.plots.handler.DistrictHandler;
import de.melonigemelone.plots.handler.PlotHandler;
import de.melonigemelone.plots.handler.mysql.MelonPlotsMysql;
import org.bukkit.plugin.java.JavaPlugin;

public class MelonPlots extends JavaPlugin {

    private static MelonPlots instance;
    private WorldEditPlugin worldEditPlugin ;
    private WorldGuardPlugin worldGuardPlugin;

    private MelonPlotsMysql melonPlotsMysql;

    private DistrictHandler districtHandler;
    private PlotHandler plotHandler;

    @Override
    public void onEnable() {
        instance = this;
        DatabaseConnector.getInstance().connect(this.getClass(),new MysqlData(new MysqlConfig(getDataFolder().toString())));

        worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");;
        worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");;

        melonPlotsMysql = new MelonPlotsMysql(this);

        districtHandler = new DistrictHandler(melonPlotsMysql);
        plotHandler = new PlotHandler(melonPlotsMysql);

        initCommands();
        initListener();
    }

    @Override
    public void onDisable() {
        DatabaseConnector.getInstance().disconnect(this.getClass());
    }

    public void initCommands() {
        getCommand("createDistrict").setExecutor(new CreateDistrictCommand());
        getCommand("deleteDistrict").setExecutor(new DeleteDistrictCommand());
        getCommand("setDistrictTeleport").setExecutor(new SetDistrictTeleportCommand());
        getCommand("districtTeleport").setExecutor(new DistrictTelportCommand());
        getCommand("districtInfo").setExecutor(new DistrictInfoCommand());

        getCommand("createPlot").setExecutor(new CreatePlotCommand());
        getCommand("deletePlot").setExecutor(new DeletePlotCommand());
        getCommand("gs").setExecutor(new PlotMainCommand());
    }

    public void initListener() {

    }

    public static MelonPlots getInstance() {
        return instance;
    }

    public WorldEditPlugin getWorldEditPlugin() {
        return worldEditPlugin;
    }

    public WorldGuardPlugin getWorldGuardPlugin() {
        return worldGuardPlugin;
    }

    public MelonPlotsMysql getMelonPlotsMysql() {
        return melonPlotsMysql;
    }

    public DistrictHandler getDistrictHandler() {
        return districtHandler;
    }

    public PlotHandler getPlotHandler() {
        return plotHandler;
    }
}
