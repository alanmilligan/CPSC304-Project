package database;

import exceptions.InputException;
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
    int currConf = 9999;



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
        int curr = -1;
        for (Reservation r: reservations) {
            if (r.getConfNo() > curr) {
                curr = r.getConfNo();
            }
        }
        currConf = curr + 1;
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
        System.out.println(rents.size());

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

        getCustomers();
        getRents();
        getReservations();
        getReturns();
        getVehicleTypes();
        getVehicles();
//        searchCars("Sedan", "Kits", "", "");
//        searchCars("", "UBC", "", "");
//        searchCars("Sedan", "UBC", "2019-02-02 05:10:00", "2019-02-05 05:10:00");
//        searchCars("Sedan", "UBC", "2019-05-02 05:10:00", "2019-04-05 05:10:00");
    }


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

     public ArrayList<Vehicle> searchCars(String type,String location,String from,String to) throws InputException {
        ArrayList<Vehicle> result = new ArrayList<>();
         if (type.equals("")) {
             type = "%";
         } if (location.equals("")) {
             location = "%";
         }
         try {
             PreparedStatement ps;
             if (from.equals("")) {
                 ps = connection.prepareStatement
                         ("SELECT * FROM Vehicle WHERE status='Available' AND vtname LIKE ? AND location LIKE ?");
                 System.out.println("SELECT * FROM Vehicle WHERE status='Available' AND vtname LIKE ? AND location LIKE ?");
                 ps.setString(1, type);
                 ps.setString(2, location);
             } else {
                 Timestamp startDate = Timestamp.valueOf(from);
                 Timestamp endDate = Timestamp.valueOf(to);
                 if (startDate.after(endDate)) {
                     throw new InputException("Improper dates! Start after end");
                 }
                 ps = connection.prepareStatement("SELECT * FROM Vehicle v WHERE v.vtname LIKE ? AND " +
                         "v.location LIKE ? AND NOT EXISTS (SELECT * FROM Rent r WHERE v.vlicense = r.vlicense AND (" +
                         "r.toDate > ?) AND (r.fromDate < ?))");
                 System.out.println("SELECT * FROM Vehicle v WHERE v.vtname LIKE '?' AND v.location LIKE '?' AND NOT EXISTS (SELECT * FROM Rent r WHERE v.vlicense = r.vlicense AND (r.toDate > ?) AND (r.fromDate < ?))");
                 ps.setString(1, type);
                 ps.setString(2, location);
                 ps.setTimestamp(3, startDate);
                 ps.setTimestamp(4, endDate);
             }
             ResultSet rs = ps.executeQuery();
             while(rs.next()) {
                 Vehicle v = new Vehicle(rs.getString("vlicense"), rs.getString("make"),
                         rs.getString("model"), rs.getInt("year"), rs.getString("color"),
                         rs.getInt("odometer"), rs.getString("status"), rs.getString("vtname"),
                         rs.getString("location"), rs.getString("city"));
                 result.add(v);
             }
             rs.close();
             ps.close();
             System.out.println(result);

             // GET COUNT
             PreparedStatement p;
             if (from.equals("")) {
                 p = connection.prepareStatement
                         ("SELECT COUNT(*) as total FROM Vehicle WHERE status='Available' AND vtname LIKE ? AND location LIKE ?");
                 System.out.println("SELECT COUNT(*) as total FROM Vehicle WHERE status='Available' AND vtname LIKE ? AND location LIKE ?");
                 p.setString(1, type);
                 p.setString(2, location);
             } else {
                 Timestamp startDate = Timestamp.valueOf(from);
                 Timestamp endDate = Timestamp.valueOf(to);
                 if (startDate.after(endDate)) {
                     System.out.println("Improper dates!");
                     //Todo SHOW GUI ERROR for improper dates.
                     return new ArrayList<>();
                 }
                 p = connection.prepareStatement("SELECT COUNT(*) as total FROM Vehicle v WHERE v.vtname LIKE ? AND " +
                         "v.location LIKE ? AND NOT EXISTS (SELECT * FROM Rent r WHERE v.vlicense = r.vlicense AND (" +
                         "r.toDate > ?) AND (r.fromDate < ?))");
                 System.out.println("SELECT COUNT(*) as total FROM Vehicle v WHERE v.vtname LIKE '?' AND v.location LIKE '?' AND NOT EXISTS (SELECT * FROM Rent r WHERE v.vlicense = r.vlicense AND (r.toDate > ?) AND (r.fromDate < ?))");
                 p.setString(1, type);
                 p.setString(2, location);
                 p.setTimestamp(3, startDate);
                 p.setTimestamp(4, endDate);
             }
             ResultSet r = p.executeQuery();
             r.next();
             int count = r.getInt("total");
             r.close();
             p.close();
             System.out.println(count);
             if (count != result.size()) {
                 System.out.println("WEIRd error with count");
             }

         } catch (SQLException e) {
             System.out.println(e.getMessage());
         } catch (IllegalArgumentException e) {
             //TODO SHOW GUI error for improper date.
             throw new InputException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
         }

         return result;

         //SELECT V.type, V.location, from, to, COUNT(DISTINCT(V.vid))
         //FROM Vehicle V,
         //WHERE NOT EXISTS (SELECT R.rid
         //                  FROM Rent R
         //                  WHERE R.vid = V.vid AND (R.fromDate < DATE(to) AND R.toDate > DATE(from))
         //                        OR R.fromDate > DATE(to))
         //GROUP BY V.type
         //ORDER BY COUNT(DISTINCT(vid)) ASC;
    }


    public Reservation makeReservation(String name,String license,String type,String location, String pdate,String ptime,String rdate,String rtime) throws InputException {
        Timestamp start = Timestamp.valueOf(pdate + " " + ptime);
        Timestamp end = Timestamp.valueOf(rdate + " " + rtime);
        if (start.after(end)) {
            throw new InputException("Invalid dates! Start is after end");
        }
        ArrayList<Vehicle> availableVehicles = searchCars(type, location, pdate + " " + ptime, rdate + " " + rtime);
        if (availableVehicles.size() == 0) {
            throw new InputException("No vehicles available at that time!");
        }

        Reservation r = new Reservation(currConf, license, type, start, end);

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Reservation VALUES (?,?,?,?,?)");
            ps.setString(1, Integer.toString(r.getConfNo()));
            ps.setString(2, r.getDlicense());
            ps.setString(3, r.getVtname());
            ps.setTimestamp(4, r.getFromDate());
            ps.setTimestamp(5, r.getToDate());

            ps.executeUpdate();
            connection.commit();
            ps.close();
            getReservations();

            return r;
        } catch (SQLException e) {
            System.out.println("SQL ERROR");
            System.out.println(e.getMessage());
            rollbackConnection();
            throw new InputException("SQL error :(");
        }

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
