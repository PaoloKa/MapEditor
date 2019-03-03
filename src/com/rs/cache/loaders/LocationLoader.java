package com.rs.cache.loaders;

import com.alex.io.InputStream;
import com.alex.io.InputStream2;
import com.rs.cache.definitions.Location;
import com.rs.cache.definitions.LocationsDefinition;
import com.rs.cache.definitions.Position;

public class LocationLoader
{
    public LocationsDefinition load(int regionX, int regionY, byte[] b)
    {
        LocationsDefinition loc = new LocationsDefinition();
        loc.setRegionX(regionX);
        loc.setRegionY(regionY);
        loadLocations(loc, b);
        return loc;
    }

    private void loadLocations(LocationsDefinition loc, byte[] b)
    {
        InputStream2 buf = new InputStream2(b);

        int id = -1;
        int idOffset;

        while ((idOffset = buf.readUnsignedIntSmartShortCompat()) != 0)
        {


            id += idOffset;
            System.out.println(id+" "+idOffset);
            int position = 0;
            int positionOffset;

            while ((positionOffset = buf.readUnsignedShortSmart()) != 0)
            {
                position += positionOffset - 1;

                int localY = position & 0x3F;
                int localX = position >> 6 & 0x3F;
                int height = position >> 12 & 0x3;

                int attributes = buf.readUnsignedByte();
                int type = attributes >> 2;
                int orientation = attributes & 0x3;
                loc.getLocations().add(new Location(id, type, orientation, new Position(localX, localY, height)));
            }
        }
    }
}