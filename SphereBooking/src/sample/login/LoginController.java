package sample.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class LoginController implements Initializable{

    @FXML public Button btnClose;
    @FXML private TextField username;
    @FXML private PasswordField pass;
    @FXML private Label message;

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
    }

    @FXML
    public void clickedBtnClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void clickedForgotPassword(){
        JOptionPane.showMessageDialog(null, "Please contact the manager.");
    }

    @FXML
    public void clickedBtnLogin() {

        String user = username.getText().trim();
        String password = pass.getText().trim();
        String usertype = null;

        if(user.isEmpty() || password.isEmpty()){
            message.setTextFill(Color.RED);
            message.setText("Invalid");
        } else{

            String query = "SELECT * FROM accounts WHERE userId = '"+user+"' and pass= '"+password+"'";
            try {
                rs = pst.executeQuery(query);
                while (rs.next()) {
                    usertype = rs.getString(5);
                }

                if(usertype != null){
                    message.setTextFill(Color.GREEN);
                    message.setText("Welcome, " + user);

                    query = "DELETE FROM latestUser; INSERT into latestUser VALUES('"+user+"', '"+usertype+"');";
                    pst.executeUpdate(query);

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../layout/layout.fxml"));
                    Parent root1 = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setTitle("Sphere Booking");
                    stage.setScene(new Scene(root1, 1080, 680));
                    stage.show();

                    Stage currentStage = (Stage) btnClose.getScene().getWindow();
                    currentStage.close();
                } else {
                    message.setTextFill(Color.RED);
                    message.setText("Invalid Credentials");
                }

            } catch (Exception e) {
                e.getCause().printStackTrace();
            }
        }

    }

}
