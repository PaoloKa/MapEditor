package com.rs.cache.loaders;

import com.alex.io.InputStream2;
import com.rs.cache.definitions.ObjectDefinition;

import java.util.HashMap;
import java.util.Map;

public class ObjectLoader
{

    public ObjectDefinition load(int id, byte[] b)
    {
        ObjectDefinition def = new ObjectDefinition();
        InputStream2 is = new InputStream2(b);

        def.setId(id);

        for (;;)
        {
            int opcode = is.readUnsignedByte();
            if (opcode == 0)
            {
                break;
            }

            processOp(opcode, def, is);
        }

        post(def);

        return def;
    }

    private void processOp(int opcode, ObjectDefinition def, InputStream2 is)
    {
        if (opcode == 1)
        {
            int length = is.readUnsignedByte();
            if (length > 0)
            {
                int[] objectTypes = new int[length];
                int[] objectModels = new int[length];

                for (int index = 0; index < length; ++index)
                {
                    objectModels[index] = is.readUnsignedShort();
                    objectTypes[index] = is.readUnsignedByte();
                }

                def.setObjectTypes(objectTypes);
                def.setObjectModels(objectModels);
            }
        }
        else if (opcode == 2)
        {
            def.setName(is.readString());
        }
        else if (opcode == 5)
        {
            int length = is.readUnsignedByte();
            if (length > 0)
            {
                def.setObjectTypes(null);
                int[] objectModels = new int[length];

                for (int index = 0; index < length; ++index)
                {
                    objectModels[index] = is.readUnsignedShort();
                }

                def.setObjectModels(objectModels);
            }
        }
        else if (opcode == 14)
        {
            def.setSizeX(is.readUnsignedByte());
        }
        else if (opcode == 15)
        {
            def.setSizeY(is.readUnsignedByte());
        }
        else if (opcode == 17)
        {
            def.setInteractType(0);
            def.setBlocksProjectile(false);
        }
        else if (opcode == 18)
        {
            def.setBlocksProjectile(false);
        }
        else if (opcode == 19)
        {
            def.setAnInt2088(is.readUnsignedByte());
        }
        else if (opcode == 21)
        {
            def.setAnInt2105(0);
        }
        else if (opcode == 22)
        {
            def.setNonFlatShading(false);
        }
        else if (opcode == 23)
        {
            def.setABool2111(true);
        }
        else if (opcode == 24)
        {
            def.setAnimationID(is.readUnsignedShort());
            if (def.getAnimationID() == 0xFFFF)
            {
                def.setAnimationID(-1);
            }
        }
        else if (opcode == 27)
        {
            def.setInteractType(1);
        }
        else if (opcode == 28)
        {
            def.setAnInt2069(is.readUnsignedByte());
        }
        else if (opcode == 29)
        {
            def.setAmbient(is.readByte());
        }
        else if (opcode == 39)
        {
            def.setContrast(is.readByte());
        }
        else if (opcode >= 30 && opcode < 35)
        {
            String[] actions = def.getActions();
            actions[opcode - 30] = is.readString();
            if (actions[opcode - 30].equalsIgnoreCase("Hidden"))
            {
                actions[opcode - 30] = null;
            }
        }
        else if (opcode == 40)
        {
            int length = is.readUnsignedByte();
            short[] recolorToFind = new short[length];
            short[] recolorToReplace = new short[length];

            for (int index = 0; index < length; ++index)
            {
                recolorToFind[index] = is.readShort();
                recolorToReplace[index] = is.readShort();
            }

            def.setRecolorToFind(recolorToFind);
            def.setRecolorToReplace(recolorToReplace);
        }
        else if (opcode == 41)
        {
            int length = is.readUnsignedByte();
            short[] retextureToFind = new short[length];
            short[] textureToReplace = new short[length];

            for (int index = 0; index < length; ++index)
            {
                retextureToFind[index] = is.readShort();
                textureToReplace[index] = is.readShort();
            }

            def.setRetextureToFind(retextureToFind);
            def.setTextureToReplace(textureToReplace);
        }
        else if (opcode == 62)
        {
            def.setRotated(true);
        }
        else if (opcode == 64)
        {
            def.setABool2097(false);
        }
        else if (opcode == 65)
        {
            def.setModelSizeX(is.readUnsignedShort());
        }
        else if (opcode == 66)
        {
            def.setModelSizeHeight(is.readUnsignedShort());
        }
        else if (opcode == 67)
        {
            def.setModelSizeY(is.readUnsignedShort());
        }
        else if (opcode == 68)
        {
            def.setMapSceneID(is.readUnsignedShort());
        }
        else if (opcode == 69)
        {
            is.readByte();
        }
        else if (opcode == 70)
        {
            def.setOffsetX(is.readUnsignedShort());
        }
        else if (opcode == 71)
        {
            def.setOffsetHeight(is.readUnsignedShort());
        }
        else if (opcode == 72)
        {
            def.setOffsetY(is.readUnsignedShort());
        }
        else if (opcode == 73)
        {
            def.setABool2104(true);
        }
        else if (opcode == 74)
        {
            def.setSolid(true);
        }
        else if (opcode == 75)
        {
            def.setAnInt2106(is.readUnsignedByte());
        }
        else if (opcode == 77)
        {
            int varpID = is.readUnsignedShort();
            if (varpID == 0xFFFF)
            {
                varpID = -1;
            }
            def.setVarbitID(varpID);

            int configId = is.readUnsignedShort();
            if (configId == 0xFFFF)
            {
                configId = -1;
            }
            def.setVarpID(configId);

            int length = is.readUnsignedByte();
            int[] configChangeDest = new int[length + 2];

            for (int index = 0; index <= length; ++index)
            {
                configChangeDest[index] = is.readUnsignedShort();
                if (0xFFFF == configChangeDest[index])
                {
                    configChangeDest[index] = -1;
                }
            }

            configChangeDest[length + 1] = -1;

            def.setConfigChangeDest(configChangeDest);
        }
        else if (opcode == 78)
        {
            def.setAnInt2110(is.readUnsignedShort());
            def.setAnInt2083(is.readUnsignedByte());
        }
        else if (opcode == 79)
        {
            def.setAnInt2112(is.readUnsignedShort());
            def.setAnInt2113(is.readUnsignedShort());
            def.setAnInt2083(is.readUnsignedByte());
            int length = is.readUnsignedByte();
            int[] anIntArray2084 = new int[length];

            for (int index = 0; index < length; ++index)
            {
                anIntArray2084[index] = is.readUnsignedShort();
            }

            def.setAnIntArray2084(anIntArray2084);
        }
        else if (opcode == 81)
        {
            def.setAnInt2105(is.readUnsignedByte() * 256);
        }
        else if (opcode == 82)
        {
            def.setMapAreaId(is.readUnsignedShort());
        }
        else if (opcode == 92)
        {
            int varpID = is.readUnsignedShort();
            if (varpID == 0xFFFF)
            {
                varpID = -1;
            }
            def.setVarbitID(varpID);

            int configId = is.readUnsignedShort();
            if (configId == 0xFFFF)
            {
                configId = -1;
            }
            def.setVarpID(configId);


            int var = is.readUnsignedShort();
            if (var == 0xFFFF)
            {
                var = -1;
            }

            int length = is.readUnsignedByte();
            int[] configChangeDest = new int[length + 2];

            for (int index = 0; index <= length; ++index)
            {
                configChangeDest[index] = is.readUnsignedShort();
                if (0xFFFF == configChangeDest[index])
                {
                    configChangeDest[index] = -1;
                }
            }

            configChangeDest[length + 1] = var;

            def.setConfigChangeDest(configChangeDest);
        }
        else if (opcode == 249)
        {
            int length = is.readUnsignedByte();

            Map<Integer, Object> params = new HashMap<>(length);
            for (int i = 0; i < length; i++)
            {
                boolean isString = is.readUnsignedByte() == 1;
                int key = is.read24BitInt();
                Object value;

                if (isString)
                {
                    value = is.readString();
                }

                else
                {
                    value = is.readInt();
                }

                params.put(key, value);
            }

            def.setParams(params);
        }
        else
        {
            System.out.println("Missing opcode "+opcode);
        }
    }


    private void post(ObjectDefinition def)
    {
        if (def.getAnInt2088() == -1)
        {
            def.setAnInt2088(0);
            if (def.getObjectModels() != null && (def.getObjectTypes() == null || def.getObjectTypes()[0] == 10))
            {
                def.setAnInt2088(1);
            }

            for (int var1 = 0; var1 < 5; ++var1)
            {
                if (def.getActions()[var1] != null)
                {
                    def.setAnInt2088(1);
                }
            }
        }

        if (def.getAnInt2106() == -1)
        {
            def.setAnInt2106(def.getInteractType() != 0 ? 1 : 0);
        }
    }
}
