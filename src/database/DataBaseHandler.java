package database;

import exceptions.InputException;
import model.*;

import java.io.*;
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
    public int currConf = 9999;
    public int currRent = 9999;
    public int emptyTankFee = 100;



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

    public ArrayList<Customer> getCustomers() {
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
        return customers;
    }

    public ArrayList<VehicleType> getVehicleTypes() {
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
        return vehicleTypes;
    }

    public ArrayList<Vehicle> getVehicles() {
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
        return vehicles;
    }

    public ArrayList<Reservation> getReservations() {
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

        return reservations;
    }

    public ArrayList<Rent> getRents() {
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

        int curr = -1;
        for (Rent r: rents) {
            if (r.getRid() > curr) {
                curr = r.getRid();
            }
        }
        currRent = curr + 1;

        return rents;
    }

    public ArrayList<Return> getReturns() {
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
        return returns;
    }

    //run sql scripts/populate database
    private void setup(){
//        File initialData = new File("src/database/init.sql");
//        try {
//            InputStream stream = new DataInputStream(new FileInputStream(initialData));
//
//            executeScript(stream);
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        } catch (SQLException e) {
//            System.out.println("Error setting up database");
//        }

        getCustomers();
        getRents();
        System.out.println(rents.size());
        getReservations();
        getReturns();
        getVehicleTypes();
        getVehicles();

//        searchCars("Sedan", "Kits", "", "");
//        searchCars("", "UBC", "", "");
//        searchCars("Sedan", "UBC", "2019-02-02 05:10:00", "2019-02-05 05:10:00");
//        searchCars("Sedan", "UBC", "2019-05-02 05:10:00", "2019-04-05 05:10:00");
//        try {
//            System.out.println(makeRentNoReservation("Kobe Bryant", "MAMBA24", "Truck",
//                    "2020-02-02", "03:33:33", "2020-02-03", "03:33:33", "mamba", 1231231231, 32323, "UBC"));
//            System.out.println(rents.size());
//        } catch (InputException e) {
//            System.out.println(e.getMessage());
//        }
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
             location = "UBC";
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
                     throw new InputException("Improper dates! Start after end");
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

    public Reservation findReservation(int confNumber) throws InputException {
        Reservation r = null;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Reservation WHERE confNo = " + Integer.toString(confNumber));
            while(rs.next()) {
                r = new Reservation(rs.getInt("confNo"), rs.getString("vtname"),
                        rs.getString("dlicense"), rs.getTimestamp("fromDate"),
                        rs.getTimestamp("toDate"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (r == null) {
            throw new InputException("Reservation Number with given Confirmation Number not found!");
        }

        return r;

    }

    //Todo ADD cardName into GUI, also Card type is not needed
    //Todo Add a GUI field to specify the location when renting a car with reservation because the project spec is stupid
    public Rent makeRent(Reservation r, String cardName, int cardNo, int expDate, String location) throws InputException {
        ArrayList<Vehicle> cars = searchCars(r.getVtname(), location, r.getFromDate().toString(), r.getToDate().toString());
        if (cars.size() == 0) {
            throw new InputException("Car type specified for your reservation is no longer available");
        }else {
            Rent rent = new Rent(currRent, cars.get(0).getVlicense(), r.getDlicense(), r.getFromDate(), r.getToDate(),
                    cars.get(0).getOdometer(),cardName, cardNo, expDate, r.getConfNo());
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO Rent VALUES (?,?,?,?,?,?,?,?,?,?)");
                ps.setInt(1, rent.getRid());
                ps.setString(2, rent.getVlicense());
                ps.setString(3, rent.getDlicense());
                ps.setTimestamp(4, rent.getFromDate());
                ps.setTimestamp(5, rent.getToDate());
                ps.setInt(6, rent.getOdometer());
                ps.setString(7, rent.getCardName());
                ps.setInt(8, rent.getCardNo());
                ps.setInt(9, rent.getExpDate());
                ps.setInt(10, rent.getConfNo());

                PreparedStatement p = connection.prepareStatement("UPDATE Vehicle SET status = 'Rented' WHERE vlicense = ?");
                p.setString(1, rent.getVlicense());
                p.executeUpdate();
                connection.commit();
                p.close();
                getRents();
                getVehicles();
                return rent;
            } catch (SQLException e) {
                System.out.println("SQL ERROR");
                System.out.println(e.getMessage());
                rollbackConnection();
                throw new InputException("SQL error :(");
            }
        }
    }

    public Rent makeRentNoReservation(String name, String dlicense, String type, String pdate, String ptime, String rdate, String rtime, String cardName, int cardNo, int expDate, String location) throws InputException {
        boolean customerFound = false;
        for (Customer c: customers) {
            if (c.getDlicense().equals(dlicense)) {
                customerFound = true;
            }
        }
        if (!customerFound) {
            throw new InputException("No Customer with specified license found. Please register customer in the Customer tab!");
        }

        Timestamp start = Timestamp.valueOf(pdate + " " + ptime);
        Timestamp end = Timestamp.valueOf(rdate + " " + rtime);
        if (start.after(end)) {
            throw new InputException("Invalid dates! Start is after end");
        }
        ArrayList<Vehicle> availableVehicles = searchCars(type, location, pdate + " " + ptime, rdate + " " + rtime);
        if (availableVehicles.size() == 0) {
            throw new InputException("No vehicles available at that time!");
        }
        Rent rent = new Rent(currRent, availableVehicles.get(0).getVlicense(), dlicense, start, end,
                availableVehicles.get(0).getOdometer(),cardName, cardNo, expDate, null);
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Rent VALUES (?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, rent.getRid());
            ps.setString(2, rent.getVlicense());
            ps.setString(3, rent.getDlicense());
            ps.setTimestamp(4, rent.getFromDate());
            ps.setTimestamp(5, rent.getToDate());
            ps.setInt(6, rent.getOdometer());
            ps.setString(7, rent.getCardName());
            ps.setInt(8, rent.getCardNo());
            ps.setInt(9, rent.getExpDate());
            ps.setNull(10,java.sql.Types.INTEGER);

            ps.executeUpdate();
            connection.commit();
            ps.close();

            PreparedStatement p = connection.prepareStatement("UPDATE Vehicle SET status = 'Rented' WHERE vlicense = ?");
            p.setString(1, rent.getVlicense());
            p.executeUpdate();
            connection.commit();
            p.close();
            getRents();
            getVehicles();
            return rent;
        } catch (SQLException e) {
            System.out.println("SQL ERROR");
            System.out.println(e.getMessage());
            rollbackConnection();
            throw new InputException("SQL error :(");
        }
    }


    public String[] handleReturn(int rentid, String date, int odometer, boolean gas) throws InputException {
        getRents();
        Rent r = null;
        for (Rent rent: rents) {
            if (rent.getRid() == rentid) {
                r = rent;
            }
        }
        if (r == null) {
            throw new InputException("No matching rent ID!");
        }

        Timestamp rdate = Timestamp.valueOf(date);
        if (rdate.before(r.getFromDate())) {
            throw new InputException("Return date is before the start date!");
        }

        // Get car.
        getVehicles();
        Vehicle v = null;
        for (Vehicle veh: vehicles) {
            if (veh.getVlicense().equals(r.getVlicense())) {
                v = veh;
            }
        }
        if (v == null) {
            throw new InputException("No matching vehicle rented out!");
        }

        // Get vehicleType.
        getVehicleTypes();
        VehicleType vt = null;
        for (VehicleType vtype: vehicleTypes) {
            if (vtype.getVtname().equals(v.getVtname())) {
                vt = vtype;
            }
        }
        if (vt == null) {
            throw new InputException("No matching vehicle type!");
        }

        // Calculate Cost.
        int odometerDiff = odometer - r.getOdometer();
        if (odometerDiff < 0) {
            throw new InputException("New odometer value is less than odometer value before rental!");
        }
        int kmrate = vt.getKrate();
        int value = kmrate * odometerDiff;
        if (!gas) {
            value += emptyTankFee;
        }

        String fulltank = gas ? "Y" : "N";

        getReturns();
        for (Return ret: returns) {
            if (ret.getRid() == r.getRid()) {
                throw new InputException("Vehicle for this rental already returned!");
            }
        }

        // Insert return value, update car.
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Return VALUES (?,?,?,?,?)");
            ps.setInt(1, r.getRid());
            ps.setTimestamp(2, rdate);
            ps.setInt(3, odometer);
            ps.setString(4, fulltank);
            ps.setInt(5, value);
            ps.executeUpdate();
            connection.commit();
            ps.close();
            getReturns();
            PreparedStatement p = connection.prepareStatement("UPDATE  Vehicle SET status = 'Available', odometer = ? " +
                    "WHERE vlicense = ?");
            p.setInt(1, odometer);
            p.setString(2, v.getVlicense());
            p.executeUpdate();
            connection.commit();
            p.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new InputException("SQL Error :(");
        }

        String costExplanation = "Vehicle Type " + vt.getVtname() + " kmRate = " + kmrate + " \n Odometer Difference = " +
                "" + odometerDiff + "\n KmRate cost = " + odometerDiff + " * " + kmrate + " = " + Integer.toString(odometerDiff * kmrate);
        if (!gas) {
            costExplanation = costExplanation + "\n Tank not returned full, added $100 extra cost \n " +
                    "Final cost = " + value;
        }

        return new String[] {Integer.toString(r.getRid()), r.getFromDate().toString(), rdate.toString(),
                Integer.toString(value), costExplanation};

    }
}
