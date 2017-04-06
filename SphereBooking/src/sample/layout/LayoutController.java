package sample.layout;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LayoutController implements Initializable{

    @FXML private StackPane mainPane;

    @FXML private Button sessionsBtn;
    @FXML private Button membersBtn;
    @FXML private Button adminBtn;
    @FXML private Button btnClose;

    @FXML private ImageView btnSessions;
    @FXML private ImageView btnMembers;
    @FXML private ImageView btnAdmin;

    private String active = "-fx-text-fill: #fa3838;";
    private String inactive = "-fx-text-fill: #4E4F52;";

    static Connection con = null;
    static Statement pst = null;
    static ResultSet rs = null;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        con = sample.database.DBConnection.makeConnection();
        try{
            pst = con.createStatement();
        } catch (Exception e){
            System.out.println("error connecting to db");
        }

        try {
            BorderPane pane = FXMLLoader.load(getClass().getResource("../sessions/sessions.fxml"));
            mainPane.getChildren().setAll(pane);
            btnSessions.setImage(new Image("sample/icons/sessionred.png"));
            sessionsBtn.setStyle(active);
            checkButton();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clickedBtnClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void resetButtons(){
        btnSessions.setImage(new Image("sample/icons/session.png"));
        btnMembers.setImage(new Image("sample/icons/users.png"));
        btnAdmin.setImage(new Image("sample/icons/admin.png"));

        sessionsBtn.setStyle(inactive);
        membersBtn.setStyle(inactive);
        adminBtn.setStyle(inactive);
    }


    @FXML
    public void clickedBtnSessions() throws IOException{
        resetButtons();
        btnSessions.setImage(new Image("sample/icons/sessionred.png"));
        sessionsBtn.setStyle(active);

        BorderPane pane = FXMLLoader.load(getClass().getResource("../sessions/sessions.fxml"));
        mainPane.getChildren().setAll(pane);
    }

    @FXML
    public void clickedBtnMembers() throws IOException{
        resetButtons();
        btnMembers.setImage(new Image("sample/icons/usersred.png"));
        membersBtn.setStyle(active);

        BorderPane pane = FXMLLoader.load(getClass().getResource("../members/members.fxml"));
        mainPane.getChildren().setAll(pane);
    }

    @FXML
    public void clickedBtnAdmin() throws IOException{

        resetButtons();
        btnAdmin.setImage(new Image("sample/icons/adminred.png"));
        adminBtn.setStyle(active);

        BorderPane pane = FXMLLoader.load(getClass().getResource("../admin/admin.fxml"));
        mainPane.getChildren().setAll(pane);

    }

    private void checkButton(){

        String usertype = null;

        Button[] instructors = {adminBtn, membersBtn};
        Button[] slope = {adminBtn};

        String query = "SELECT * FROM latestUser";
        try {
            rs = pst.executeQuery(query);
            while (rs.next()) {
                usertype = rs.getString(2);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(usertype.equals("Instructor")){
            for(Button i : instructors){
                i.setDisable(true);
            }
        }
        else if(usertype.equals("Slope Operator")){
            for(Button i : slope){
                i.setDisable(true);
            }
        }

    }
}
