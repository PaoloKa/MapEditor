package com.rs.cache.definitions;

import java.util.ArrayList;
import java.util.List;

public class LocationsDefinition
{
    private int regionX;
    private int regionY;
    private List<Location> locations = new ArrayList<>();

    public void setRegionX(int regionX) {
        this.regionX = regionX;
    }

    public void setRegionY(int regionY) {
        this.regionY = regionY;
    }

    public List<Location> getLocations() {
        return locations;
    }
}

