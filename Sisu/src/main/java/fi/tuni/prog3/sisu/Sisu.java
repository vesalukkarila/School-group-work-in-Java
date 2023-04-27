package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX Sisu
 */
public class Sisu extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();
        ChoiceBox choiceBox = new ChoiceBox<>();
        
        ArrayList<ActualDegreeModule> degreeList = RootSearch.allDegreeProgrammes();
        
        for (var degmod : degreeList) {                                            
            choiceBox.getItems().add(degmod.getName());
        }
        
        //Left tab
        Tab tabLeft = new Tab("Choose a Degree Programme");
        tabLeft.setClosable(false);
        Button button1 = getQuitButton();
        VBox tab1Content = new VBox(new Label("Choose a degree:"), choiceBox, button1);
        
        
        tab1Content.setSpacing(10);
        VBox.setVgrow(tab1Content, Priority.ALWAYS);
        HBox bottomBox1 = new HBox(button1);
        bottomBox1.setAlignment(Pos.CENTER_RIGHT);
        bottomBox1.setPadding(new Insets(10));
        VBox.setVgrow(bottomBox1, Priority.NEVER);
        VBox vbox1 = new VBox(tab1Content, bottomBox1);
        tabLeft.setContent(vbox1);
        
        //Right tab
        Tab tabRight = new Tab("View chosen degree");
        
        tabRight.setClosable(false);
        TreeView tree = new TreeView();
        tree.setId("emptytree");
        VBox tab2Content = new VBox(tree);
        
        tab2Content.setId("tab2content");
        tab2Content.setSpacing(10);
        VBox.setVgrow(tab2Content, Priority.ALWAYS);
        HBox bottomBox2 = bottomBox1;              
        bottomBox2.setAlignment(Pos.CENTER_RIGHT);
        bottomBox2.setPadding(new Insets(10));
        VBox.setVgrow(bottomBox2, Priority.NEVER);
        VBox vbox2 = new VBox(tab2Content, bottomBox2);
        tabRight.setContent(vbox2);
        
        
        tabLeft.setStyle("-fx-background-color: rgb(45, 214, 169)");
        tabRight.setStyle("-fx-background-color: rgb(100, 232, 197)");
        button1.setStyle("-fx-background-color: rgb(237, 84, 74)");
        
        
        tabPane.getTabs().addAll(tabLeft, tabRight);
        root.setCenter(tabPane);
        VBox bottomBox = new VBox(bottomBox1);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        root.setBottom(bottomBox);
        Scene scene = new Scene(root, 400, 400);
        
        choiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                                
                int selectedIndex = choiceBox.getSelectionModel().getSelectedIndex();
                ActualDegreeModule module = degreeList.get(selectedIndex);
               
                //Building url
                String groupId = module.getGroupId();                         
                URL url = null;
                
                try {
                    url = getUrl(groupId);
                } catch (MalformedURLException ex) {                              
                    java.util.logging.Logger.getLogger(Sisu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                
                try {
                    //Fetching all module-objects from Sisu API for the chosen degree
                    DegreeModule degree = iAPI.findModules(url);                 
                    
                    //Running through the degree-structure
                    TreeItem rootItem = treeItemsRecursive(degree);
                    
                    TreeView tree = (TreeView) root.lookup("#emptytree");
                    tree.setRoot(rootItem);
                    
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Sisu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }   
            }
        });
        
        stage.setScene(scene);
        
        stage.show();
    }

        /**
     * A depth-first algorithm that runs through degree-structure and 
     * creates tree-items along the way which are then added to parent-item
     * @param module Degreemodule, baseclass of all used classes
     * @return TreeItem
     */
    private TreeItem treeItemsRecursive (DegreeModule module) {       
        
        TreeItem currentItem = null;
        
        if (module.getType().equals("course")) {
             currentItem = new TreeItem(module.getName() + ", credits: " + module.getMinCredits() );  
        }
        else 
            currentItem = new TreeItem(module.getName());
        
        for ( var node : module.getArrayList()) {
            currentItem.getChildren().add(treeItemsRecursive(node));
        }
        return currentItem;
    }

    
        /**
     * Builds a url-address for ActualDegreeModule
     * @param groupId String for individual groupid
     * @return url-address
     * @throws MalformedURLException 
     */
    private URL getUrl (String groupId) throws MalformedURLException {
        URL url = null;
        if (groupId.startsWith("otm")) {
            String beginning = "https://sis-tuni.funidata.fi/kori/api/modules/";
            String address = beginning + groupId;    
            url = new URL(address);
            
        }                             
               
        else {
            String beginning = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId=";
            String ending = "&universityId=tuni-university-root-id";
            String address = beginning + groupId + ending;
            url = new URL(address);    
        } 

        return url;
    }

    
    public static void main(String[] args) {
        launch();
    }
    
    
    /**
     * Creates a quit-button
     * @return a quit button
     */
    private Button getQuitButton() {
        //Creating a button.
        Button button = new Button("Quit");
        
        //Adding an event to the button to terminate the application.
        button.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        
        return button;
    }
}