package com.rs.cache.definitions;

import lombok.Data;

import java.util.Map;

@Data
public class OsrsObjectDefinition
{
    private int id;
    private short[] retextureToFind;
    private int anInt2069 = 16;
    private boolean isSolid = false;
    private String name = "null";
    private int[] objectModels;
    private int[] objectTypes;
    private short[] recolorToFind;
    private int mapAreaId = -1;
    private short[] textureToReplace;
    private int sizeX = 1;
    private int sizeY = 1;
    private int anInt2083 = 0;
    private int[] anIntArray2084;
    private int offsetX = 0;
    private boolean nonFlatShading = false;
    private int anInt2088 = -1;
    private int animationID = -1;
    private int varbitID = -1;
    private int ambient = 0;
    private int contrast = 0;
    private String[] actions = new String[5];
    private int interactType = 2;
    private int mapSceneID = -1;
    private short[] recolorToReplace;
    private boolean aBool2097 = true;
    private int modelSizeX = 128;
    private int modelSizeHeight = 128;
    private int modelSizeY = 128;
    private int objectID;
    private int offsetHeight = 0;
    private int offsetY = 0;
    private boolean aBool2104 = false;
    private int anInt2105 = -1;
    private int anInt2106 = -1;
    private int[] configChangeDest;
    private boolean isRotated = false;
    private int varpID = -1;
    private int anInt2110 = -1;
    private boolean aBool2111 = false;
    private int anInt2112 = 0;
    private int anInt2113 = 0;
    private boolean blocksProjectile = true;
    private Map<Integer, Object> params = null;

}