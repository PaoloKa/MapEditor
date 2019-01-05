package com.rs.cache.loaders;

import com.alex.io.InputStream;
import com.rs.cache.definitions.MapDefinition;

public class MapLoader
{
    public MapDefinition load(int regionX, int regionY, byte[] b)
    {
        MapDefinition map = new MapDefinition();
        map.setRegionX(regionX);
        map.setRegionY(regionY);
        loadTerrain(map, b);
        return map;
    }

    private void loadTerrain(MapDefinition map, byte[] buf)
    {
        MapDefinition.Tile[][][] tiles = map.getTiles();

        InputStream in = new InputStream(buf);

        for (int z = 0; z < MapDefinition.Z; z++)
        {
            for (int x = 0; x < MapDefinition.X; x++)
            {
                for (int y = 0; y < MapDefinition.Y; y++)
                {
                    MapDefinition.Tile tile = tiles[z][x][y] = new MapDefinition.Tile();
                    while (true)
                    {
                        int attribute = in.readUnsignedByte();
                        if (attribute == 0)
                        {
                            break;
                        }
                        else if (attribute == 1)
                        {
                            int height = in.readUnsignedByte();
                            tile.height = height;

                            break;
                        }
                        else if (attribute <= 49)
                        {
                            tile.attrOpcode = attribute;
                            tile.overlayId = (byte)in.readByte();
                            tile.shape = (byte) ((attribute - 2) / 4);
                            tile.overlayRotation = (byte) (attribute - 2 & 3);
                           // System.out.println(x+" " +y+" "+z+" overlay = "+tile.overlayId);
                        }
                        else if (attribute <= 81)
                        {
                            tile.mask = (byte) (attribute - 49);
                        }
                        else
                        {
                            tile.underlayId = (byte) (attribute - 81);
                        }

                    }

                }
            }
        }
    }
}
