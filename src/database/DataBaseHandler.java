package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseHandler {

    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection = null;



    public DataBaseHandler() {
//        try {
//            // Load the Oracle JDBC driver
//            // Note that the path could change for new drivers
//            //DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//        }
    }

    //prompt oracle login
    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    //run sql scripts/populate database
    private void setup(){

    }


     public void addCustomer(String address, String cellNumber, String license, String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
            ps.setString(1, cellNumber);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, license);
        } catch (SQLException e) {
            System.out.println("ERROR");
        }
     }

     //TODO figure out return type
     public void searchCars(String type,String location,String from,String to) {

         if (type.equals("")){type = "*";}
         //some sorta blank date functionality that removes the where not exists


         //TODO figure this one out since reservations are done by type not by actual vehicle so it will be hard to count

         //SELECT V.type, V.location, from, to, COUNT(DISTINCT(V.vid))
         //FROM Vehicle V,
         //WHERE NOT EXISTS (SELECT R.rid
         //                  FROM Rent R
         //                  WHERE R.vid = V.vid AND (R.fromDate < DATE(to) AND R.toDate > DATE(from))
         //                        OR R.fromDate > DATE(to))
         //GROUP BY V.type
         //ORDER BY COUNT(DISTINCT(vid)) ASC;
    }

    //TODO figure out return type
    public void verifyReservation() {
        //Query to find there exists a car that is allowed with constraints

    }

    //TODO figure out return type
    public void findReservation() {
        //SELECT *
        //FROM Reservation R
        //WHERE R.confNumber = ConfNumber
    }




}
