package com.github.hintofbasil.standingalone.geolocation;

import android.content.pm.ApplicationInfo;
import android.location.Location;

/**
 * Created by will on 17/12/16.
 */
public class LocationsFactory {

    public Location[] getLocations() {
        throw new UnsupportedOperationException("Locations for live app have not yet been implemented");
    }

    protected Location createLocation(double lat, double lng) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }
}
