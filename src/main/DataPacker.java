package main;

import com.alex.store.Store;
import com.rs.cache.Cache;
import com.rs.cache.OsrsIndexes;
import com.rs.cache.definitions.Location;
import com.rs.cache.definitions.LocationsDefinition;
import com.rs.cache.definitions.ObjectDefinition;
import com.rs.cache.loaders.ObjectLoader;
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
    public static void readLoop() throws IOException {
        LocationsDefinition def = Main.loadedObjects;
        for(Location l : def.getLocations()){
            int id = addObject(l.getId());
            if(id != -1)
            l.setId(id);
        }

        for(Location l : def.getLocations()){
            System.out.println(l.getId());
        }
    }

    /**
     * packs a given osrs obejct into a rs2 cache
     * @param osrsID
     * @return
     * @throws IOException
     */
    public static int addObject(int osrsID) throws IOException {
        byte[] data = osrs.getIndexes()[OsrsIndexes.OBJECT_DEFINITIONS.getIndice()].getFile(OsrsIndexes.OBJECT_DEFINITIONS.getArchive(), osrsID);
        ObjectDefinition def = ObjectLoader.load(osrsID, data);
        if(packedObjects.containsKey(osrsID)) // not adding already existing objects again xD
            return packedObjects.get(osrsID);
        if(def.getObjectModels() == null)
            return -1;
        for(int i = 0; i < def.getObjectModels().length; i++){
            def.getObjectModels()[i] = packModel(def.getObjectModels()[i]);
        }
        objectArchiveLastID++;
        packedObjects.put(osrsID, objectArchiveLastID);
        byte[] rawData = ObjectSaver.save(def);
        System.out.println("Packed object "+osrsID+ " into "+objectArchiveLastID);
      //  Cache.STORE.getIndexes()[16].putFile(getArchiveId(objectArchiveLastID),objectArchiveLastID & 0xff, rawData);
        return objectArchiveLastID;

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
                //toPack.getIndexes()[7].putFile(modelArchiveLastID, 0, modelData);
                /*if(toPack.getIndexes()[7].getFile(modelArchiveLastID, 0) != null)
                    System.out.println("MODEL PACKED SUCCESFULLY");*/
                return modelArchiveLastID;
            }
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
