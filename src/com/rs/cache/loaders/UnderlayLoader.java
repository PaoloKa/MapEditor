package com.rs.cache.loaders;

import com.alex.io.InputStream;
import com.rs.cache.definitions.UnderlayDefinition;

public class UnderlayLoader
{


    public UnderlayDefinition load(int id, byte[] b)
    {
        UnderlayDefinition def = new UnderlayDefinition();
        InputStream is = new InputStream(b);

        def.setId(id);
       // System.out.println("NEW------------");
        for (;;)
        {
            int opcode = is.readUnsignedByte();
        //    System.out.println("Opcode" +opcode);
            if (opcode == 0)
            {
                break;
            }

            if (opcode == 1)
            {
                int color = is.read24BitInt();
                def.setColor(color);
            }

            else if (opcode == 2)
            {
                int texture = is.readUnsignedByte();
                def.setTexture((byte)texture);
            }
        }

        def.calculateHsl();

        return def;
    }
}
