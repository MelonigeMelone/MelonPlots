package de.melonigemelone.plots.model;

import de.melonigemelone.plots.MelonPlots;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class District {

    private int id;
    private final String name;
    private DistrictType districtType;
    private double buyPrice;
    private double rentPrice;
    private Location teleportLocation;
    private List<Plot> plots;

    public District(int id, String name, DistrictType districtType, double buyPrice, double rentPrice, Location teleportLocation) {
        this.id = id;
        this.name = name;
        this.districtType = districtType;
        this.buyPrice = buyPrice;
        this.rentPrice = rentPrice;
        this.teleportLocation = teleportLocation;
        this.plots = new ArrayList<>();
    }

    public District(String name, DistrictType districtType, double buyPrice, double rentPrice, Location teleportLocation) {
        this.name = name;
        this.districtType = districtType;
        this.buyPrice = buyPrice;
        this.rentPrice = rentPrice;
        this.teleportLocation = teleportLocation;
        this.plots = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public DistrictType getDistrictType() {
        return districtType;
    }


    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
        updateDistrictInDataBase();
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
        updateDistrictInDataBase();
    }

    public boolean hasTeleportLocation() {
        return teleportLocation != null;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
        updateDistrictInDataBase();
    }

    public List<Plot> getPlots() {
        return plots;
    }

    public void setPlots(List<Plot> plots) {
        this.plots = plots;
    }

    public void updateDistrictInDataBase() {
        MelonPlots.getInstance().getMelonPlotsMysql().updateDistrict(this);
    }
}