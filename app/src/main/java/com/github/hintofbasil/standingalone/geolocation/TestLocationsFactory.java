package com.github.hintofbasil.standingalone.geolocation;

import android.location.Location;

/**
 * Created by will on 17/12/16.
 */
public class TestLocationsFactory extends LocationsFactory {

    @Override
    public Location[] getLocations() {
        throw new UnsupportedOperationException("Test data must be implemented on a per user basis");
    }
}
