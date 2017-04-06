package sample.members;

import javafx.scene.control.Alert;

public class UpgradeFactory {

    //method which gets 3 parameters
    public void upgradeUser(String id, String status, int sessions){

        //if current id is loyalty, do not upgrade
        if(status.equals("Loyalty")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oops");
            alert.setHeaderText(null);
            alert.setContentText("Member already upgraded.");
            alert.showAndWait();
        }

        //if id is basic, call upgradeToLoyalty
        else if(status.equals("Basic")){
            UpgradeToLoyalty l = new UpgradeToLoyalty();
            l.loyalty(sessions, id);
        }

        //if customer is not a member, call upgradeToBasic
        else if(status.equals("None")){
            UpgradeToBasic b = new UpgradeToBasic();
            b.basic(id);
        }

        //else print error
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oops");
            alert.setHeaderText(null);
            alert.setContentText("Please select member!");
            alert.showAndWait();
        }


    }
}
