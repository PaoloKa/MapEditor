package main;

import com.alex.store.Store;
import com.rs.cache.RS2Indexes;
import com.rs.cache.definitions.Location;
import com.rs.cache.definitions.LocationsDefinition;
import com.rs.cache.definitions.OsrsObjectDefinition;
import com.rs.cache.definitions.Rs2ObjectDefinitions;
import com.rs.cache.loaders.LocationLoader;
import com.rs.cache.loaders.ObjectLoader;
import com.rs.cache.saver.LocationSaver;
import com.rs.cache.saver.ObjectSaver;

import java.io.IOException;
import java.util.HashMap;

public class DataPacker {

    /**
     * osrsId, packedIds
     */
    public static HashMap<Integer,Integer> packedObjects = new HashMap<Integer,Integer>();
    public static HashMap<Integer,Integer> packedModels = new HashMap<Integer,Integer>();
    private static Store osrs = Main.OSRS_CACHE;
    private static Store toPack = Main.RS2_CACHE;
    private static int modelArchiveLastID = toPack.getIndexes()[7].getLastArchiveId() - 1;
    private static int objectArchiveLastID = getLastObjectId();

    /**
     * loops throu the whole maplayers and packs every object
     * @throws IOException
     */
    public static void readLoop(byte[] objectlayer, byte[] mapLayer ) throws IOException {
        if(objectlayer == null)
            System.out.println("is null ");
        if(mapLayer == null)
            System.out.println(" Also null");
        LocationLoader location = new LocationLoader();
        LocationsDefinition def = location.load(0,0,objectlayer);
        for(int i = 0; i < def.getLocations().size(); i++){
            Location l = def.getLocations().get(i);
            if(l == null)
                continue;
           if(l.getId() > 0) {
               try {
                   int id = addObject(l.getId());
                   if (id != -1)
                       def.getLocations().get(i).setId(id);
                   //   l.setId(id);
               } catch(Exception ex){
                   System.out.println("Couldn't load object data.");
               }
           }

        }
        for(Location l : def.getLocations()){
            System.out.println(l.getId());
        }

        packMap(LocationSaver.getData(def),mapLayer);

    }

    /**
     * packs a given osrs obejct into a rs2 cache
     * @param osrsID
     * @return
     * @throws IOException
     */
    public static int addObject(int osrsID) throws IOException {
        byte[] data = osrs.getIndexes()[2].getFile(6, osrsID);
        OsrsObjectDefinition def = ObjectLoader.load(osrsID, data);
        Rs2ObjectDefinitions o667 = Rs2ObjectDefinitions.getObjectDefinitions(1);
        if(packedObjects.containsKey(osrsID)) {// not adding already existing objects again xD
           System.out.println("Object "+osrsID+" was already packed");
            return packedObjects.get(osrsID);
        }
        if(def == null){
            System.out.println("Def is null for object Id "+osrsID);
            return -1;
        }
        if(def.getObjectModels() == null)
            return -1;
        /**
         * setting data
         */
        //name

        if(def.getName() != null && def.getName() != "" && def.getName()!= "null")
            o667.name = def.getName();
        else
            o667.name = "";
        //object types
        if(def.getObjectTypes() != null) {
            if(osrsID == 10064)
             System.out.println(def.getObjectTypes().length+" id: "+osrsID);
            o667.possibleTypes = new byte[def.getObjectTypes().length];
            for( int i = 0; i < def.getObjectTypes().length; i ++) {
                o667.possibleTypes[i] = (byte)def.getObjectTypes()[i];
            }
        } else {
            o667.possibleTypes = new byte[1];
            o667.possibleTypes[0] = 10;
        }
        //animating if possible (won't work for everything)
        o667.objectAnimation  = def.getAnimationID();
        o667.originalColors = def.getRecolorToFind();
        o667.modifiedColors = def.getRecolorToReplace();
        /**
         * walls
         */
        if(isWall(def)) {
            int length = def.getObjectModels().length;
            o667.modelIds = new int[ def.getObjectModels().length][1];
            for(int i = 0; i < length; i++) {
                int packedModel = packModel(def.getObjectModels()[i]);
                if(packedModel != -1) //never know
                    o667.modelIds[i][0] = packedModel;
            }
        } else { //normal packing
            o667.modelIds = new int[1][def.getObjectModels().length];
            for(int i =0; i < o667.modelIds[0].length; i++) {
                int packedModel = packModel(def.getObjectModels()[i]);
                if(packedModel != -1) //never know
                    o667.modelIds[0][i] = packedModel;

            }
        }

        /* TODO in encode */
        o667.modelSizeX = def.getModelSizeX();
        o667.modelSizeY = def.getModelSizeY();
        o667.modelHeight = def.getModelSizeHeight();
        o667.modelHeightOffset = def.getOffsetHeight();
        o667.ignoreClipOnAlternativeRoute = def.isSolid();
        o667.yOffset = def.getOffsetY();
        o667.secondBool = def.isABool2104();
        o667.contrast = def.getContrast();
      /*  if(def.getAnInt2105() == 0)
            o667.aByte3912 = 1;*/
        if(def.isABool2111())
            o667.thirdInt = 1;
        if(def.isBlocksProjectile())
            o667.projectileCliped = true;
        o667.rotated = def.isRotated();
        o667.sizeX = def.getSizeX();
        o667.sizeY = def.getSizeY();
        o667.nonFlatShading = def.isNonFlatShading();
        o667.options = def.getActions();
        byte[] rawData = o667.encode();
        objectArchiveLastID++;
        packedObjects.put(osrsID, objectArchiveLastID);
        System.out.println("Packed object "+osrsID+ " into "+objectArchiveLastID);
        toPack.getIndexes()[16].putFile(getArchiveId(objectArchiveLastID),objectArchiveLastID & 0xff, rawData);
        return objectArchiveLastID;

    }

    private static boolean isWall(OsrsObjectDefinition obj) {
        if(obj.getObjectTypes() == null) {
            return false;
        }
        for(int i=0; i < obj.getObjectTypes().length;i++) {
            if(obj.getObjectTypes()[i] != 10 && obj.getObjectTypes()[i] != 11 && obj.getObjectTypes()[i] != 22 )
                return true;
        }
        return false;
    }

    /**
     * packs a given model Id from the osrs cache into the RS2 cache.
     * @param modelId
     * @return the packed ID
     * @throws IOException
     */
    public static int packModel(int modelId) throws IOException {

            if(packedModels.containsKey(modelId)) {
                System.out.println("Model "+modelId+ " not packed since it is already packed.");
                return packedModels.get(modelId);
            } else {
                modelArchiveLastID++;
                packedModels.put(modelId,modelArchiveLastID);
                System.out.println("Packed model :" +modelId+ " into archive: "+modelArchiveLastID);
                byte[] modelData = osrs.getIndexes()[7].getFile(modelId, 0);
                if(modelData == null) {
                    System.out.println("Osrs model data is null -> data not packed");
                    return -1;
                }
                toPack.getIndexes()[7].putFile(modelArchiveLastID, 0, modelData);
                /*if(toPack.getIndexes()[7].getFile(modelArchiveLastID, 0) != null)
                    System.out.println("MODEL PACKED SUCCESFULLY");*/
                return modelArchiveLastID;
            }
    }

    public static void packMap(byte[] objectLayer, byte[] mapLayer){
        toPack.getIndexes()[RS2Indexes.LANDSCAPES.getIndex()].putFile(Main.map_archiveId,0, mapLayer);
        toPack.getIndexes()[RS2Indexes.LANDSCAPES.getIndex()].putFile(Main.object_archiveId,0,  objectLayer);
    }

    /**
     * returns the archive ID of a given objectID
     * @param id
     * @return
     */
    private static  int getArchiveId(int id) {
        return id >>> -1135990488;
    }

    /***
     * returns the last object ID possible
     * @return
     */
    public static int getLastObjectId() {
        int lastArchiveId = toPack.getIndexes()[16].getLastArchiveId();
        return lastArchiveId * 256 + toPack.getIndexes()[16].getValidFilesCount(lastArchiveId);
    }
}
