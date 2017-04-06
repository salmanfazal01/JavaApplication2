package sample.members;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

public class UpgradeToLoyalty {

    static Connection con = null;
    static Statement stmt = null;

    public void loyalty(int sessions, String id){

        //connect to database
        con = sample.database.DBConnection.makeConnection();
        try {
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("error connecting to db");
        }

        //check if user has minimum 10 sessions
        if(sessions >= 10){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Upgrade user to LOYALTY membership?");

            //upgrade if user clicks okay
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                String query = String.format("UPDATE members SET membership = 'Loyalty' WHERE memberId='%s'", id);
                try {
                    stmt.executeUpdate(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else { //alert if less than 10 sessions
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oops");
            alert.setHeaderText(null);
            alert.setContentText("Member needs at least 10 sessions to upgrade.");
            alert.showAndWait();
        }
    }

}
