package com.rs.cache.loaders;

import com.alex.io.InputStream;
import com.rs.cache.definitions.OverlayDefinition;

public class OverlayLoader
{

    /**
     * Index index = store.getIndex(IndexType.CONFIGS);
     Archive archive = index.getArchive(ConfigType.OVERLAY.getId());
     * @param id
     * @param b
     * @return
     */

    public OverlayDefinition load(int id, byte[] b)
    {
        OverlayDefinition def = new OverlayDefinition();
        InputStream is = new InputStream(b);

        def.setId(id);

        for (;;)
        {
            int opcode = is.readUnsignedByte();
            if (opcode == 0)
            {
                break;
            }

            if (opcode == 1)
            {
                int color = is.read24BitInt();
                def.setRgbColor(color);
              // System.out.println("Color :"+def.getRgbColor());
            }
            else if (opcode == 2)
            {
                int texture = is.readUnsignedByte();
                def.setTexture(texture);
            }
            else if (opcode == 5)
            {
                def.setHideUnderlay(false);

            }
            else if (opcode == 7)
            {
                int secondaryColor = is.read24BitInt();
                def.setSecondaryRgbColor(secondaryColor);
            }
        }

        def.calculateHsl();

        return def;
    }
}
