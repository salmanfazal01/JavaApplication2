package sample.members;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class MembersController implements Initializable {

    //initialize variables
    @FXML
    private TextField id, fName, lName, street, city, postCode, phone, email, sessions, membership;
    @FXML
    private ChoiceBox<String> title;
    @FXML
    private ImageView image;
    @FXML
    private Button edit, deleteBtn;

    private String newBookingID;

    private ObservableList<Members> data;
    @FXML
    private TableView<Members> table;
    @FXML
    private TableColumn<?, ?> column_id;
    @FXML
    private TableColumn<?, ?> column_name;
    @FXML
    private TableColumn<?, ?> column_membership;
    @FXML
    private TableColumn<?, ?> column_sessions;

    //initialize database variables
    static Connection con = null;
    static Statement stmt = null;
    static PreparedStatement pst = null;
    static ResultSet rs = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //connect to database
        con = sample.database.DBConnection.makeConnection();
        try {
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("error connecting to db");
        }

        //set table data
        data = FXCollections.observableArrayList();
        setCellTable();
        loadDataFromDB();

        checkButton();
    }

    //set table cells
    private void setCellTable() {
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_membership.setCellValueFactory(new PropertyValueFactory<>("membership"));
        column_sessions.setCellValueFactory(new PropertyValueFactory<>("sessions"));
    }

    //get member data from database
    private void loadDataFromDB() {
        try {
            String query2 = "SELECT memberId, fName +' '+ Lname as Name, membership, totalSessions FROM members";
            pst = con.prepareStatement(query2);
            rs = pst.executeQuery();

            while (rs.next()) {
                data.add(new Members(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(data);
    }

    //fill textboxes when table row clicked
    @FXML
    private void tableClicked() {

        Members item = table.getSelectionModel().getSelectedItem();

        try {
            String query = "SELECT * FROM members WHERE memberId = '" + item.getId() + "'";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                id.setText(rs.getString(1));
                title.setValue(rs.getString(2));
                fName.setText(rs.getString(3));
                lName.setText(rs.getString(4));
                street.setText(rs.getString(5));
                city.setText(rs.getString(6));
                postCode.setText(rs.getString(7));
                phone.setText(rs.getString(8));
                email.setText(rs.getString(9));
                membership.setText(rs.getString(10));
                sessions.setText(String.valueOf(rs.getInt(11)));

                try {
                    image.setImage(new Image(String.format("sample/icons/users/%s.jpg", rs.getString(1))));
                } catch (Exception e) {
                    image.setImage(new Image("sample/icons/users/image.jpg"));
                }
            }
        } catch (Exception e) {
            System.out.println("");
        }
    }

    //New button clicked
    @FXML
    private void newClicked() {
        //set all to default
        id.setText(calculate_id());
        title.setValue("Mr.");
        fName.setText(null);
        lName.setText(null);
        street.setText(null);
        city.setText(null);
        postCode.setText(null);
        phone.setText(null);
        email.setText(null);
        membership.setText("None");
        sessions.setText("0");

        image.setImage(new Image("sample/icons/users/image.jpg")); //reset image
    }

    //Delete Button Clicked
    @FXML
    private void deleteClicked() {

        //get table selected value, then delete data and refresh table
        Members item = table.getSelectionModel().getSelectedItem();

        TextInputDialog dialog = new TextInputDialog(item.getId());
        dialog.setTitle("Delete");
        dialog.setHeaderText("Are you sure you want to delete this booking?");
        dialog.setContentText("Booking ID: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String query2 = "DELETE FROM members WHERE memberId = '" + item.getId() + "';";
            try {
                stmt.executeUpdate(query2);

                data = FXCollections.observableArrayList();
                setCellTable();
                loadDataFromDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //update data to latest textbox items
    @FXML
    private void updateClicked() {
        if (perform_checks().equalsIgnoreCase("done")) {
            String query = String.format("UPDATE members SET title='%s', fName='%s', lName='%s', street='%s', city='%s', postCode='%s', phone='%s'" +
                            ", email='%s', membership='%s' WHERE memberId='%s'", title.getSelectionModel().getSelectedItem(), fName.getText().trim(), lName.getText().trim(),
                    street.getText().trim(), city.getText().trim(), postCode.getText().trim(), phone.getText().trim(), email.getText().trim(),
                    membership.getText().trim(), id.getText().trim());
            try {
                stmt.executeUpdate(query);

                data = FXCollections.observableArrayList();
                setCellTable();
                loadDataFromDB();
                newClicked();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(perform_checks());
            alert.showAndWait();
        }
    }

    //insert into database then refresh table
    @FXML
    private void addClicked(){
        if(perform_checks().equalsIgnoreCase("done")){
            String query = String.format("INSERT INTO members VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', 0)",
                    id.getText().trim(), title.getSelectionModel().getSelectedItem(), fName.getText().trim(), lName.getText().trim(),
                    street.getText().trim(), city.getText().trim(), postCode.getText().trim(), phone.getText().trim(), email.getText().trim(),
                    membership.getText().trim());
            try {
                stmt.executeUpdate(query);

                data = FXCollections.observableArrayList();
                setCellTable();
                loadDataFromDB();
                newClicked();

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Insert Failed! User may already exist.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(perform_checks());
            alert.showAndWait();
        }
    }

    //upgrade membership
    @FXML
    private void upgradeClicked(){
        String memberType = "";
        int totalSessions = 0;
        try{
            String query = "SELECT membership, totalSessions FROM members WHERE memberId='"+id.getText()+"'";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                memberType = rs.getString(1);
                totalSessions = Integer.parseInt(rs.getString(2));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        UpgradeFactory upgradeFactory = new UpgradeFactory();
        upgradeFactory.upgradeUser(id.getText(), memberType, totalSessions);

    }


    //select image
    @FXML
    private void editClicked() throws Exception{
        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {

            URL url = file.toURI().toURL();
            image.setImage(new Image(url.toExternalForm()));

        }


    }

    //check if no fields are empty
    private String perform_checks() {

        if (id.getText().trim().isEmpty() || fName.getText().trim().isEmpty() || lName.getText().trim().isEmpty() || street.getText().trim().isEmpty()
                || city.getText().trim().isEmpty() || postCode.getText().trim().isEmpty() || phone.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {

            return "Fill in everything!";
        }

        return "done";
    }

    //calculate new booking id
    private String calculate_id() {
        try {
            String query = "SELECT memberId FROM members";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                newBookingID = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int temp = Integer.parseInt(newBookingID) + 1096;

        return (Integer.toString(temp));
    }

    //disable delete if slope operator
    private void checkButton(){

        String userType = "";

        Button[] slope = {deleteBtn};

        String query3 = "SELECT * FROM latestUser";

        try {
            rs = stmt.executeQuery(query3);
            while (rs.next()) {
                userType = rs.getString(2);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(userType.equals("Slope Operator")){
            for(Button i : slope){
                i.setDisable(true);
            }
        }

    }

}
