package de.melonigemelone.plots.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.melonigemelone.plots.handler.mysql.MelonPlotsMysql;
import de.melonigemelone.plots.model.District;

public class DistrictHandler {

    private HashMap<String, District> districts = new HashMap<>();
    private MelonPlotsMysql melonPlotsMysql;

    public DistrictHandler(MelonPlotsMysql melonPlotsMysql) {
        this.melonPlotsMysql = melonPlotsMysql;
        loadDistrictsFromDataBase();
    }

    public void addDistrict(District district) {
        districts.put(district.getName().toLowerCase(), district);
    }

    public District getDistrictFromName(String name) {
        return districts.get(name.toLowerCase());
    }

    public District getDistrictFromId(int id) {
        for(District district : districts.values()) {
            if(district.getId() == id) {
                return district;
            }
        }
        return null;
    }

    public boolean existsDistrictWithName(String name) {
        return districts.containsKey(name.toLowerCase());
    }

    public void loadDistrictsFromDataBase() {
        List<District> districts = melonPlotsMysql.loadDistricts();
        for(District district : districts) {
            this.districts.put(district.getName().toLowerCase(), district);
        }
    }

    public void createDistrict(District district) {
        melonPlotsMysql.createDistrict(district);
        addDistrict(district);
    }

    public void deleteDistrict(District district) {
        melonPlotsMysql.deleteDistrict(district);
        districts.remove(district.getName().toLowerCase());
    }

}
