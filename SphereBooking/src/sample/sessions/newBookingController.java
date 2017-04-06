package sample.sessions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;


public class newBookingController implements Initializable {

    @FXML Button btnClose;
    @FXML TextField payment, bookingIdTextField, membersTextField = null;
    @FXML CheckBox paymentCheckBox;
    @FXML Label statusLabel;
    @FXML ChoiceBox instructorChoiceBox;
    @FXML ChoiceBox<String> sessionType;
    @FXML Spinner startTime, endTime;
    @FXML DatePicker bookingDate;

    static Connection con = null;
    static Statement stmt = null;
    static PreparedStatement pst = null;
    static ResultSet rs = null;

    private String newBookingID;

    private ObservableList<String> members;
    private ObservableList<String> instructors;

    public ObservableList<String> getInstructors() {
        return instructors;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = sample.database.DBConnection.makeConnection();
        try{
            stmt = con.createStatement();
        } catch (Exception e){
            System.out.println("error connecting to db");
        }

        members = FXCollections.observableArrayList();
        instructors = FXCollections.observableArrayList();

        loadMembers();
        setBookingID();
        checkPayment();
    }

    @FXML
    public void btnCancelClicked() {
        Stage currentDateStage = (Stage) btnClose.getScene().getWindow();
        currentDateStage.close();
    }

    @FXML
    private void checkPayment() {
        if (paymentCheckBox.isSelected() == true) {
            payment.setText("PAID");
        }
        else {
            float standard = 0;
            float training = 0;
            float discount = 0;
            String memberType = "";

            String query = "SELECT * FROM fees";
            try {
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();
                while (rs.next()) {
                    standard = Float.parseFloat(rs.getString(1));
                    training = Float.parseFloat(rs.getString(2));
                    discount = Float.parseFloat(rs.getString(3));
                }

                query = "SELECT membership FROM members WHERE memberId='"+membersTextField.getText()+"'";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();
                while(rs.next()){
                    memberType = rs.getString(1);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

            if(memberType.equals("None")){
                if(sessionType.getSelectionModel().getSelectedItem().toString().trim().equals("Standard")){
                    payment.setText(Float.toString(standard));
                } else{
                    payment.setText(Float.toString(training));
                }
            }
            else if(memberType.equals("UpgradeToBasic")){
                if(sessionType.getSelectionModel().getSelectedItem().toString().trim().equals("Standard")){
                    payment.setText(Float.toString(standard));
                } else{
                    payment.setText(Float.toString(training));
                }
            }
            else if(memberType.equals("UpgradeToLoyalty")){
                if(sessionType.getSelectionModel().getSelectedItem().toString().trim().equals("Standard")){
                    payment.setText(Float.toString(standard - (standard*discount/100)));
                } else{
                    payment.setText(Float.toString(training - (training*discount/100)));
                }
            }
            else{
                System.out.println(memberType);
            }
        }
    }

    private void loadMembers() {
        try {
            String query = "SELECT * FROM members";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                members.add(rs.getString(1));
            }

            query = "SELECT userId+'- '+fName FROM accounts WHERE userType='instructor'";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            instructors.add("None");
            while (rs.next()) {
                instructors.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextFields.bindAutoCompletion(membersTextField, members);
        instructorChoiceBox.getItems().setAll(instructors);

    }

    private void setBookingID(){
        try {
            String query = "SELECT bookingId FROM bookings";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                newBookingID = rs.getString(1);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        int temp = Integer.parseInt(newBookingID)+1;
        bookingIdTextField.setText(Integer.toString(temp));
    }

    @FXML
    public void btnBookClicked(){
        java.util.Date currentDate = null;
        java.util.Date inputDate = null;

        //Check if any field is empty
        if(membersTextField.getText().toString().trim().isEmpty() || bookingDate.getValue() == null){
            statusLabel.setStyle("-fx-text-fill: red");
            statusLabel.setText("Please fill in required information");
        }
        else {

            //convert input date and currentDate date to Date format
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String a = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String b = bookingDate.getValue().toString();
            try {
                currentDate = format.parse(a);
                inputDate = format.parse(b);
            } catch (Exception e){
                e.printStackTrace();
            }

            //check if input date is not before currentDate date
            if(currentDate.after(inputDate)){
                System.out.println(currentDate + " " + inputDate);
                statusLabel.setStyle("-fx-text-fill: red");
                statusLabel.setText("Invalid date entered");
            }

            //check if member entered is not in list of all Members
            else if(!members.contains(membersTextField.getText().trim())){
                statusLabel.setStyle("-fx-text-fill: red");
                statusLabel.setText("Member not found. Please add first");
            }

            //check if end time is greater than start time
            else if(Integer.parseInt(startTime.getValue().toString()) >= Integer.parseInt(endTime.getValue().toString())) {
                statusLabel.setStyle("-fx-text-fill: red");
                statusLabel.setText("Invalid time entered");
            }

            //if all correct, insert into database
            else {
                String query = String.format("INSERT INTO bookings VALUES('%s', '%s', '%s', '%s', '%s', '%s')", bookingIdTextField.getText(),
                        membersTextField.getText(), instructorChoiceBox.getSelectionModel().getSelectedItem(), sessionType.getSelectionModel().getSelectedItem(),
                        bookingDate.getValue().toString() + ' ' + startTime.getValue().toString() + ' ' + endTime.getValue().toString(), payment.getText());
                try{
                    stmt.executeUpdate(query);

                    Stage currentDateStage = (Stage) btnClose.getScene().getWindow();
                    currentDateStage.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }


    }

}
