package sample.sessions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;


public class updateBookingController implements Initializable {

    @FXML
    Button btnClose;
    @FXML
    TextField payment, bookingIdTextField, membersTextField = null;
    @FXML
    CheckBox paymentCheckBox;
    @FXML
    Label statusLabel;
    @FXML
    ChoiceBox instructorChoiceBox;
    @FXML
    ChoiceBox<String> sessionType;
    @FXML
    Spinner startTime, endTime;
    @FXML
    DatePicker bookingDate;

    static Connection con = null;
    static Statement stmt = null;
    static PreparedStatement pst = null;
    static ResultSet rs = null;

    private ObservableList<String> instructors;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = sample.database.DBConnection.makeConnection();
        try {
            stmt = con.createStatement();
        } catch (Exception e) {
            System.out.println("error connecting to db");
        }


        try {
            instructors = FXCollections.observableArrayList();
            String query = "SELECT userId+'- '+fName FROM accounts WHERE userType='instructor'";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            instructors.add("None");
            while (rs.next()) {
                instructors.add(rs.getString(1));
            }
        } catch (Exception e){e.printStackTrace();};

        instructorChoiceBox.getItems().setAll(instructors);
    }

    @FXML
    private void checkPayment() {
        if (paymentCheckBox.isSelected() == true) {
            payment.setText("PAID");
        } else {
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

                query = "SELECT membership FROM members WHERE memberId='" + membersTextField.getText() + "'";
                pst = con.prepareStatement(query);
                rs = pst.executeQuery();
                while (rs.next()) {
                    memberType = rs.getString(1);
                }
            } catch (SQLException e) {
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

    @FXML
    public void btnUpdateClicked() {
        java.util.Date currentDate = null;
        java.util.Date inputDate = null;

        //Check if any field is empty
        if (membersTextField.getText().toString().trim().isEmpty() || bookingDate.getValue() == null) {
            statusLabel.setStyle("-fx-text-fill: red");
            statusLabel.setText("Please fill in required information");
        } else {

            //convert input date and currentDate date to Date format
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String a = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String b = bookingDate.getValue().toString();
            try {
                currentDate = format.parse(a);
                inputDate = format.parse(b);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //check if input date is not before currentDate date
            if (currentDate.after(inputDate)) {
                System.out.println(currentDate + " " + inputDate);
                statusLabel.setStyle("-fx-text-fill: red");
                statusLabel.setText("Invalid date entered");
            }

            //check if end time is greater than start time
            else if (Integer.parseInt(startTime.getValue().toString()) >= Integer.parseInt(endTime.getValue().toString())) {
                statusLabel.setStyle("-fx-text-fill: red");
                statusLabel.setText("Invalid time entered");
            }

            //if all correct, insert into database
            else {
                String query = String.format("UPDATE bookings SET instructorName='%s', sessionType='%s', sessionTime='%s', payment='%s' WHERE bookingId='%s'",
                        instructorChoiceBox.getSelectionModel().getSelectedItem(), sessionType.getSelectionModel().getSelectedItem(),
                        bookingDate.getValue().toString() + ' ' + startTime.getValue().toString() + ' ' + endTime.getValue().toString(), payment.getText(),
                        bookingIdTextField.getText());
                try {
                    stmt.executeUpdate(query);

                    Stage currentDateStage = (Stage) btnClose.getScene().getWindow();
                    currentDateStage.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }

    //SETTERS
    @FXML
    public void btnCancelClicked() {
        Stage currentDateStage = (Stage) btnClose.getScene().getWindow();
        currentDateStage.close();
    }

    public void setBookingIdTextField(String bookingIdTextField) {
        this.bookingIdTextField.setText(bookingIdTextField);
    }

    public void setMembersTextField(String membersTextField) {
        this.membersTextField.setText(membersTextField);
    }

    public void setSessionType(String sessionType) {
        this.sessionType.setValue(sessionType);
    }

    public void setInstructorChoiceBox(String instructorChoiceBox) {
        this.instructorChoiceBox.setValue(instructorChoiceBox);
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate.setValue(bookingDate);
    }

    public void setStartTime(int startTime) {
        this.startTime.getValueFactory().setValue(startTime);
    }

    public void setEndTime(int endTime) {
        this.endTime.getValueFactory().setValue(endTime);
    }

    public void setPayment(String payment) {
        this.payment.setText(payment);
    }

    public void setPaymentCheckBox(Boolean paymentCheckBox) {
        this.paymentCheckBox.setSelected(paymentCheckBox);
    }

}
