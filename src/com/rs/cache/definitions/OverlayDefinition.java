package com.rs.cache.definitions;

public class OverlayDefinition
{
    private int id;
    private int rgbColor = 0;
    private int texture = -1;
    private int secondaryRgbColor = -1;
    private boolean hideUnderlay = true;

    private transient int hue;
    private transient int saturation;
    private transient int lightness;

    private transient int otherHue;
    private transient int otherSaturation;
    private transient int otherLightness;

    public void calculateHsl()
    {
        if (getSecondaryRgbColor() != -1)
        {
            calculateHsl(getSecondaryRgbColor());
            setOtherHue(getHue());
            setOtherSaturation(getSaturation());
            setOtherLightness(getLightness());
        }

        calculateHsl(getRgbColor());
    }

    private void calculateHsl(int var1)
    {
        double var2 = (double) (var1 >> 16 & 255) / 256.0D;
        double var4 = (double) (var1 >> 8 & 255) / 256.0D;
        double var6 = (double) (var1 & 255) / 256.0D;
        double var8 = var2;
        if (var4 < var2)
        {
            var8 = var4;
        }

        if (var6 < var8)
        {
            var8 = var6;
        }

        double var10 = var2;
        if (var4 > var2)
        {
            var10 = var4;
        }

        if (var6 > var10)
        {
            var10 = var6;
        }

        double var12 = 0.0D;
        double var14 = 0.0D;
        double var16 = (var8 + var10) / 2.0D;
        if (var10 != var8)
        {
            if (var16 < 0.5D)
            {
                var14 = (var10 - var8) / (var10 + var8);
            }

            if (var16 >= 0.5D)
            {
                var14 = (var10 - var8) / (2.0D - var10 - var8);
            }

            if (var2 == var10)
            {
                var12 = (var4 - var6) / (var10 - var8);
            }
            else if (var4 == var10)
            {
                var12 = 2.0D + (var6 - var2) / (var10 - var8);
            }
            else if (var10 == var6)
            {
                var12 = 4.0D + (var2 - var4) / (var10 - var8);
            }
        }

        var12 /= 6.0D;
        this.setHue((int) (256.0D * var12));
        this.setSaturation((int) (var14 * 256.0D));
        this.setLightness((int) (var16 * 256.0D));
        if (this.getSaturation() < 0)
        {
            this.setSaturation(0);
        }
        else if (this.getSaturation() > 255)
        {
            this.setSaturation(255);
        }

        if (this.getLightness() < 0)
        {
            this.setLightness(0);
        }
        else if (this.getLightness() > 255)
        {
            this.setLightness(255);
        }

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRgbColor(int rgbColor) {
        this.rgbColor = rgbColor;
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }

    public void setHideUnderlay(boolean hideUnderlay) {
        this.hideUnderlay = hideUnderlay;
    }

    public void setSecondaryRgbColor(int secondaryRgbColor) {
        this.secondaryRgbColor = secondaryRgbColor;
    }

    public int getId() {
        return id;
    }

    public int getRgbColor() {
        return rgbColor;
    }

    public int getTexture() {
        return texture;
    }

    public int getSecondaryRgbColor() {
        return secondaryRgbColor;
    }

    public boolean isHideUnderlay() {
        return hideUnderlay;
    }

    public int getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getLightness() {
        return lightness;
    }

    public void setLightness(int lightness) {
        this.lightness = lightness;
    }

    public int getOtherHue() {
        return otherHue;
    }

    public void setOtherHue(int otherHue) {
        this.otherHue = otherHue;
    }

    public int getOtherSaturation() {
        return otherSaturation;
    }

    public void setOtherSaturation(int otherSaturation) {
        this.otherSaturation = otherSaturation;
    }

    public int getOtherLightness() {
        return otherLightness;
    }

    public void setOtherLightness(int otherLightness) {
        this.otherLightness = otherLightness;
    }
}
