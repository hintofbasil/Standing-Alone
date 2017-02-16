package com.github.hintofbasil.standingalone.geolocation;

import android.content.pm.ApplicationInfo;
import android.location.Location;

/**
 * Created by will on 17/12/16.
 */
public class LocationsFactory {

    public Location[] getLocations() {
        Location[] locations = new Location[10];
        locations[0] = createLocation(55.90688997547829, -3.425231492945261); // The Suck
        locations[1] = createLocation(55.90634198689964, -3.427762643197); // Stone House
        locations[2] = createLocation(55.90622570196397, -3.429853338269866); // Temple of Apollo
        locations[3] = createLocation(55.90562486287725, -3.428451932905319); // In Memory
        locations[4] = createLocation(55.904683153888335, -3.423970476615136); // Life mounds 1
        locations[5] = createLocation(55.90418391890694, -3.4221492263718303); // Life mounds 2
        locations[6] = createLocation(55.903141832636024, -3.420337195196831); // Duck pond
        locations[7] = createLocation(55.90284511304614, -3.42198192123814); // The Light Pours Out of Me
        locations[8] = createLocation(55.90740371243897, -3.424072890771568); // Estate Office
        locations[9] = createLocation(55.907187183708864, -3.424675202315717); // Big tree
        return locations;
    }

    protected Location createLocation(double lat, double lng) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }
}
