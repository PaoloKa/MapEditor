package sample;

import com.rs.cache.definitions.OverlayDefinition;
import com.rs.cache.definitions.UnderlayDefinition;

public class Utils {


    /**
     * gets the right archive Id for the map
     * @param x
     * @param y
     * @return
     */
    public static int getArchiveId(int x, int y){
        int regionId = (x >> 6 << 8)  + (y >> 6);
        int regionX = (regionId >> 8) * 64;
        int regionY = (regionId & 0xff) * 64;
        int mapArchiveId = Main.RS2_CACHE.getIndexes()[5].getArchiveId("m" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));

        return mapArchiveId;
    }

    /**
     * gets the objects layer
     * @param x
     * @param y
     * @return
     */
    public static int getMapArchiveId(int x, int y){
        int regionId = (x >> 6 << 8)  + (y >> 6);
        int regionX = (regionId >> 8) * 64;
        int regionY = (regionId & 0xff) * 64;
        int landscapeArchiveId =  Main.RS2_CACHE.getIndexes()[5].getArchiveId("l" + ((regionX >> 3) / 8) + "_" + ((regionY >> 3) / 8));
        return landscapeArchiveId;
    }


    public static int adjustRGB(int var0, double var1)
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

}
