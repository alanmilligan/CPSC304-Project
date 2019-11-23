package model;

import java.sql.Timestamp;

public class Rent {

    private int rid;
    private String vlicense;
    private String dlicense;
    private Timestamp fromDate;
    private Timestamp toDate;
    private int odometer;
    private String CardName;
    private int cardNo;
    private int expDate;
    private int confNo;

    public Rent(int rid, String vlicense, String dlicense, Timestamp fromDate, Timestamp toDate, int odometer, String cardName, int cardNo, int expDate, int confNo) {
        this.rid = rid;
        this.vlicense = vlicense;
        this.dlicense = dlicense;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.odometer = odometer;
        CardName = cardName.trim();
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confNo = confNo;
    }

    public int getRid() {
        return rid;
    }

    @Override
    public String toString() {
        return "Rent{" +
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
                '}';
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

    public int getConfNo() {
        return confNo;
    }

    public String[] getData() {
        String fromDate = String.valueOf(getFromDate());
        String toDate = String.valueOf(getToDate());
        String rid = String.valueOf(getRid());
        String odometer = String.valueOf(getOdometer());
        String cardNo = String.valueOf(getCardNo());
        String expDate = String.valueOf(getExpDate());
        String confNum = String.valueOf(getConfNo());
        return new String[]{rid, vlicense, dlicense, fromDate, toDate, odometer, CardName, cardNo, expDate, confNum};}
}
