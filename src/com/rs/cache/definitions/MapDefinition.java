package com.rs.cache.definitions;

public class MapDefinition
{
    public static final int X = 64;
    public static final int Y = 64;
    public static final int Z = 4;




    public static class Tile
    {
        public Integer height;
        public int attrOpcode;
        public byte mask;
        public byte overlayId;
        public byte shape;
        public byte overlayRotation;
        public byte underlayId;

        public Integer getHeight() {
            return height;
        }

        public byte getOverlayId() {
            return overlayId;
        }

        public byte getOverlayPath() {
            return shape;
        }

        public byte getOverlayRotation() {
            return overlayRotation;
        }

        public byte getSettings() {
            return mask;
        }

        public byte getUnderlayId() {
            return underlayId;
        }
    }

    private int regionX;
    private int regionY;
    private Tile[][][] tiles = new Tile[Z][X][Y];

    public int getRegionX() {
        return regionX;
    }

    public void setRegionX(int regionX) {
        this.regionX = regionX;
    }

    public int getRegionY() {
        return regionY;
    }

    public void setRegionY(int regionY) {
        this.regionY = regionY;
    }
    public Tile[][][] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[][][] tiles) {
        this.tiles = tiles;
    }


    }