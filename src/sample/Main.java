package sample;

import com.alex.store.Store;
import com.rs.cache.ConfigType;
import com.rs.cache.RS2Indexes;
import com.rs.cache.definitions.*;
import com.rs.cache.loaders.LocationLoader;
import com.rs.cache.loaders.OverlayLoader;
import com.rs.cache.loaders.TextureLoader;
import com.rs.cache.loaders.UnderlayLoader;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static Store OSRS_CACHE;
    public static Store RS2_CACHE;
    public static List<OverlayDefinition> overlays = new ArrayList<OverlayDefinition>();
    public static List<UnderlayDefinition> underlays = new ArrayList<UnderlayDefinition>();
    public static List<TextureDefinition> textures = new ArrayList<TextureDefinition>();
    public static boolean p_pressed = false;
    public static int map_archiveId =   3320;//1565 ;
    public static int object_archiveId =   3322;//1565 ;
    public static MapDefinition loadedMap = null;
    public static LocationsDefinition loadedObjects = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Shneks map editor");
        primaryStage.setScene(new Scene(root, 760, 660));
        primaryStage.show();
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString() == "P")
                    p_pressed = true;

            }
        });

        primaryStage.getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().toString() == "P")
                    p_pressed = false;
            }
        });
    }


    public static void main(String[] args) {

        try {
            RS2_CACHE = new Store("C:/Users/paolo/Dropbox/Zaria 667/data/cache/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        getOverlayDefinitions();
        getUnderlayDefinitions();
        getTextureDefinitions();
        getTextureOverlay();
        launch(args);
    }


    private static void getOverlayDefinitions(){
        for(int i = 0; i < RS2_CACHE.getIndexes()[RS2Indexes.CONFIG.getIndex()].getLastFileId(ConfigType.OVERLAY.getId()); i++) {
            byte[] data = RS2_CACHE.getIndexes()[RS2Indexes.CONFIG.getIndex()].getFile(ConfigType.OVERLAY.getId(), i);
            OverlayLoader l = new OverlayLoader();
            OverlayDefinition x = l.load(i, data);
            overlays.add(i,x);

        }
    }


    private static void getUnderlayDefinitions(){
        for(int i = 0; i <   RS2_CACHE.getIndexes()[RS2Indexes.CONFIG.getIndex()].getLastFileId(ConfigType.UNDERLAY.getId()); i++) {
            byte[] data = RS2_CACHE.getIndexes()[RS2Indexes.CONFIG.getIndex()].getFile(ConfigType.UNDERLAY.getId(), i);
            UnderlayLoader l = new UnderlayLoader();
            if(data != null) {
                UnderlayDefinition u = l.load(i, data);
                //if(!overlays.contains(x))
                underlays.add(u);
            }

        }
    }

    private static void getTextureDefinitions(){
        for(int i = 0; i <   RS2_CACHE.getIndexes()[RS2Indexes.TEXTURES.getIndex()].getLastArchiveId(); i++) {
            byte[] data = RS2_CACHE.getIndexes()[RS2Indexes.TEXTURES.getIndex()].getFile(i,0);
            TextureLoader l = new TextureLoader();
            if(data != null) {
                TextureDefinition x = l.load(i, data);
                //if(!overlays.contains(x))
                textures.add(x);
            }

        }
    }

    /**
     * for testing all the textures
     */
    public static List<OverlayDefinition> defs = new ArrayList<OverlayDefinition>();
    private static void getTextureOverlay(){
        for(OverlayDefinition def : overlays){
            if(def.getTexture() > 0){
               defs.add(def);
            }
        }
    }


}
