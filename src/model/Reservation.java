package model;

import java.sql.Timestamp;

public class Reservation {

    private int confNo;
    private String vtname;
    private String dlicense;
    private Timestamp fromDate;
    private Timestamp toDate;

    public Reservation(int confNo, String vtname, String dlicense, Timestamp fromDate, Timestamp toDate) {
        this.confNo = confNo;
        this.vtname = vtname.trim();
        this.dlicense = dlicense.trim();
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getConfNo() {
        return confNo;
    }

    public String getVtname() {
        return vtname;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "confNo=" + confNo +
                ", vtname=" + vtname +
                ", dlicense=" + dlicense +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}
