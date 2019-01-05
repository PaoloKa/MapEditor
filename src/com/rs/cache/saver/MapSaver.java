package com.rs.cache.saver;

import com.alex.io.OutputStream;
import com.alex.io.OutputStream2;
import com.rs.cache.definitions.MapDefinition;

public class MapSaver
{
    public static byte[] getData(MapDefinition map)
    {
        MapDefinition.Tile[][][] tiles = map.getTiles();
        OutputStream2 out = new OutputStream2();
        for (int z = 0; z < 4; z++)
        {
            for (int x = 0; x < 64; x++)
            {
                for (int y = 0; y < 64; y++)
                {
                    MapDefinition.Tile tile = tiles[z][x][y];
                    if (tile.attrOpcode != 0)
                    {
                        out.writeByte(tile.attrOpcode);
                        out.writeByte(tile.overlayId);
                    }
                   if (tile.mask != 0)
                    {
                        out.writeByte(tile.mask + 49);
                    }
                    if (tile.underlayId != 0)
                    {
                        out.writeByte(tile.underlayId + 81);
                    }
                    if (tile.height == null)
                    {
                        out.writeByte(0);
                    }
                    else
                    {
                        out.writeByte(1);
                        out.writeByte(tile.height);
                    }
                }
            }
        }
        return out.flip();
    }
}