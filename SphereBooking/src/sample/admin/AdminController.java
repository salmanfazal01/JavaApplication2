package sample.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class AdminController implements Initializable{

    @FXML private TextField userName;
    @FXML private TextField fName;
    @FXML private TextField lName;
    @FXML private TextField password;
    @FXML private ChoiceBox choiceBox;

    @FXML private TextField standard;
    @FXML private TextField training;
    @FXML private TextField discount;

    private ObservableList<Admin> data;
    @FXML private TableView<Admin> table;
    @FXML private TableColumn<?, ?> column_id;
    @FXML private TableColumn<?, ?> column_name;
    @FXML private TableColumn<?, ?> column_type;


    static Connection con = null;
    static Statement stmt = null;
    static PreparedStatement pst2 = null;
    static ResultSet rs = null;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        con = sample.database.DBConnection.makeConnection();
        try{
            stmt = con.createStatement();
        } catch (Exception e){
            System.out.println("error connecting to db");
        }

        data = FXCollections.observableArrayList();
        setCellTable();
        loadDataFromDB();
        fillFees();
    }

    @FXML
    private void btnDeleteClicked(){
        if(userName.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Enter valid user name!");
        } else {

            String query = "DELETE FROM accounts WHERE userId = '"+userName.getText().trim()+"';";

            try{
                stmt.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Account Deleted.");

                data = FXCollections.observableArrayList();
                setCellTable();
                loadDataFromDB();

            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @FXML
    private void btnAddClicked(){
        if(userName.getText().isEmpty() || password.getText().isEmpty() || fName.getText().isEmpty()
                || lName.getText().isEmpty() || choiceBox.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Enter valid details");
        } else{
            String query = String.format("INSERT INTO accounts VALUES('%s', '%s', '%s', '%s', '%s')", userName.getText().trim(),
                    fName.getText().trim(), lName.getText().trim(), password.getText().trim(), choiceBox.getSelectionModel().getSelectedItem());

            try {
                stmt.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Account Added");

                data = FXCollections.observableArrayList();
                setCellTable();
                loadDataFromDB();

            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error! (user may already exist)");
            }
        }
    }

    private void setCellTable(){
        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_type.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    private void loadDataFromDB(){
        try {
            String query2 = "SELECT * FROM accounts";
            pst2 = con.prepareStatement(query2);
            rs = pst2.executeQuery();

            while (rs.next()){
                data.add(new Admin(rs.getString(1), rs.getString(2), rs.getString(5)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(data);
    }

    @FXML
    private void fillFees(){
        try {
            String query = "SELECT * FROM fees";
            pst2 = con.prepareStatement(query);
            rs = pst2.executeQuery();

            while (rs.next()){
                standard.setText(String.valueOf(rs.getString(1)));
                training.setText(String.valueOf(rs.getString(2)));
                discount.setText(String.valueOf(rs.getString(3)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateFees(){

        try {
            String query2 = "UPDATE fees SET standard="+String.valueOf(standard.getText())+", training="+String.valueOf(training.getText())+
                    ", loyaltyDiscount="+String.valueOf(discount.getText());
            stmt.executeUpdate(query2);
            fillFees();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
