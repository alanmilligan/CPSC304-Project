package model;

import java.sql.Timestamp;

public class RentReport {

    private int rid;
    private String vlicense;
    private String dlicense;
    private Timestamp fromDate;
    private Timestamp toDate;
    private int odometer;
    private String CardName;
    private int cardNo;
    private int expDate;
    private Integer confNo;
    private String location;
    private String city;
    private String vtname;

    public RentReport(int rid, String vlicense, String dlicense, Timestamp fromDate, Timestamp toDate, int odometer, String cardName, int cardNo, int expDate, Integer confNo, String location, String city, String vtname) {
        this.rid = rid;
        this.vlicense = vlicense;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.odometer = odometer;
        CardName = cardName;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
        this.location = location;
        this.city = city;
        this.vtname = vtname;
    }

    public int getRid() {
        return rid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getDlicense() {
        return dlicense;
    }

    public Timestamp getFromDate() {
        return fromDate;
    }

    public Timestamp getToDate() {
        return toDate;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getCardName() {
        return CardName;
    }

    public int getCardNo() {
        return cardNo;
    }

    public int getExpDate() {
        return expDate;
    }

    public Integer getConfNo() {
        return confNo;
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
        return "RentReport{" +
                "rid=" + rid +
                ", vlicense='" + vlicense + '\'' +
                ", dlicense='" + dlicense + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", odometer=" + odometer +
                ", CardName='" + CardName + '\'' +
                ", cardNo=" + cardNo +
                ", expDate=" + expDate +
                ", confNo=" + confNo +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                ", vtname='" + vtname + '\'' +
                '}';
    }
}
