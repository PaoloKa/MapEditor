package com.rs.cache.definitions;

import utils.HeightCalc;

import java.util.ArrayList;
import java.util.List;

public class Region
{

    public static final int X = 64;
    public static final int Y = 64;
    public static final int Z = 4;

    private final int regionID;
    private final int baseX;
    private final int baseY;

    private final int[][][] tileHeights = new int[Z][X][Y];
    private final byte[][][] tileSettings = new byte[Z][X][Y];
    private final byte[][][] overlayIds = new byte[Z][X][Y];
    private final byte[][][] overlayPaths = new byte[Z][X][Y];
    private final byte[][][] overlayRotations = new byte[Z][X][Y];
    private final byte[][][] underlayIds = new byte[Z][X][Y];

    private final List<Location> locations = new ArrayList<>();

    public Region(int id)
    {
        this.regionID = id;
        this.baseX = ((id >> 8) & 0xFF) << 6; // local coords are in bottom 6 bits (64*64)
        this.baseY = (id & 0xFF) << 6;
    }

    public Region(int x, int y)
    {
        this.regionID = x << 8 | y;
        this.baseX = x << 6;
        this.baseY = y << 6;
    }

    public void loadTerrain(MapDefinition map)
    {
        MapDefinition.Tile[][][] tiles = map.getTiles();
        for (int z = 0; z < Z; z++)
        {
            for (int x = 0; x < X; x++)
            {
                for (int y = 0; y < Y; y++)
                {
                    MapDefinition.Tile tile = tiles[z][x][y];

                    if (tile.height == null)
                    {
                        if (z == 0)
                        {
                            tileHeights[0][x][y] = -HeightCalc.calculate(baseX + x + 0xe3b7b, baseY + y + 0x87cce) * 8;
                        }
                        else
                        {
                            tileHeights[z][x][y] = tileHeights[z - 1][x][y] - 240;
                        }
                    }
                    else
                    {
                        int height = tile.getHeight();
                        if (height == 1)
                        {
                            height = 0;
                        }

                        if (z == 0)
                        {
                            tileHeights[0][x][y] = -height * 8;
                        }
                        else
                        {
                            tileHeights[z][x][y] = tileHeights[z - 1][x][y] - height * 8;
                        }
                    }

                    overlayIds[z][x][y] = tile.getOverlayId();
                    overlayPaths[z][x][y] = tile.getOverlayPath();
                    overlayRotations[z][x][y] = tile.getOverlayRotation();

                    tileSettings[z][x][y] = tile.getSettings();
                    underlayIds[z][x][y] = tile.getUnderlayId();
                }
            }
        }
    }

    public void loadLocations(LocationsDefinition locs)
    {
        for (Location loc : locs.getLocations())
        {
            Location newLoc = new Location(loc.getId(), loc.getType(), loc.getOrientation(),
                    new Position(getBaseX() + loc.getPosition().getX(),
                            getBaseY() + loc.getPosition().getY(),
                            loc.getPosition().getZ()));
            locations.add(newLoc);
        }
    }

    public int getRegionID()
    {
        return regionID;
    }

    public int getBaseX()
    {
        return baseX;
    }

    public int getBaseY()
    {
        return baseY;
    }

    public int getTileHeight(int z, int x, int y)
    {
        return tileHeights[z][x][y];
    }

    public byte getTileSetting(int z, int x, int y)
    {
        return tileSettings[z][x][y];
    }

    public int getOverlayId(int z, int x, int y)
    {
        return overlayIds[z][x][y] & 0xFF;
    }

    public byte getOverlayPath(int z, int x, int y)
    {
        return overlayPaths[z][x][y];
    }

    public byte getOverlayRotation(int z, int x, int y)
    {
        return overlayRotations[z][x][y];
    }

    public int getUnderlayId(int z, int x, int y)
    {
        return underlayIds[z][x][y] & 0xFF;
    }

    public List<Location> getLocations()
    {
        return locations;
    }

    public int getRegionX()
    {
        return baseX >> 6;
    }

    public int getRegionY()
    {
        return baseY >> 6;
    }
}
