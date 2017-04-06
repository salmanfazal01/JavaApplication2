package sample.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    static Connection con = null;

    public static Connection makeConnection() {

        try
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=SphereDB;integratedSecurity=true;";
            con=DriverManager.getConnection(url);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return con;
    }



}
