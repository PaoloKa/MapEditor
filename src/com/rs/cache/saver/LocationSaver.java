package com.rs.cache.saver;

import com.alex.io.OutputStream2;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.rs.cache.definitions.Location;
import com.rs.cache.definitions.LocationsDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocationSaver
{
    public static byte[] getData(LocationsDefinition locs)
    {
        Multimap<Integer, Location> locById = LinkedListMultimap.create();
        List<Location> sortedLocs = new ArrayList<>(locs.getLocations());
        sortedLocs.sort((l1, l2) -> Integer.compare(l1.getId(), l2.getId()));
        for (Location loc : sortedLocs)
        {
            locById.put(loc.getId(), loc);
        }
        OutputStream2 out = new OutputStream2();
        int prevId = -1;
        for (Integer id : locById.keySet())
        {

            int diffId = id - prevId;
            prevId = id;
            out.b(diffId);
                Collection<Location> locations = locById.get(id);
                int position = 0;
                for (Location loc : locations)
                {
                    int packedPosition = (loc.getPosition().getZ() << 12)
                            | (loc.getPosition().getX() << 6)
                            | (loc.getPosition().getY());

                    int diffPos = packedPosition - position;
                position = packedPosition;
                out.writeShortSmart(diffPos + 1);

                int packedAttributes = (loc.getType() << 2) | loc.getOrientation();
                out.writeByte(packedAttributes);
            }

            out.writeShortSmart(0);
        }
        out.writeShortSmart(0);
        return out.flip();
    }
}
