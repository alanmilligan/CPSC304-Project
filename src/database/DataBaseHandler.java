package database;

import exceptions.InputException;
import model.*;
import ui.ErrorTemplate;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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

        Login log = new Login();

//        try {
//            if (connection != null) {
//                connection.close();
//            }
//
//            connection = DriverManager.getConnection(ORACLE_URL, "ora_jacquesc", "a17816802");
//            connection.setAutoCommit(false);
//
//            System.out.println("\nConnected to Oracle!");
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//        }
//        setup();
    }

    //prompt oracle login
    public boolean login(String username, String password) throws SQLException {
        if (connection != null) {
            connection.close();
        }

        connection = DriverManager.getConnection(ORACLE_URL, username, password);
        connection.setAutoCommit(false);

        getCustomers();
        getRents();
        getReservations();
        getReturns();
        getVehicleTypes();
        getVehicles();
        System.out.println("\nConnected to Oracle!");
        return true;
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
        getCustomers();
        getRents();
        getReservations();
        getReturns();
        getVehicleTypes();
        getVehicles();

//        Stuff for testing leave commented out
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
//        try {
//            System.out.println(handleRentReport("2020-02-02", "UBC", "Vancouver"));
//            System.out.println(handleRentReport("2019-07-01", "", ""));
//            System.out.println(handleRentReportCount("2019-07-01", "UBC","Vancouver"));
//        } catch (InputException e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(handleReturnReport("2017-02-01", "", ""));
//            System.out.println(handleReturnReportCountVtype("2017-02-01", "", ""));
//            System.out.println(handleReturnReportCountBranch("2017-02-01", "", ""));
//            System.out.println(handleReturnReportCountBranch("2017-02-01", "UBC", "Vancouver"));
//        } catch (InputException e) {
//            e.printStackTrace();
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
                         ("SELECT * FROM Vehicle WHERE status='Available' AND vtname LIKE ? AND location LIKE ? " +
                                 "ORDER BY location, city, vtname");
                 //System.out.println("SELECT * FROM Vehicle WHERE status='Available' AND vtname LIKE ? AND location LIKE ?");
                 ps.setString(1, type);
                 ps.setString(2, location);
             } else {
                 Timestamp startDate = Timestamp.valueOf(from);
                 Timestamp endDate = Timestamp.valueOf(to);
                 if(startDate.before(Timestamp.valueOf("2001-01-01 00:00:00"))) {
                     throw new InputException("Date should be after after Jan 1, 2001!");
                 } else if (endDate.after(Timestamp.valueOf("2050-01-01 00:00:00"))){
                     throw new InputException("Date should be before 2050!");
                 }
                 if (startDate.after(endDate)) {
                     throw new InputException("Improper dates! Start after end");
                 }
                 ps = connection.prepareStatement("SELECT * FROM Vehicle v WHERE v.vtname LIKE ? AND " +
                         "v.location LIKE ? AND NOT EXISTS (SELECT * FROM Rent r WHERE v.vlicense = r.vlicense AND (" +
                         "r.toDate > ?) AND (r.fromDate < ?)) ORDER BY location, city, vtname");
                 //System.out.println("SELECT * FROM Vehicle v WHERE v.vtname LIKE '?' AND v.location LIKE '?' AND NOT EXISTS (SELECT * FROM Rent r WHERE v.vlicense = r.vlicense AND (r.toDate > ?) AND (r.fromDate < ?))");
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
                 System.out.println("Weird error with count");
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

                ps.executeUpdate();
                ps.close();

                PreparedStatement p = connection.prepareStatement("UPDATE Vehicle SET status = 'Rented' WHERE vlicense = ?");
                p.setString(1, rent.getVlicense());
                p.executeUpdate();
                connection.commit();
                p.close();
                getRents();
                getVehicles();
                System.out.println("rent made");
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
        Rent r = null;
        try {
            PreparedStatement psRent = connection.prepareStatement("SELECT * FROM Rent WHERE rid = ?");
            psRent.setInt(1, rentid);
            ResultSet rs = psRent.executeQuery();
            connection.commit();
            while (rs.next()) {
                r = new Rent(rs.getInt("rid"), rs.getString("vlicense"),
                        rs.getString("dlicense"), rs.getTimestamp("fromDate"),
                        rs.getTimestamp("toDate"), rs.getInt("odometer"),
                        rs.getString("cardName"), rs.getInt("cardNo"),
                        rs.getInt("ExpDate"), rs.getInt("confNo"));
            }
            rs.close();
            rs.close();
        } catch (SQLException e) {
            throw new InputException("SQL Error :(");
        }
        if (r == null) {
            throw new InputException("No matching rent ID!");
        }

        Timestamp rdate = Timestamp.valueOf(date);
        if (rdate.before(r.getFromDate())) {
            throw new InputException("Return date is before the start date!");
        }

        // Get car.
        Vehicle v = null;
        try {
            PreparedStatement psVeh = connection.prepareStatement("SELECT * FROM Vehicle WHERE vlicense = ?");
            psVeh.setString(1, r.getVlicense());
            ResultSet rs = psVeh.executeQuery();
            connection.commit();
            while(rs.next()) {
                v = new Vehicle(rs.getString("vlicense"), rs.getString("make"),
                        rs.getString("model"), rs.getInt("year"), rs.getString("color"),
                        rs.getInt("odometer"), rs.getString("status"), rs.getString("vtname"),
                        rs.getString("location"), rs.getString("city"));
            }
            rs.close();
            psVeh.close();
        } catch (SQLException e) {
            throw new InputException("SQL Error :(");
        }
        if (v == null) {
            throw new InputException("No matching vehicle rented out!");
        }

        // Get vehicleType.
        VehicleType vt = null;
        try {
            PreparedStatement psVeh = connection.prepareStatement("SELECT * FROM VehicleType WHERE vtname = ?");
            psVeh.setString(1, v.getVtname());
            ResultSet rs = psVeh.executeQuery();
            connection.commit();
            while(rs.next()) {
                vt = new VehicleType(rs.getString("vtname"), rs.getString("features"),
                        rs.getInt("wrate"), rs.getInt("drate"), rs.getInt("hrate"),
                        rs.getInt("wirate"), rs.getInt("dirate"), rs.getInt("hirate"),
                        rs.getInt("krate"));
            }
            rs.close();
            psVeh.close();
        } catch (SQLException e) {
            throw new InputException("SQL Error :(");
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
            throw new InputException("Vehicle for this rental already returned!");
        }

        String calc = Integer.toString(kmrate) + " * " + String.valueOf(odometerDiff) + " + " + String.valueOf(gas ? 0 : 100)
                +" = " + String.valueOf(kmrate*odometerDiff+(gas ? 0 : 100));
        System.out.println(odometerDiff);


        String[] returnInfo = new String[] {Integer.toString(r.getRid()),r.getFromDate().toString(),vt.getVtname(),
                Integer.toString(kmrate),String.valueOf(odometerDiff),String.valueOf(gas ? 0 : 100),calc,String.valueOf(value)};

//        String costExplanation = "Vehicle Type " + vt.getVtname() + " kmRate = " + kmrate + " \n Odometer Difference = " +
//                "" + odometerDiff + "\n KmRate cost = " + odometerDiff + " * " + kmrate + " = " + Integer.toString(odometerDiff * kmrate);
//        if (!gas) {
//            costExplanation = costExplanation + "\n Tank not returned full, added $100 extra cost \n " +
//                    "Final cost = " + value;
//        }
//
//        String[] answer = new String[] {Integer.toString(r.getRid()), r.getFromDate().toString(), rdate.toString(),
//                Integer.toString(value), costExplanation};
//        System.out.println(Arrays.toString(answer));
        return returnInfo;
    }

    public void checkBranchExists(String location, String city) throws InputException {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) as num FROM Vehicle " +
                    "WHERE location = ? AND city = ?");
            ps.setString(1, location);
            ps.setString(2, city);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("num");
            if (count == 0) {
                throw new InputException("Branch does not exist!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    // Getting Rent Reports (part 1 of 2)
    public ArrayList<RentReport> handleRentReport(String date, String location, String city) throws InputException {
        ArrayList<RentReport> reportsReturned = new ArrayList<>();
        try {
            String start = date + " 00:00:00";
            String end = date + " 23:59:59";
            PreparedStatement ps;
            if (location.equals("") && city.equals("")) {
                ps = connection.prepareStatement("SELECT rid, r.vlicense AS vlicense, dlicense,  " +
                        "fromDate, toDate, r.odometer AS odometer, cardName, cardNo, ExpDate, confNo, " +
                        "v.location as location, v.city as city, v.vtname as vtname " +
                        "FROM Rent r, Vehicle v WHERE ? <= r.fromDate AND ? >= r.fromDate AND v.vlicense = r.vlicense " +
                        "ORDER BY v.city, v.location, v.vtname");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
            } else if (!location.equals("") && !city.equals("")) {
                checkBranchExists(location, city);
                ps = connection.prepareStatement("SELECT rid, r.vlicense AS vlicense, dlicense,  " +
                        "fromDate, toDate, r.odometer AS odometer, cardName, cardNo, ExpDate, confNo, " +
                        "v.location as location, v.city as city, v.vtname as vtname " +
                        "FROM Rent r, Vehicle v WHERE ? <= r.fromDate AND ? >= r.fromDate AND v.vlicense = r.vlicense AND v.location = ? AND v.city = ? " +
                        "ORDER BY v.vtname");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
                ps.setString(3, location);
                ps.setString(4, city);
            } else {
                throw new InputException("Need both location and city or neither!");
            }

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                RentReport r = new RentReport(rs.getInt("rid"), rs.getString("vlicense"),
                        rs.getString("dlicense"), rs.getTimestamp("fromDate"),
                        rs.getTimestamp("toDate"), rs.getInt("odometer"),
                        rs.getString("cardName"), rs.getInt("cardNo"),
                        rs.getInt("ExpDate"), rs.getInt("confNo"), rs.getString("location"),
                        rs.getString("city"), rs.getString("vtname"));
                reportsReturned.add(r);
            }
            rs.close();
            ps.close();

            return reportsReturned;

        } catch (SQLException e) {
            throw new InputException("SQL Error :(");
        } catch (IllegalArgumentException e) {
            throw new InputException("Please input a date in right format!");
        }
    }

    // Getting counts for the reports (part 2 of 2)
    // Returns hashset that maps vtname to count and another hashset that maps branch (consisting of a string with
    // the location and the city) to count. If branch is specified in the input just returns the total count in the second
    // hashset.
    public ArrayList<HashMap<String, Integer>> handleRentReportCount(String date, String location, String city) throws InputException {
        ArrayList<HashMap<String, Integer>> counts = new ArrayList<>();
        try {
            String start = date + " 00:00:00";
            String end = date + " 23:59:59";
            PreparedStatement ps;
            HashMap<String, Integer> vtcount = new HashMap<>();
            if (location.equals("") && city.equals("")) {
                ps = connection.prepareStatement("SELECT COUNT(rid) AS num, v.vtname AS vtname FROM Rent r, " +
                        "Vehicle v WHERE ? <= r.fromDate AND ? >= r.fromDate AND v.vlicense = r.vlicense GROUP BY v.vtname");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
            } else if (!location.equals("") && !city.equals("")) {
                ps = connection.prepareStatement("SELECT COUNT(rid) AS num, v.vtname AS vtname FROM Rent r, " +
                        "Vehicle v WHERE ? <= r.fromDate AND ? >= r.fromDate AND v.location = ? AND v.city = ? " +
                        "AND v.vlicense = r.vlicense GROUP BY v.vtname");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
                ps.setString(3, location);
                ps.setString(4, city);
            } else {
                throw new InputException("Need both location and city or neither!");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vtcount.put(rs.getString("vtname"), rs.getInt("num"));
            }
            counts.add(vtcount);


            // Now for the branch count.
            PreparedStatement p;
            HashMap<String, Integer> branchCount = new HashMap<>();
            if (location.equals("") && city.equals("")) {
                p = connection.prepareStatement("SELECT COUNT(rid) AS num, v.city AS city, v.location AS location " +
                        "FROM Rent r, Vehicle v WHERE ? <= r.fromDate AND ? >= r.fromDate AND v.vlicense = r.vlicense " +
                        "GROUP BY v.city, v.location");
                p.setTimestamp(1, Timestamp.valueOf(start));
                p.setTimestamp(2, Timestamp.valueOf(end));
            } else if (!location.equals("") && !city.equals("")) {
                p = connection.prepareStatement("SELECT COUNT(rid) AS num, v.city AS city, v.location AS location " +
                        "FROM Rent r, Vehicle v WHERE ? <= r.fromDate AND ? >= r.fromDate AND v.location = ? AND v.city = ? AND " +
                        "v.vlicense = r.vlicense " +
                        "GROUP BY v.city, v.location");
                p.setTimestamp(1, Timestamp.valueOf(start));
                p.setTimestamp(2, Timestamp.valueOf(end));
                p.setString(3, location);
                p.setString(4, city);
            } else {
                throw new InputException("Need both location and city or neither!");
            }
            rs = p.executeQuery();
            while (rs.next()) {
                branchCount.put(rs.getString("city") + ", " + rs.getString("location"),
                        rs.getInt("num"));
            }
            counts.add(branchCount);
        } catch (SQLException e) {
            throw new InputException("SQL Error :(");
        } catch (IllegalArgumentException e) {
            throw new InputException("Please input a date in right format!");
        }

        return counts;
    }

    // Getting Return Reports (part 1 of 3)
    public ArrayList<ReturnReport> handleReturnReport(String date, String location, String city) throws InputException {
        ArrayList<ReturnReport> reportsReturned = new ArrayList<>();
        try {
            String start = date + " 00:00:00";
            String end = date + " 23:59:59";
            PreparedStatement ps;
            if (location.equals("") && city.equals("")) {
                ps = connection.prepareStatement("SELECT ret.rid AS rid, rdate, ret.odometer AS odometer, fulltank," +
                        " value,  v.location as location, v.city as city, v.vtname as vtname FROM Rent r, Vehicle v," +
                        " Return ret WHERE ret.rid = r.rid AND ? <= rdate AND ? >= rdate AND v.vlicense = r.vlicense" +
                        " ORDER BY v.city, v.location, v.vtname");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
            } else if (!location.equals("") && !city.equals("")) {
                checkBranchExists(location, city);
                ps = connection.prepareStatement("SELECT ret.rid AS rid, rdate, ret.odometer AS odometer, fulltank," +
                        " value,  v.location as location, v.city as city, v.vtname as vtname FROM Rent r, Vehicle v," +
                        " Return ret WHERE ret.rid = r.rid AND ? <= rdate AND ? >= rdate AND v.vlicense = r.vlicense  " +
                        "AND v.location = ? AND v.city = ? " +
                        " ORDER BY v.city, v.location, v.vtname");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
                ps.setString(3, location);
                ps.setString(4, city);
            } else {
                throw new InputException("Need both location and city or neither!");
            }

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                ReturnReport r = new ReturnReport(rs.getInt("rid"), rs.getTimestamp("rdate"), rs.getInt("odometer"),
                        rs.getString("fulltank"), rs.getInt("value"), rs.getString("location"),
                        rs.getString("city"), rs.getString("vtname"));
                reportsReturned.add(r);
            }
            rs.close();
            ps.close();

            return reportsReturned;

        } catch (SQLException e) {
            throw new InputException("SQL Error :(");
        } catch (IllegalArgumentException e) {
            throw new InputException("Please input a date in right format!");
        }
    }

    // Getting Return Reports (part 2 of 3) Gets counts/revenue by vehicletype name
    // Returns Arraylist or arraylists which contain (numReturns, Revenue, Vtname)
    public ArrayList<ArrayList<String>> handleReturnReportCountVtype(String date, String location, String city) throws InputException {
        ArrayList<ArrayList<String>> counts = new ArrayList<>();
        try {
            String start = date + " 00:00:00";
            String end = date + " 23:59:59";
            PreparedStatement ps;
            if (location.equals("") && city.equals("")) {
                ps = connection.prepareStatement("SELECT COUNT(ret.rid) AS num, SUM(value) AS  revenue, " +
                        "v.vtname as vtname FROM Rent r, Vehicle v, Return ret WHERE ret.rid = r.rid AND ? <= rdate " +
                        "AND ? >= rdate AND v.vlicense = r.vlicense GROUP BY v.vtname");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
            } else if (!location.equals("") && !city.equals("")) {
                ps = connection.prepareStatement("SELECT COUNT(ret.rid) AS num, SUM(value) AS  revenue, " +
                        "v.vtname as vtname FROM Rent r, Vehicle v, Return ret WHERE ret.rid = r.rid AND ? <= rdate " +
                        "AND ? >= rdate AND v.vlicense = r.vlicense  AND v.location = ? AND v.city = ? GROUP BY v.vtname");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
                ps.setString(3, location);
                ps.setString(4, city);
            } else {
                throw new InputException("Need both location and city or neither!");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ArrayList<String> count = new ArrayList<>();
                count.add(String.valueOf(rs.getInt("num")));
                count.add(String.valueOf(rs.getInt("revenue")));
                count.add(rs.getString("vtname"));
                counts.add(count);
            }

        } catch (SQLException e) {
            throw new InputException("SQL Error :(");
        } catch (IllegalArgumentException e) {
            throw new InputException("Please input a date in right format!");
        }
        return counts;
    }

    // Getting Return Reports (part 3 of 3) Gets counts/revenue by branch
    // Returns Arraylist or arraylists which contain (numReturns, Revenue, Location, City)
    public ArrayList<ArrayList<String>> handleReturnReportCountBranch(String date, String location, String city) throws InputException {
        ArrayList<ArrayList<String>> counts = new ArrayList<>();
        try {
            String start = date + " 00:00:00";
            String end = date + " 23:59:59";
            PreparedStatement ps;
            if (location.equals("") && city.equals("")) {
                ps = connection.prepareStatement("SELECT COUNT(ret.rid) AS num, SUM(value) AS  revenue,  " +
                        "v.location as location, v.city as city FROM Rent r, Vehicle v, Return ret WHERE ret.rid = r.rid " +
                        "AND ? <= rdate AND ? >= rdate AND v.vlicense = r.vlicense GROUP BY v.city, v.location");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
            } else if (!location.equals("") && !city.equals("")) {
                ps = connection.prepareStatement("SELECT COUNT(ret.rid) AS num, SUM(value) AS  revenue,  " +
                        "v.location as location, v.city as city FROM Rent r, Vehicle v, Return ret WHERE ret.rid = r.rid " +
                        "AND ? <= rdate AND ? >= rdate AND v.vlicense = r.vlicense AND v.location = ? AND v.city = ? " +
                        "GROUP BY v.city, v.location");
                ps.setTimestamp(1, Timestamp.valueOf(start));
                ps.setTimestamp(2, Timestamp.valueOf(end));
                ps.setString(3, location);
                ps.setString(4, city);
            } else {
                throw new InputException("Need both location and city or neither!");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ArrayList<String> count = new ArrayList<>();
                count.add(String.valueOf(rs.getInt("num")));
                count.add(String.valueOf(rs.getInt("revenue")));
                count.add(rs.getString("location"));
                count.add(rs.getString("city"));
                counts.add(count);
            }

        } catch (SQLException e) {
            throw new InputException("SQL Error :(");
        } catch (IllegalArgumentException e) {
            throw new InputException("Please input a date in right format!");
        }
        return counts;
    }

    public class Login extends javax.swing.JFrame {

        /**
         * Creates new form Login
         */
        public Login() {
            initComponents();
            this.setVisible(true);
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            jLabel3 = new javax.swing.JLabel();
            Username = new javax.swing.JTextField();
            Password = new javax.swing.JTextField();
            LoginButton = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

            jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
            jLabel1.setText("Login");

            jLabel2.setText("Username");

            jLabel3.setText("Password");

            LoginButton.setText("Login");
            LoginButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    LoginButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGap(41, 41, 41)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                    .addComponent(jLabel2)
                                                                    .addGap(26, 26, 26))
                                                            .addGroup(layout.createSequentialGroup()
                                                                    .addComponent(jLabel3)
                                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(Username, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                                            .addComponent(Password)))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGap(106, 106, 106)
                                                    .addComponent(jLabel1))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGap(87, 87, 87)
                                                    .addComponent(LoginButton)))
                                    .addContainerGap(31, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel1)
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(LoginButton)
                                    .addContainerGap(12, Short.MAX_VALUE))
            );

            pack();
        }// </editor-fold>

        private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {
            try {
                login(Username.getText(), Password.getText());
                this.dispose();
            } catch (SQLException e) {
                ErrorTemplate er = new ErrorTemplate("Login Failed!");
            }
        }


        // Variables declaration - do not modify
        private javax.swing.JButton LoginButton;
        private javax.swing.JTextField Password;
        private javax.swing.JTextField Username;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        // End of variables declaration
    }
}
