/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airlinemanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author yashpatel
 */
public class Conn {
    Connection c;
    Statement s;

    public Conn() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Replace `127.0.0.1` with the Docker network host if needed
            String url = "jdbc:mysql://localhost:3306/airlinemanagementsystem"; // Change `localhost` if necessary
            String user = "root"; // Replace with your MySQL username
            String password = "yash"; // Replace with your MySQL password

            // Establish the connection
            c = DriverManager.getConnection(url, user, password);

            // Create a statement object
            s = c.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
