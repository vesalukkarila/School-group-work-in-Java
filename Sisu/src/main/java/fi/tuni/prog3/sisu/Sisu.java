package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX Sisu
 */
public class Sisu extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        
        //Creating a new BorderPane.
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        
        //Adding HBox to the center of the BorderPane.
        root.setCenter(getCenterHbox());
        
        //Adding button to the BorderPane and aligning it to the right.
        var quitButton = getQuitButton();
        BorderPane.setMargin(quitButton, new Insets(10, 10, 0, 10));
        root.setBottom(quitButton);
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);
        
        Scene scene = new Scene(root, 800, 500);                      
        stage.setScene(scene);
        stage.setTitle("SisuGUI");
        
        
        VBox left = (VBox) root.lookup("#leftbox");
        VBox right = (VBox) root.lookup("#rightbox");
        Label infoLabel = (Label) root.lookup("#infoLabel");
        Button chooseButton = (Button) root.lookup("#chooseButton");
        
        ChoiceBox choiceBox = new ChoiceBox();
        choiceBox.setId("choicebox");
        left.getChildren().add(choiceBox);
        
        
        //Fetching all degreeprogrammes from Sisu-api
        ArrayList<ActualDegreeModule> degreeList = RootSearch.allDegreeProgrammes();       
        
        
        for (var degmod : degreeList) {                                            
            choiceBox.getItems().add(degmod.getName());
        }
        
        
        
        
        
        
        
        
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private HBox getCenterHbox() {
        //Creating an HBox.
        HBox centerHBox = new HBox(10);
        
        //Adding two VBox to the HBox.
        centerHBox.getChildren().addAll(getLeftVBox(), getRightVBox());
        
        return centerHBox;
    }
    
    
    
    private VBox getLeftVBox() {                
        //Creating a VBox for the left side.
        VBox leftVBox = new VBox();
        leftVBox.setId("leftbox");      
        leftVBox.setPrefWidth(380);
        leftVBox.setStyle("-fx-background-color: #8fc6fd;");
        leftVBox.getChildren().add(new Label("Left Panel"));

        Button chooseButton = new Button("Choose DegreeProgramme");
        chooseButton.setId("chooseButton");
        
        Label infoLabel = new Label("");
        infoLabel.setId("infoLabel");
        
        leftVBox.getChildren().addAll(chooseButton, infoLabel);
        
        return leftVBox;
    }
    
      private VBox getRightVBox() {
        //Creating a VBox for the right side.
        VBox rightVBox = new VBox();
        rightVBox.setId("rightbox");    
        rightVBox.setPrefWidth(380);
        rightVBox.setStyle("-fx-background-color: #b1c2d4;");
        
        rightVBox.getChildren().add(new Label("Right Panel"));
        
        return rightVBox;
    }
    
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