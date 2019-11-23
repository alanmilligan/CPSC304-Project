package model;

public class Vehicle {
    private String vlicense;
    private String make;
    private String model;
    private int year;
    private String color;
    private int odometer;
    private String status;
    private String vtname;
    private String location;
    private String city;

    public Vehicle(String vlicense, String make, String model, int year, String color, int odometer, String status, String vtname, String location, String city) {
        this.vlicense = vlicense;
        this.make = make.trim();
        this.model = model.trim();
        this.year = year;
        this.color = color.trim();
        this.odometer = odometer;
        this.status = status.trim();
        this.vtname = vtname.trim();
        this.location = location.trim();
        this.city = city.trim();
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getStatus() {
        return status;
    }

    public String getVtname() {
        return vtname;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vlicense='" + vlicense + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", color='" + color + '\'' +
                ", odometer=" + odometer +
                ", status='" + status + '\'' +
                ", vtname='" + vtname + '\'' +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public String[] getData(){
        String year = String.valueOf(getYear());
        String odometer = String.valueOf(getOdometer());
        return new String[]{vlicense, make, model, year, color, odometer, status, vtname, location, city};
    }
}
