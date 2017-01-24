package com.github.hintofbasil.standingalone.geolocation;

import android.content.pm.ApplicationInfo;
import android.location.Location;

/**
 * Created by will on 17/12/16.
 */
public class LocationsFactory {

    public Location[] getLocations() {
        Location[] locations = new Location[10];
        locations[0] = createLocation(55.60669955, -3.42525819); // The Suck
        locations[1] = createLocation(55.9111988, -3.434973); // Stone House
        locations[2] = createLocation(55.9060177, -3.43002497); // Temple of Apollo
        locations[3] = createLocation(55.90572187, -3.42817361); // In Memory
        locations[4] = createLocation(55.901317, -3.4145172); // Life mounds 1
        locations[5] = createLocation(55.897764, -3.4295833); // Life mounds 2
        locations[6] = createLocation(55.90138, -3.4122425); // Duck pond
        locations[7] = createLocation(55.9042894, -3.4203787); // The Light Pours Out of Me
        locations[8] = createLocation(55.90702105, -3.42455728); // Estate Office
        locations[9] = createLocation(55.90702105, -3.42455728); // Coppice Room
        return locations;
    }

    protected Location createLocation(double lat, double lng) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }
}
