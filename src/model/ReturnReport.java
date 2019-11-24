package model;

import java.sql.Timestamp;

public class ReturnReport {
    private int rid;
    private Timestamp rDate;
    private int odometer;
    private String fulltank;
    private int value;
    private String location;
    private String city;
    private String vtname;

    public ReturnReport(int rid, Timestamp rDate, int odometer, String fulltank, int value, String location, String city, String vtname) {
        this.rid = rid;
        this.rDate = rDate;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
        this.location = location;
        this.city = city;
        this.vtname = vtname;
    }

    public int getRid() {
        return rid;
    }

    public Timestamp getrDate() {
        return rDate;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getFulltank() {
        return fulltank;
    }

    public int getValue() {
        return value;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }

    public String getVtname() {
        return vtname;
    }

    @Override
    public String toString() {
        return "ReturnReport{" +
                "rid=" + rid +
                ", rDate=" + rDate +
                ", odometer=" + odometer +
                ", fulltank='" + fulltank + '\'' +
                ", value=" + value +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                ", vtname='" + vtname + '\'' +
                '}';
    }
}
