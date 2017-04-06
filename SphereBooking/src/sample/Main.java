package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login/login.fxml"));
        primaryStage.setTitle("Sphere Booking");
        primaryStage.setScene(new Scene(root, 367, 354));
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //primaryStage.setScene(new Scene(root, 1080, 680));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
