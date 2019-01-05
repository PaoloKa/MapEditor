package com.rs.cache.loaders;

import com.alex.io.InputStream;
import com.rs.cache.definitions.TextureDefinition;

public class TextureLoader {

    public TextureDefinition load(int id, byte[] b)
    {
        TextureDefinition def = new TextureDefinition();
        InputStream is = new InputStream(b);

        def.textureRGB = is.readUnsignedShort();
        if(def.textureRGB != 0)
            return def;
        def.field1778 = is.readByte() != 0;
        def.setId(id);

        int count = is.readUnsignedByte();
        int[] files = new int[count];

        for (int i = 0; i < count; ++i)
            files[i] = is.readUnsignedShort();

        def.setFileIds(files);

        if (count > 1)
        {
            def.field1780 = new int[count - 1];

            for (int var3 = 0; var3 < count - 1; ++var3)
            {
                def.field1780[var3] = is.readUnsignedByte();
            }
        }

        if (count > 1)
        {
            def.field1781 = new int[count - 1];

            for (int var3 = 0; var3 < count - 1; ++var3)
            {
                def.field1781[var3] = is.readUnsignedByte();
            }
        }

        def.field1786 = new int[count];

        for (int var3 = 0; var3 < count; ++var3)
        {
            def.field1786[var3] = is.readInt();
        }

        def.field1783 = is.readUnsignedByte();
        def.field1782 = is.readUnsignedByte();

        return def;
    }
}
