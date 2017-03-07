/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threelayeredexampleGUI;

/**
 *
 * @author YL Hedley
 */
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.*;

public class ManagerGUI {
    /**
     * @param args the command line arguments
     */   
        
    private JFrame mainFrame = new JFrame("PDS System Demo");
    private JFrame addFrame = new JFrame(" Dentist Registration");
    private JLabel headerLabel;
    private JLabel mainStatusLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
       
    ManagerController con = new ManagerController(); 
    ArrayList list = new ArrayList();
        
    public ManagerGUI() {
    }

    
   public void setup(){
      mainFrame = new JFrame(" PDS System Demo");
      mainFrame.setSize(500,500);
      mainFrame.setLayout(new GridLayout(4, 1));

      headerLabel = new JLabel("",JLabel.CENTER );
      mainStatusLabel = new JLabel("",JLabel.CENTER);        

      mainStatusLabel.setSize(400,400);
      
      
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
	        System.exit(0);
         }        
      });    
      controlPanel = new JPanel();
      controlPanel.setLayout(new GridLayout(4, 1));

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(mainStatusLabel);      
      mainFrame.setVisible(true);  
      
      mainFrame.setVisible(true);
   }
    
   public void setup(JFrame frame){
      frame.setSize(500,500);
      frame.setLayout(new GridLayout(4, 1));

      headerLabel = new JLabel("",JLabel.CENTER );
      statusLabel = new JLabel("",JLabel.CENTER);        

      statusLabel.setSize(350,100);
      
      
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
	        System.exit(0);
         }        
      });    
      controlPanel = new JPanel();
      controlPanel.setLayout(new GridLayout(4, 1));

      frame.add(headerLabel);
      frame.add(controlPanel);
      frame.add(statusLabel);      
      frame.setVisible(true);  
      
      frame.setVisible(true);
   }

   private void setVisibility(JFrame frame, boolean bool){
      frame.setVisible(bool); 
   }
   
   public void showEventDemo(){
      setup(); 

      JButton addButton = new JButton("Add a new Customer");
      JButton viewButton = new JButton("View all Customers");
      JButton updateButton = new JButton("Update a Customer");
      JButton deletelButton = new JButton("Delete a Customer");

      addButton.setActionCommand("Add a dentist");
      viewButton.setActionCommand("View all dentists");
      updateButton.setActionCommand("Update a dentist");
      deletelButton.setActionCommand("Delete a dentist");

      addButton.addActionListener(new ButtonClickListener()); 
      viewButton.addActionListener(new ButtonClickListener());
      updateButton.addActionListener(new ButtonClickListener()); 
      deletelButton.addActionListener(new ButtonClickListener()); 

      controlPanel.add(addButton);
      controlPanel.add(viewButton);
      controlPanel.add(updateButton);
      controlPanel.add(deletelButton);       

      mainFrame.setVisible(true);
      addFrame.setVisible(false);
      
   }
   

    private class ButtonClickListener implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         String command = e.getActionCommand();  
         if( command.equals( "Add a dentist" ))  {
            addDemo(); 
         }
         else if( command.equals( "View all dentists" ) )  {
           String data = con.viewAll();  
    
           mainStatusLabel.setText(data);
           
         }
         else if( command.equals( "Update a dentist" ) )  {
            mainStatusLabel.setText("Update Button clicked."); 
         }
         else  {
            mainStatusLabel.setText("Delete Button clicked.");
         }  	
      }		
   }
      
    public void addDemo(){
      mainFrame.setVisible(false);
      JFrame addFrame = new JFrame("Dentist Registration");
      setup(addFrame);
      
      JLabel  reflabel= new JLabel("Enter the reference no: ", JLabel.CENTER);
      JLabel  namelabel= new JLabel("Enter the name: ", JLabel.CENTER);
      JLabel  specLabel = new JLabel("Membership Type: ", JLabel.CENTER);
      final JTextField ref = new JTextField(6);
      final JTextField name = new JTextField(10);
      final JTextField spec = new JTextField(20);      

      JButton addButton = new JButton("Add a new dentist");
      addButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            String theRef = ref.getText();
            String theName = name.getText();
            String theSpec = spec.getText();

            int num =  Integer.parseInt(theRef);            
            con.register(num, theName, theSpec);
            
            String data = "A new dentist is registered with ref no: " + ref.getText();
            data += ", name: " + name.getText();
            data += ", Membership Type: "+ spec.getText(); 
            
            statusLabel.setText(data);        
         }        
      }); 

      JButton backButton = new JButton("Back");
      backButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
             
             setVisibility(mainFrame, true);
             setVisibility(addFrame, false);
         }        
      });       
      controlPanel.add(reflabel);
      controlPanel.add(ref);
      controlPanel.add(namelabel);
      controlPanel.add(name);
      controlPanel.add(specLabel);       
      controlPanel.add(spec);
      controlPanel.add(addButton);
      controlPanel.add(backButton); 
            



      addFrame.setVisible(true);
      
   }
}
