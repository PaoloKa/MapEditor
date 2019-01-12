package main;

import com.rs.cache.RS2Indexes;
import com.rs.cache.definitions.*;
import com.rs.cache.loaders.LocationLoader;
import com.rs.cache.loaders.MapLoader;
import com.rs.cache.saver.LocationSaver;
import com.rs.cache.saver.MapSaver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public GridPane grid_landscape;
    public TextArea console;
    public ChoiceBox dropdown_mode;
    public TextField settings;
    public TextField height;
    public TextField underlayId;
    public TextField overlayId;
    public Pane pane;
    public Button packmap;
    public Button getmap_button;
    public TextField overlayrotation;
    public TextField overlaypath;
    public TextField new_overlayId;
    public TextField old_overlayId;
    public TextField height_paint;
    public TextField underlay_paint;
    public TextField overlay_paint;
    public TextField attrcode;
    public GridPane overlay_grid;
    public GridPane underlay_grid;
    public TextField new_underlay;
    public TextField old_underlay;
    public GridPane grid_object;
    public TextField paint_mask;
    public TextField paint_settings;
    public TextField paint_path;
    public CheckBox type_22;
    public CheckBox type_10;
    public CheckBox type_roof;
    public CheckBox type_wall;
    public CheckBox delete_onclick;
    public TextField y_clean;
    public TextField x_clean;
    public TextField object_id;
    public TextField object_type;
    public TextField object_rot;
    DropShadow shadow = new DropShadow();
    private String mode = "Select Mode";
    private MapDefinition.Tile selectedTile = null;
    private Location selectedLocation = null;
    private int[] coords = new int[2];

    /**
     * when the gui gets started
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        writeOutput("Starting editor.");
        getMapData(Main.map_archiveId, Main.object_archiveId);
        loadMap();
        loadObjects();
        populateOverlayColors();
        populatUnderlayColors();
        ObservableList<String> options = FXCollections.observableArrayList(
                "Select mode",
                "Edit mode",
                "Click",
                "Brush"
        );
        dropdown_mode.setItems(options);
        dropdown_mode.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String newValue) {
                mode = newValue;
            }
        });

        dropdown_mode.getSelectionModel().selectFirst();
        pane.setStyle("-fx-border-color: black");
        grid_landscape.setStyle("-fx-border-color: black");
        writeOutput("Editor launched successfully.");
    }

    public void populateOverlayColors(){
        writeOutput("Loading overlay colors.");
        overlay_grid.setVgap(1);
        for(int i = 0; i < Main.overlays.size(); i ++) {
            final Button button = new Button();
            button.setMinWidth(200);
            button.setMinHeight(25);

            button.setMaxHeight(25);
            button.setMaxWidth(200);
            button.setText("Id: "+i);
            button.setStyle("-fx-text-fill: green");
            int rgb = Main.overlays.get(i).getRgbColor();
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue =  rgb & 0xFF;
            button.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
            overlay_grid.add(button, 0,i );
            final Button button2 = new Button();
            button2.setMinWidth(200);
            button2.setMinHeight(25);

            button2.setMaxHeight(25);
            button2.setMaxWidth(200);
            button2.setText("Id: "+i);
            button2.setStyle("-fx-text-fill: green");
            rgb = Main.overlays.get(i).getSecondaryRgbColor();
            red = (rgb >> 16) & 0xFF;
            green = (rgb >> 8) & 0xFF;
            blue =  rgb & 0xFF;
            button2.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
            overlay_grid.add(button2, 1,i );

            if(Main.overlays.get(i).getTexture() > 0) {
                final Button button3 = new Button();
                button3.setMinWidth(200);
                button3.setMinHeight(25);

                button3.setMaxHeight(25);
                button3.setMaxWidth(200);
                button3.setText("Id: " + i);
                button3.setStyle("-fx-text-fill: green");
                rgb = Main.textures.get(Main.overlays.get(i).getTexture() - 1).textureRGB;
                red = (rgb >> 16) & 0xFF;
                green = (rgb >> 8) & 0xFF;
                blue = rgb & 0xFF;
                button3.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
                overlay_grid.add(button3, 3, i);
            }
        }
    }

    public void populatUnderlayColors(){
        writeOutput("Loading underlay colors.");
       underlay_grid.setVgap(1);
        for(int i = 0; i < Main.underlays.size(); i ++) {
            final Button button = new Button();
            button.setMinWidth(200);
            button.setMinHeight(25);

            button.setMaxHeight(25);
            button.setMaxWidth(200);
            button.setText("Id: "+i);
            button.setStyle("-fx-text-fill: green");
            int rgb = Main.underlays.get(i).getCollor();
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue =  rgb & 0xFF;
            button.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
            underlay_grid.add(button, 0,i );
        }
    }

    public void getMapData(int mapId, int objectId){
        writeOutput("Loading mapfile: "+mapId);
        grid_landscape.getChildren().clear();
        MapLoader l = new MapLoader();
        Main.map_archiveId = mapId;
        Main.object_archiveId = objectId;
        Main.loadedMap = l.load(1,1, Main.RS2_CACHE.getIndexes()[RS2Indexes.LANDSCAPES.getIndex()].getFile(mapId,0));
        writeOutput("Loading objectfile: "+objectId);
        grid_object.setVgap(0);
        grid_object.setHgap(0);
        byte[] objectlayer = Main.RS2_CACHE.getIndexes()[RS2Indexes.LANDSCAPES.getIndex()].getFile(objectId, 0);
        LocationLoader loader = new LocationLoader();
        Main.loadedObjects = loader.load(1,1, objectlayer);
    }

    private void loadMap(){
        grid_landscape.getChildren().clear();
        for(int x = 0; x < 64; x ++){
            for(int y = 0; y < 64; y++ ){
                final MapDefinition.Tile tile = Main.loadedMap.getTiles()[0][x][y];
                if((tile.underlayId & 0xFF) >= Main.underlays.size())
                    tile.underlayId = (byte) (Main.underlays.size() -1);
                final OverlayDefinition overlay = Main.overlays.get((((tile.overlayId) & 0xFF)));
                final UnderlayDefinition underlay = Main.underlays.get(tile.underlayId & 0xFF);
                final Button button = new Button();
                button.setMinWidth(8);
                button.setMinHeight(8);

                button.setMaxHeight(8);
                button.setMaxWidth(8);

                int rgb = (underlay.getCollor());
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue =  rgb & 0xFF;
                if(tile.underlayId != 0)
                  button.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));

                /**
                 * overlays
                 */
                if(overlay.getTexture() >= 0){
                    rgb = (Main.textures.get(overlay.getTexture() & 0xFF).textureRGB);
                    red = (rgb >> 16) & 0xFF;
                    green = (rgb >> 8) & 0xFF;
                    blue = rgb & 0xFF;
                    button.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
                } else  if ((tile.overlayId  & 0xFF) > 0) {
                        //rgb = overlay.getRgbColor();
                       int index = (tile.overlayId) & 0xFF;
                       rgb = Main.overlays.get(index -1).getRgbColor();

                        red = (rgb >> 16) & 0xFF;
                        green = (rgb >> 8) & 0xFF;
                        green += green / 5;
                        blue = rgb & 0xFF;
                        try {
                            button.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
                        } catch (Exception ex){
                                //can something go wrong
                        }

                } if (overlay.getRgbColor() == 0xFF_00F && overlay.getSecondaryRgbColor() > 0){
                    rgb = overlay.getSecondaryRgbColor();
                    red = (rgb >> 16) & 0xFF;
                    green = (rgb >> 8) & 0xFF;
                    blue = rgb & 0xFF;
                    button.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));


                }

                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Node source = (Node)event.getSource() ;
                        Integer colIndex = GridPane.getColumnIndex(source);
                        Integer rowIndex = GridPane.getRowIndex(source);
                        setValues(tile);
                        coords[0] = rowIndex;
                        coords[1] = colIndex;
                        writeOutput("OverlayId: "+tile.overlayId+" UnderlayId: "+tile.underlayId+" Height:"+tile.height+ " [x:"+rowIndex+" y:"+colIndex+"] TextureId:"+overlay.getTexture());
                        if(mode == "Click"){
                            if(!overlay_paint.getText().isEmpty()) {
                                Main.loadedMap.getTiles()[0][coords[0]][coords[1]].overlayId = (byte) Integer.parseInt(overlay_paint.getText());
                                Main.loadedMap.getTiles()[0][coords[0]][coords[1]].attrOpcode = (byte) 2;
                            }if(!underlay_paint.getText().isEmpty())
                                Main.loadedMap.getTiles()[0][coords[0]][coords[1]].underlayId = (byte) Integer.parseInt(underlay_paint.getText());
                            if(!height_paint.getText().isEmpty())// otherwise keep same height
                                Main.loadedMap.getTiles()[0][coords[0]][coords[1]].height = Integer.parseInt(height_paint.getText());
                            if(!paint_mask.getText().isEmpty()){
                                Main.loadedMap.getTiles()[0][coords[0]][coords[1]].shape = (byte) Integer.parseInt(paint_mask.getText());
                            }
                            if(!paint_settings.getText().isEmpty()){
                                Main.loadedMap.getTiles()[0][coords[0]][coords[1]].mask = (byte) Integer.parseInt(paint_settings.getText());
                            }
                            if( Main.loadedMap.getTiles()[0][coords[0]][coords[1]].overlayId  != 0) {
                               int rgb = Main.overlays.get(Main.loadedMap.getTiles()[0][coords[0]][coords[1]].overlayId & 0xFF ).getRgbColor();
                               int  red = (rgb >> 16) & 0xFF;
                               int   green = (rgb >> 8) & 0xFF;
                               int   blue = rgb & 0xFF;
                                button.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
                            }

                        }
                    }
                });
                button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                Node source = (Node)e.getSource() ;
                                Integer colIndex = GridPane.getColumnIndex(source);
                                Integer rowIndex = GridPane.getRowIndex(source);
                               // setValues(tile);
                                coords[0] = rowIndex;
                                coords[1] = colIndex;
                                if(mode == "Brush" && Main.p_pressed){
                                    if(!overlay_paint.getText().isEmpty()) {
                                        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].overlayId = (byte) Integer.parseInt(overlay_paint.getText());
                                        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].attrOpcode = (byte) 2;
                                    }if(!underlay_paint.getText().isEmpty()) {
                                        if(Integer.parseInt(overlay_paint.getText()) == 0)
                                            Main.loadedMap.getTiles()[0][coords[0]][coords[1]].attrOpcode = (byte) 0;
                                        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].underlayId = (byte) Integer.parseInt(underlay_paint.getText());
                                    } if(!height_paint.getText().isEmpty())// otherwise keep same height
                                        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].height = Integer.parseInt(height_paint.getText());
                                    if(!paint_path.getText().isEmpty()){
                                        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].shape = (byte) Integer.parseInt(paint_path.getText());
                                    }
                                    if(!paint_mask.getText().isEmpty()){
                                        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].mask = (byte) Integer.parseInt(paint_mask.getText());
                                    }
                                    if(!paint_settings.getText().isEmpty()){
                                        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].attrOpcode = (byte) Integer.parseInt(paint_settings.getText());
                                    }
                                    if( Main.loadedMap.getTiles()[0][coords[0]][coords[1]].overlayId  != 0) {
                                        int rgb = Main.overlays.get(Main.loadedMap.getTiles()[0][coords[0]][coords[1]].overlayId & 0xFF -1 ).getRgbColor();
                                        int  red = (rgb >> 16) & 0xFF;
                                        int   green = (rgb >> 8) & 0xFF;
                                        int   blue = rgb & 0xFF;
                                        button.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));
                                    }
                                } else if(mode == "Select" || mode == "Edit")
                                    button.setEffect(shadow);
                            }
                        });

                button.addEventHandler(MouseEvent.MOUSE_EXITED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                button.setEffect(null);
                            }
                        });

                grid_landscape.add(button, y, x);


            }
        }
        grid_landscape.setRotate(270);
    }

    public void packMap(MouseEvent mouseEvent) {
        packmap.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Main.RS2_CACHE.getIndexes()[RS2Indexes.LANDSCAPES.getIndex()].putFile(Main.map_archiveId,0, MapSaver.getData(Main.loadedMap));
                Main.RS2_CACHE.getIndexes()[RS2Indexes.LANDSCAPES.getIndex()].putFile(Main.object_archiveId,0, LocationSaver.getData(Main.loadedObjects));
                writeOutput("Packed map to cache." +Main.map_archiveId);
            }
        });
    }

    public void getMap(MouseEvent mouseEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Regio finder");
        dialog.setHeaderText("Enter the coords you would like to edit.  x,y");
        dialog.setContentText("Please enter your coords:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String[] coords_txt = result.get().split(",");
            int archiveId = Utils.getArchiveId(Integer.parseInt(coords_txt[0]),Integer.parseInt(coords_txt[1]));
            int objectLayer = Utils.getMapArchiveId(Integer.parseInt(coords_txt[0]),Integer.parseInt(coords_txt[1]));
            writeOutput("Archives loaded: m:"+ archiveId+ " o:"+objectLayer);

            getMapData(archiveId, objectLayer);
            loadMap();
            loadObjects();
        }
    }

    private void preloadObjectsGrid(){
        for(int x = 0; x < 64; x ++){
            for(int y = 0; y < 64; y++ ) {
                Button button = new Button();
                button.setMaxSize(8,8);
                button.setMinSize(8,8);
                grid_object.add(button, x, y);
                }
            }
    }
    /**
     * loads all the objects to the grid
     *  0 = top
     *  1 = down
     *  2 = right
     *  3 = left;
     */
    private void loadObjects() {
        grid_object.getChildren().clear();
        writeOutput("Drawing object map");
        preloadObjectsGrid();
        for(Location l : Main.loadedObjects.getLocations()){
            if(l.getPosition().getZ() >0) // only for begin will do this after
                continue;
        Button button = new Button();
        button.setMaxSize(8,8);
        button.setMinSize(8,8);
        if(l.getType() == 10 && !type_10.isSelected())
            continue;
        if(l.getType() == 22 && !type_22.isSelected())
            continue;
        if((l.getType() == 0 || l.getType() == 2 || l.getType() == 9) && !type_wall.isSelected())
            continue;
        if(l.getType() >= 12 && l.getType() < 22 && !type_roof.isSelected())
            continue;
       if(l.getType() == 10)
            button.setStyle("-fx-background-color : cornflowerblue;");
        else if(l.getType() == 22)
            button.setStyle("-fx-background-color : GOLDENROD" );
        else if(l.getType() == 11)
           button.setStyle("-fx-background-color : GREEN;");
        if(l.getType() == 0 || l.getType() == 2) {
            if (l.getOrientation() == 3)
                button.setStyle("-fx-border-color : black; -fx-background-color: transparent; -fx-border-width : 1 0 0 0");
            else if (l.getOrientation() == 1)
                button.setStyle("-fx-border-color : black; -fx-background-color: transparent;  -fx-border-width : 0 0 1 0");
            else if (l.getOrientation() == 2)
                button.setStyle("-fx-border-color : black; -fx-background-color: transparent;  -fx-border-width : 0 1 0 0");
            else if (l.getOrientation() == 0)
                button.setStyle("-fx-border-color : black; -fx-background-color: transparent; -fx-border-width : 0 1 0 0");
           // grid_object.add(button, l.getPosition().getX(), l.getPosition().getY());
        }
        if(l.getType() == 9 ){ // type 1 is connector
            if (l.getOrientation() == 3) {
                button.setStyle("-fx-border-color : black; -fx-background-color: transparent; -fx-border-width : 1 0 0 0");
                button.setRotate(-225);
            }else if (l.getOrientation() == 0) {
                button.setStyle("-fx-border-color : black; -fx-background-color: transparent;  -fx-border-width : 0 0 1 0");
                button.setRotate(225);
            }else if (l.getOrientation() == 2) {
                button.setStyle("-fx-border-color : black; -fx-background-color: transparent;  -fx-border-width : 0 1 0 0");
                button.setRotate(-225);
            }else if (l.getOrientation() == 1) {
                button.setStyle("-fx-border-color : black; -fx-background-color: transparent; -fx-border-width : 0 1 0 0");
                button.setRotate(225);
            }

        }

            /**
             * clicking
             */
        button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Node source = (Node)event.getSource() ;
                    Integer colIndex = GridPane.getColumnIndex(source);
                    Integer rowIndex = GridPane.getRowIndex(source);
                    coords[0] = rowIndex;
                    coords[1] = colIndex;
                    writeOutput("objectId: "+l.getId()+" type: "+l.getType()+" rotation: "+l.getOrientation()+ " "+l.getPosition());
                    setObjectValues(l);
                    if(delete_onclick.isSelected()){
                        writeOutput("Removed object :"+l.getId());
                        Main.loadedObjects.getLocations().remove(l);
                        button.setStyle(null);
                    }
                }
            });
        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {

                        }
        });
        grid_object.add(button, l.getPosition().getX(), l.getPosition().getY());
        }

    }

    private void setObjectValues(Location l) {
        selectedLocation = l;
        object_id.setText(l.getId()+"");
        object_rot.setText(l.getOrientation()+"");
        object_type.setText(l.getType()+"");
    }

    public  void writeOutput(String input){
        console.appendText("["+ LocalDateTime.now()+"] "+input+" \n");
    }



    private void setValues(MapDefinition.Tile tile) {
        selectedTile = tile;
        height.setText(tile.height+"");
        settings.setText(tile.mask +"");
        underlayId.setText((tile.underlayId & 0xFF) +"");
        overlayId.setText((tile.overlayId & 0xFF)+"");
        overlayrotation.setText(tile.overlayRotation+"");
        overlaypath.setText(tile.shape +"");
        attrcode.setText(tile.attrOpcode+"");
    }



    public void saveEdit(MouseEvent mouseEvent) {
        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].underlayId = (byte) Integer.parseInt(underlayId.getText());
        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].overlayId = (byte) Integer.parseInt(overlayId.getText());
        Main.loadedMap.getTiles()[0][coords[0]][coords[1]].attrOpcode = (byte) Integer.parseInt(attrcode.getText());
        writeOutput("Saved tile ["+coords[0]+","+coords[1]+"]");

    }

    public void changeOverlay(MouseEvent mouseEvent) {
        int newId = Integer.parseInt(new_overlayId.getText());
        int oldId = Integer.parseInt(old_overlayId.getText());
        int counter = 0;
        for(int x = 0; x < 64; x ++){
            for(int y = 0; y < 64; y++ ) {
                if(Main.loadedMap.getTiles()[0][x][y].overlayId == oldId) {
                    Main.loadedMap.getTiles()[0][x][y].overlayId = (byte) newId;
                    if(oldId == 0)
                        Main.loadedMap.getTiles()[0][x][y].attrOpcode = (byte) 2;
                    counter++;
                }
            }
        }
    writeOutput("Replaced "+counter+ " overlays.");
    }

    /**
     * puts all the textured overlays in one line on the map
     * @param mouseEvent
     */
    public void dumpMap(MouseEvent mouseEvent) throws IOException, InterruptedException {
        writeOutput("Dumping data");
        byte[] landscape = MapSaver.getData(Main.loadedMap);

        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\paolo\\Desktop\\MapEditor\\previewdump\\0.dat")) {
            try {
                fos.write(landscape);
                fos.close();
            } catch (IOException e) {


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] objectlayer = Main.RS2_CACHE.getIndexes()[5].getFile(Main.object_archiveId, 0);
        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\paolo\\Desktop\\MapEditor\\previewdump\\1.dat")) {
            try {
                fos.write(objectlayer);
                fos.close();
            } catch (IOException e) {


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writeOutput("Opening viewer");
      //Process ps = Runtime.getRuntime().exec("java -jar C:\\Users\\paolo\\Desktop\\MapEditor\\Mapeditor.jar -Xmx8096"); //to buggy for now
       //System.out.println(p.isAlive()+" ");
       //p.waitFor();





    }

    /**
     * changes all the overlays of a certain type to another one
     * @param mouseEvent
     */
    public void changeUnderlay(MouseEvent mouseEvent) {
        int newId = Integer.parseInt(new_underlay.getText());
        int oldId = Integer.parseInt(old_underlay.getText());
        int counter = 0;
        for(int x = 0; x < 64; x ++){
            for(int y = 0; y < 64; y++ ) {
                if((Main.loadedMap.getTiles()[0][x][y].underlayId  & 0xFF) == oldId) {
                    Main.loadedMap.getTiles()[0][x][y].underlayId = (byte) newId;
                    counter++;
                }
            }
        }
        writeOutput("Replaced "+counter+ " underlays.");
    }

    /**
     * reloading the map after object view changed
     * @param mouseEvent
     */
    public void reloadMap(MouseEvent mouseEvent) {
        loadObjects();
    }

    public void removeArea(MouseEvent mouseEvent) {
        String[] xvalues = x_clean.getText().split(",");
        String[] yvalues = y_clean.getText().split(",");
        int x1 = Integer.parseInt(xvalues[0]);
        int x2 = Integer.parseInt(xvalues[1]);
        int y1 = Integer.parseInt(yvalues[0]);
        int y2 = Integer.parseInt(yvalues[1]);
        int total = 0;
        for (Iterator<Location> iterator = Main.loadedObjects.getLocations().iterator(); iterator.hasNext(); ) {
            Location location = iterator.next();
            if(location.getPosition().getY() >= y1 && location.getPosition().getY() <= y2 && location.getPosition().getX() >= x1 && location.getPosition().getX() <= x2){
                iterator.remove();
                total++;
            }
        }
        loadObjects();
        writeOutput("Total objects removed: " +total);
    }

    public void saveObject(MouseEvent mouseEvent) {
        int index = Main.loadedObjects.getLocations().indexOf(selectedLocation);
        Main.loadedObjects.getLocations().get(index).setId(Integer.parseInt(object_id.getText()));
        writeOutput("Object saved");
        loadObjects();

    }

    /**
     * gets all the objects from the map, packs them into your cache and changes the id in the file
     * @param mouseEvent
     */
    public void packOsrsMap(MouseEvent mouseEvent) {
        try {
            DataPacker.readLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
