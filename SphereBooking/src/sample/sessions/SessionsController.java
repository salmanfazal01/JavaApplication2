package sample.sessions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class SessionsController implements Initializable {

    static Connection con = null;
    static PreparedStatement pst = null;
    static Statement stmt = null;
    static ResultSet rs;
    private ObservableList<Sessions> data;
    private FilteredList<Sessions> filteredData;

    @FXML private TextField searchID = null;
    @FXML private TextField searchDate = null;
    @FXML private TextField searchInstructor = null;

    @FXML private Button newBtn, updateBtn, deleteBtn, checkinBtn;

    @FXML private TableView<Sessions> table;
    @FXML private TableColumn<?, ?> column_bookingId;
    @FXML private TableColumn<?, ?> column_memberId;
    @FXML private TableColumn<?, ?> column_instructor;
    @FXML private TableColumn<?, ?> column_session;
    @FXML private TableColumn<?, ?> column_time;
    @FXML private TableColumn<?, ?> column_payment;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        con = sample.database.DBConnection.makeConnection();
        data = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(data, e -> true);

        try{
            stmt = con.createStatement();
        } catch (Exception e){
            System.out.println("error connecting to db");
        }

        setCellTable();
        loadDataFromDB();

        checkButton();
    }

    public void setCellTable(){
        column_bookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        column_memberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        column_instructor.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        column_session.setCellValueFactory(new PropertyValueFactory<>("sessionType"));
        column_time.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        column_payment.setCellValueFactory(new PropertyValueFactory<>("payment"));
    }

    public void loadDataFromDB(){
        try {
            String query = "SELECT * FROM bookings";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()){
                data.add(new Sessions(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(data);
    }

    @FXML
    private void newButtonClicked(){
        try{
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newBooking.fxml"));
            Parent root1 = fxmlLoader.load();
            root1.setStyle("-fx-background-color: null;");

            Scene scene = new Scene(root1, 1080,680, Color.rgb(0,0,0,0.65));
            stage.setScene(scene);
            stage.show();

            if (true){
                table.setOnMouseMoved(event -> {
                    refreshTable();
                    table.setOnMouseMoved(null);
                });
            }

        } catch (Exception e){
            e.getCause();
        }
    }

    @FXML
    private void deleteButtonClicked(){
        Sessions item = table.getSelectionModel().getSelectedItem();

        TextInputDialog dialog = new TextInputDialog(item.getBookingId());
        dialog.setTitle("Delete");
        dialog.setHeaderText("Are you sure you want to delete this booking?");
        dialog.setContentText("Booking ID: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String query2 = "DELETE FROM bookings WHERE bookingId = '"+item.getBookingId()+"';";
            try {
                stmt.executeUpdate(query2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (true){
                table.setOnMouseMoved(event -> {
                    refreshTable();
                    table.setOnMouseMoved(null);
                });
            }
        }
    }

    @FXML
    private void updateButtonClicked(){
        try{
            Sessions item = table.getSelectionModel().getSelectedItem();

            Stage stage2 = new Stage();
            stage2.initStyle(StageStyle.TRANSPARENT);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/sessions/updateBooking.fxml"));
            Parent root1 = fxmlLoader.load();
            root1.setStyle("-fx-background-color: null;");

            updateBookingController update = fxmlLoader.getController();
            update.setBookingIdTextField(item.getBookingId());
            update.setMembersTextField(item.getMemberId());
            update.setPayment(item.getPayment());
            if(item.getPayment().equals("PAID")){update.setPaymentCheckBox(true);}
            update.setSessionType(item.getSessionType());
            update.setInstructorChoiceBox(item.getInstructorName());
            String[] temp = item.getSessionTime().split(" ");
            update.setStartTime(Integer.parseInt(temp[1]));
            update.setEndTime(Integer.parseInt(temp[2]));
            temp = temp[0].split("-");
            LocalDate date1 = LocalDate.of(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            update.setBookingDate(date1);

            Scene scene = new Scene(root1, 1080,680, Color.rgb(0,0,0,0.7));
            stage2.setScene(scene);
            stage2.show();


            if (true){
                table.setOnMouseMoved(event -> {
                    refreshTable();
                    table.setOnMouseMoved(null);
                });
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void checkinClicked() throws Exception{
        int totalSessions = 0;
        String memberType = "";
        Sessions item = table.getSelectionModel().getSelectedItem();
        System.out.println(item.getPayment());

        if(item.getPayment().equalsIgnoreCase("PAID")){
            System.out.println("in 1");
            String query = "SELECT membership, totalSessions FROM members WHERE memberId='"+item.getMemberId()+"'";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while(rs.next()){
                System.out.println(rs.getString(1));
                totalSessions = Integer.parseInt(rs.getString(2));
                memberType = rs.getString(1);
            }

            if (memberType.equalsIgnoreCase("UpgradeToBasic") || memberType.equalsIgnoreCase("UpgradeToLoyalty")) {
                totalSessions++;
            }

            query = "UPDATE members SET totalSessions = " + totalSessions + " WHERE memberId = '"+item.getMemberId()+"'";
            stmt.executeUpdate(query);

            query = "DELETE FROM bookings WHERE bookingId = '"+item.getBookingId()+"'";
            stmt.executeUpdate(query);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checked-in");
            alert.setHeaderText(null);
            alert.setContentText("Check-in successful!");
            alert.showAndWait();

            refreshTable();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("User has not paid for booking!");
            alert.showAndWait();
        }
    }

    @FXML
    private void searchID(){
        searchDate.setText("");
        searchInstructor.setText("");

        searchID.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user->{
                return newValue.isEmpty() || user.getMemberId().contains(newValue);
            });
        });
        SortedList<Sessions> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void searchInstructor(){
        searchDate.setText(""); searchID.setText("");

        searchInstructor.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user->{
                return newValue.isEmpty() || user.getInstructorName().contains(newValue);
            });
        });
        SortedList<Sessions> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void searchDate(){
        searchID.setText(""); searchInstructor.setText("");

        searchDate.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user->{
                return newValue.isEmpty() || user.getSessionTime().contains(newValue);
            });
        });
        SortedList<Sessions> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    @FXML
    private void refreshTable(){
        data.removeAll(data);
        setCellTable();
        loadDataFromDB();
    }

    private void checkButton(){

        String userType = "";

        Button[] instructors = {newBtn, updateBtn, deleteBtn, checkinBtn};
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

        if(userType.equals("Instructor")){
            for(Button i : instructors){
                i.setDisable(true);
            }
        }
        else if(userType.equals("Slope Operator")){
            for(Button i : slope){
                i.setDisable(true);
            }
        }

    }

}
