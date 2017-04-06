package sample.members;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

public class UpgradeToBasic {

    static Connection con = null;
    static Statement stmt = null;

    public void basic(String id){

        //connect to database
        con = sample.database.DBConnection.makeConnection();
        try {
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("error connecting to db");
        }

        //ask for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText("Upgrade user to free BASIC membership?");

        //execute statement if user selects OK
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            String query = String.format("UPDATE members SET membership = 'Basic' WHERE memberId='%s'", id);
            try {
                stmt.executeUpdate(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
