/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threelayeredexampleGUI;

import java.util.*;

/**
 *
 * @author YL Hedley
 */
public class ManagerController {
  
         ArrayList list = new ArrayList();
         
         public void register(int ref, String name, String spec){
             
             Dentist dent = new Dentist();
             dent.setRef(ref);
             dent.setName(name);
             dent.setSpeciality(spec); 
             
             list.add(dent);
         }          

        public String viewAll(){
        String data  = "";
        
        System.out.print("list: " + list.size());
        for (int i = 0; i<list.size(); i++){
            
                Dentist temp = (Dentist)list.get(i);
                String str = "\n Dentist ref no: " + temp.getRef() + " name: " + temp.getName() + 
                        " speciality: " +  temp.getSpeciality() + "\n";
                data = data + str;
 
            }  
            return data;
         } 
}   
