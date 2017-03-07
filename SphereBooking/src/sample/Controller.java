package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Controller {

    @FXML private StackPane acContent;

    @FXML
    public void btnHomeOnClick() throws IOException{
        BorderPane pane = FXMLLoader.load(getClass().getResource("fxml2.fxml"));
        acContent.getChildren().setAll(pane);
    }
}
