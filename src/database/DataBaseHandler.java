package database;

import model.*;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBaseHandler {

    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection = null;

    public ArrayList<Customer> customers = new ArrayList<>();
    public ArrayList<Rent> rents = new ArrayList<>();
    public ArrayList<VehicleType> vehicleTypes = new ArrayList<>();
    public ArrayList<Vehicle> vehicles = new ArrayList<>();
    public ArrayList<Return> returns = new ArrayList<>();
    public ArrayList<Reservation> reservations = new ArrayList<>();



    public DataBaseHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, "ora_jacquesc", "a17816802");
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        setup();
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

    //Probably wont be used cause we can prepopulate the DB.
    public void executeScript(InputStream in) throws SQLException {
        Scanner s = new Scanner(in);
        s.useDelimiter("/\\*[\\s\\S]*?\\*/|--[^\\r\\n]*|;");

        Statement st = null;

        try {
            st = connection.createStatement();
            while (s.hasNext())
            {
                String line = s.next().trim();
                System.out.println(line);
                if (!line.isEmpty())
                    st.execute(line);
                connection.commit();
            }
        } catch (SQLException e) {
            System.out.println("ERROR setting up database");
            System.out.println(e.getMessage());
        } finally {
            if (st != null)
                st.close();
        }
    }

    public void getCustomers() {
        customers = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");
            while(rs.next()) {
                Customer c = new Customer(rs.getString("cellphone"), rs.getString("name"),
                        rs.getString("address"), rs.getString("dlicense"));
                customers.add(c);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(customers);
    }

    public void getVehicleTypes() {
        vehicleTypes = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM VehicleType");
            while(rs.next()) {
                VehicleType vt = new VehicleType(rs.getString("vtname"), rs.getString("features"),
                        rs.getInt("wrate"), rs.getInt("drate"), rs.getInt("hrate"),
                        rs.getInt("wirate"), rs.getInt("dirate"), rs.getInt("hirate"),
                        rs.getInt("krate"));
                vehicleTypes.add(vt);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(vehicleTypes);

    }

    public void getVehicles() {
        vehicles = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Vehicle");
            while(rs.next()) {
                Vehicle v = new Vehicle(rs.getString("vlicense"), rs.getString("make"),
                        rs.getString("model"), rs.getInt("year"), rs.getString("color"),
                        rs.getInt("odometer"), rs.getString("status"), rs.getString("vtname"),
                        rs.getString("location"), rs.getString("city"));
                vehicles.add(v);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(vehicles);
    }

    public void getReservations() {
        reservations = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Reservation");
            while(rs.next()) {
                Reservation r = new Reservation(rs.getInt("confNo"), rs.getString("vtname"),
                        rs.getString("dlicense"), rs.getTimestamp("fromDate"),
                        rs.getTimestamp("toDate"));
                reservations.add(r);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(reservations);
    }

    public void getRents() {
        rents = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Rent");
            while(rs.next()) {
                Rent r = new Rent(rs.getInt("rid"), rs.getString("vlicense"),
                        rs.getString("dlicense"), rs.getTimestamp("fromDate"),
                        rs.getTimestamp("toDate"), rs.getInt("odometer"),
                        rs.getString("cardName"), rs.getInt("cardNo"),
                        rs.getInt("ExpDate"), rs.getInt("confNo"));
                rents.add(r);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(rents);

    }

    public void getReturns() {
        returns = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Return");
            while(rs.next()) {
                Return r = new Return(rs.getInt("rid"), rs.getTimestamp("rdate"),
                        rs.getInt("odometer"), rs.getString("fulltank"),
                        rs.getInt("value"));
                returns.add(r);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(returns);
    }

    //run sql scripts/populate database
    private void setup(){
//        File initialData = new File("src/database/init.sql");
//        try {
//            InputStream stream = new DataInputStream(new FileInputStream(initialData));
//            executeScript(stream);
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        } catch (SQLException e) {
//            System.out.println("Error setting up database");
//        }
//        getCustomers();
//        getRents();
//        getReservations();
//        getReturns();
//        getVehicleTypes();
//        getVehicles();
    }

// DELETE THIS DELETE THIS DELETE THIS IN FINAL COPY
//    public void insertBranch(BranchModel model) {
//        try {
//            PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?,?,?,?)");
//            ps.setInt(1, model.getId());
//            ps.setString(2, model.getName());
//            ps.setString(3, model.getAddress());
//            ps.setString(4, model.getCity());
//            if (model.getPhoneNumber() == 0) {
//                ps.setNull(5, java.sql.Types.INTEGER);
//            } else {
//                ps.setInt(5, model.getPhoneNumber());
//            }
//
//            ps.executeUpdate();
//            connection.commit();
//
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//            rollbackConnection();
//        }
//    }

    // add functions: inserts NEW values into the tables

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println("ERROR");
        }
    }

     public void addCustomer(String cellNumber, String name, String address, String license) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
            ps.setString(1, cellNumber);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, license);

            ps.executeUpdate();
            connection.commit();
            ps.close();
            getCustomers();
        } catch (SQLException e) {
            System.out.println("ERROR");
            rollbackConnection();
        }
     }

     public void updateCustomer(String cellNumber, String name, String address, String license) {
         try {
             getCustomers();
             PreparedStatement ps = connection.prepareStatement("UPDATE Customer SET cellphone = ?, name = ?, address = ? WHERE dlicense = ?");
             ps.setString(1, cellNumber);
             ps.setString(2, name);
             ps.setString(3, address);
             ps.setString(4, license);
             int rowCount = ps.executeUpdate();
             if (rowCount == 0) {
                 System.out.println("ERROR" + " Customer with license " + license + " does not exist!");
             }
             connection.commit();
             ps.close();
             getCustomers();
         } catch (SQLException e) {
             System.out.println("ERROR");
             rollbackConnection();
         }
     }

    public void deleteReturn(int rid) {
        try {
            getReturns();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Return WHERE rid = ?");
            ps.setInt(1, rid);
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("ERROR" + " Return with rid " + rid + " does not exist!");
            }
            connection.commit();
            ps.close();
            getReturns();
        } catch (SQLException e) {
            System.out.println("ERROR");
            rollbackConnection();
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
