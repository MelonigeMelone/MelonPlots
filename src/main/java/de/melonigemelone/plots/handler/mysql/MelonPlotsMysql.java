package de.melonigemelone.plots.handler.mysql;

import de.melonigemelone.api.lib.configuration.LocationSerialization;
import de.melonigemelone.api.lib.mysql.DatabaseConnector;
import de.melonigemelone.api.lib.mysql.model.MysqlUtil;
import de.melonigemelone.plots.MelonPlots;
import de.melonigemelone.plots.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MelonPlotsMysql {

    private final MysqlUtil mysqlUtil;

    public MelonPlotsMysql(MelonPlots melonPlots) {
        mysqlUtil = DatabaseConnector.getMysqlInstance(melonPlots.getClass());

        createTables();
    }

    public void createTables() {
        mysqlUtil.executeQuery("CREATE TABLE IF NOT EXISTS districts(" +
                "id int(11) unsigned NOT NULL AUTO_INCREMENT," +
                "name varchar(64)," +
                "type varchar(64)," +
                "buyPrice double," +
                "rentPrice double," +
                "teleportLocation varchar(128)," +
                "PRIMARY KEY (id));");

        mysqlUtil.executeQuery("CREATE TABLE IF NOT EXISTS plots(" +
                "id int(11) unsigned NOT NULL AUTO_INCREMENT," +
                "regionId varchar(64)," +
                "regionName varchar(64)," +
                "districtId int(11)," +
                "ownerUUID varchar(64)," +
                "plotStatus varchar(64)," +
                "plotType varchar(64)," +
                "price double," +
                "sellPrice double," +
                "lastRent long," +
                "PRIMARY KEY (id));");
    }

    public void createDistrict(District district) {
        HashMap<Integer, Object> map = new HashMap<>();
        map.put(1, district.getName());
        map.put(2, district.getDistrictType().name());
        map.put(3, district.getBuyPrice());
        map.put(4, district.getRentPrice());
        map.put(5, LocationSerialization.getStringFromLocation(district.getTeleportLocation()));

        CompletableFuture<Integer> completableFuture = mysqlUtil.executeInsertWithReturnNewID("INSERT INTO districts (name,type,buyPrice,rentPrice,teleportLocation) VALUES (?,?,?,?,?);", map, "id");
        try {
            int id = completableFuture.get();
            district.setId(id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void deleteDistrict(District district) {
        HashMap<Integer, Object> map = new HashMap<>();
        map.put(1, district.getId());

        mysqlUtil.executeQuery("DELETE FROM districts WHERE id = ?", map);
    }

    public void updateDistrict(District district) {
        HashMap<Integer, Object> map = new HashMap<>();
        map.put(1, district.getName());
        map.put(2, district.getDistrictType().name());
        map.put(3, district.getBuyPrice());
        map.put(4, district.getRentPrice());
        map.put(5, LocationSerialization.getStringFromLocation(district.getTeleportLocation()));
        map.put(6, district.getId());

        mysqlUtil.executeQuery("UPDATE districts SET name = ?, type = ?, buyPrice = ?, rentPrice = ?,teleportLocation = ? WHERE id = ?;", map);
    }

    public List<District> loadDistricts() {
        CompletableFuture<ResultSet> completableFuture = mysqlUtil.executeQuery("SELECT * FROM districts");

        List<District> districts = new ArrayList<>();
        ResultSet resultSet;
        try {
            resultSet = completableFuture.get();

            try {
                while (resultSet.next()) {
                    districts.add(new District(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            DistrictType.valueOf(resultSet.getString("type")),
                            resultSet.getDouble("buyPrice"),
                            resultSet.getDouble("rentPrice"),
                            LocationSerialization.getLocationFromString(resultSet.getString("teleportLocation"))
                    ));
                }
            } catch (final SQLException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return districts;
    }

    public void createPlot(Plot plot) {
        HashMap<Integer, Object> map = new HashMap<>();
        map.put(1, plot.getRegionId());
        map.put(2, plot.getRegionName());
        map.put(3, plot.getDistrictId());
        map.put(4, plot.getOwnerUUID());
        map.put(5, plot.getPlotStatus().name());
        map.put(6, plot.getPlotType().name());
        map.put(7, plot.getPrice());
        map.put(8, plot.getSellPrice());
        map.put(9, plot.getEnd());

        CompletableFuture<Integer> completableFuture = mysqlUtil.executeInsertWithReturnNewID("INSERT INTO plots (regionId,regionName,districtId,ownerUUID,plotStatus,plotType,price,sellPrice,lastRent) VALUES (?,?,?,?,?,?,?,?,?);", map, "id");
        try {
            int id = completableFuture.get();
            plot.setId(id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void deletePlot(Plot plot) {
        HashMap<Integer, Object> map = new HashMap<>();
        map.put(1, plot.getId());

        mysqlUtil.executeQuery("DELETE FROM plots WHERE id = ?", map);
    }

    public void updatePlot(Plot plot) {
        HashMap<Integer, Object> map = new HashMap<>();
        map.put(1, plot.getOwnerUUID());
        map.put(2, plot.getPlotStatus().name());
        map.put(3, plot.getPrice());
        map.put(4, plot.getSellPrice());
        map.put(5, plot.getEnd());
        map.put(6, plot.getId());

        mysqlUtil.executeQuery("UPDATE plots SET ownerUUID = ?,plotStatus = ?,price = ?, sellPrice = ?, lastRent = ? WHERE id = ?;", map);
    }

    public List<Plot> loadPlots() {
        CompletableFuture<ResultSet> completableFuture = mysqlUtil.executeQuery("SELECT * FROM plots");

        List<Plot> plots = new ArrayList<>();
        ResultSet resultSet;
        try {
            resultSet = completableFuture.get();

            try {
                while (resultSet.next()) {
                    plots.add(new Plot(
                            resultSet.getInt("id"),
                            resultSet.getString("regionId"),
                            resultSet.getString("regionName"),
                            resultSet.getInt("districtId"),
                            resultSet.getString("ownerUUID"),
                            PlotStatus.valueOf(resultSet.getString("plotStatus")),
                            PlotType.valueOf(resultSet.getString("plotType")),
                            resultSet.getDouble("price"),
                            resultSet.getLong("lastRent"),
                            resultSet.getDouble("sellPrice")
                    ));
                }
            } catch (final SQLException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return plots;
    }


}
