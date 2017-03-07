/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threelayeredexampleGUI;

/**
 *
 * @author Oscar
 */
public class Dentist{
         
         private int ref = -1;
         private String name="";
         private String speciality="";
        
         public int getRef(){
             return ref;
         }
         public void setRef(int aRef){
             ref = aRef;
         }
         public String getName(){
             return name;
         }
         public void setName(String aName){
             name = aName;
         }
         public String getSpeciality(){
             return speciality;
         }
         public void setSpeciality(String spec){
             speciality = spec;
         }     
}

