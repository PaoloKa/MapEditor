package com.rs.cache.definitions;

public class TextureDefinition
{
    public int textureRGB;
    public boolean field1778;
    private int id;
    private int[] fileIds;
    public int[] field1780;
    public int[] field1781;
    public int[] field1786;
    public int field1782;
    public int field1783;

    public transient int[] pixels;


    static int adjustRGB(int var0, double var1)
    {
        double var3 = (double) (var0 >> 16) / 256.0D;
        double var5 = (double) (var0 >> 8 & 255) / 256.0D;
        double var7 = (double) (var0 & 255) / 256.0D;
        var3 = Math.pow(var3, var1);
        var5 = Math.pow(var5, var1);
        var7 = Math.pow(var7, var1);
        int var9 = (int) (var3 * 256.0D);
        int var10 = (int) (var5 * 256.0D);
        int var11 = (int) (var7 * 256.0D);
        return var11 + (var10 << 8) + (var9 << 16);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFileIds(int[] fileIds) {
        this.fileIds = fileIds;
    }
}