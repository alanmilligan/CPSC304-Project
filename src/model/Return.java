package model;

import java.sql.Timestamp;

public class Return {
    private int rid;
    private Timestamp rDate;
    private int odometer;
    private String fulltank;
    private int value;

    public Return(int rid, Timestamp rDate, int odometer, String fulltank, int value) {
        this.rid = rid;
        this.rDate = rDate;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
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

    @Override
    public String toString() {
        return "Return{" +
                "rid=" + rid +
                ", rDate=" + rDate +
                ", odometer=" + odometer +
                ", fulltank='" + fulltank + '\'' +
                ", value=" + value +
                '}';
    }

    public String[] getData(){
        String rid = String.valueOf(getRid());
        String rDate = String.valueOf(getrDate());
        String odometer = String.valueOf(getOdometer());
        String value = String.valueOf(getValue());
        return new String[]{rid, rDate, odometer, fulltank, value};
    }
}
