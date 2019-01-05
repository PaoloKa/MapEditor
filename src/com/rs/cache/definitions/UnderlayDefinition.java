package com.rs.cache.definitions;

public class UnderlayDefinition
{
    private int id;
    private int color;

    private transient int hue;
    private transient int saturation;
    private transient int lightness;
    private transient int hueMultiplier;
    private byte texture;

    public void calculateHsl() {
        int var1 = color;
        double var2 = (double) (var1 >> 16 & 255) / 256.0D;
        double var4 = (double) (var1 >> 8 & 255) / 256.0D;
        double var6 = (double) (var1 & 255) / 256.0D;
        double var8 = var2;
        if (var4 < var2) {
            var8 = var4;
        }

        if (var6 < var8) {
            var8 = var6;
        }

        double var10 = var2;
        if (var4 > var2) {
            var10 = var4;
        }

        if (var6 > var10) {
            var10 = var6;
        }

        double var12 = 0.0D;
        double var14 = 0.0D;
        double var16 = (var10 + var8) / 2.0D;
        if (var8 != var10) {
            if (var16 < 0.5D) {
                var14 = (var10 - var8) / (var8 + var10);
            }

            if (var16 >= 0.5D) {
                var14 = (var10 - var8) / (2.0D - var10 - var8);
            }

            if (var2 == var10) {
                var12 = (var4 - var6) / (var10 - var8);
            } else if (var10 == var4) {
                var12 = 2.0D + (var6 - var2) / (var10 - var8);
            } else if (var10 == var6) {
                var12 = 4.0D + (var2 - var4) / (var10 - var8);
            }
        }

        var12 /= 6.0D;
        this.saturation = (int) (var14 * 256.0D);
        this.lightness = (int) (var16 * 256.0D);
        if (this.saturation < 0) {
            this.saturation = 0;
        } else if (this.saturation > 255) {
            this.saturation = 255;
        }

        if (this.lightness < 0) {
            this.lightness = 0;
        } else if (this.lightness > 255) {
            this.lightness = 255;
        }

        if (var16 > 0.5D) {
            this.hueMultiplier = (int) (var14 * (1.0D - var16) * 512.0D);
        } else {
            this.hueMultiplier = (int) (var14 * var16 * 512.0D);
        }

        if (this.hueMultiplier < 1) {
            this.hueMultiplier = 1;
        }

        this.hue = (int) ((double) this.hueMultiplier * var12);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSaturation() {
        return saturation;
    }

    public int getLightness() {
        return lightness;
    }

    public int getHue() {
        return hue;
    }

    public int getCollor() {
        return color;
    }

    public void setTexture(byte texture) {
        this.texture = texture;
    }

    public int getId() {
        return id;
    }
}
