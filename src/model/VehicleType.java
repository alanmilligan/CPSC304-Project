package model;

public class VehicleType {

    private String vtname;
    private String features;
    private int wrate;
    private int drate;
    private int hrate;
    private int wirate;
    private int dirate;
    private int hirate;
    private int krate;

    public VehicleType(String vtname, String features, int wrate, int drate, int hrate, int wirate, int dirate, int hirate, int krate) {
        this.vtname = vtname.trim();
        this.features = features.trim();
        this.wrate = wrate;
        this.drate = drate;
        this.hrate = hrate;
        this.wirate = wirate;
        this.dirate = dirate;
        this.hirate = hirate;
        this.krate = krate;
    }

    public String getVtname() {
        return vtname;
    }

    public String getFeatures() {
        return features;
    }

    public int getWrate() {
        return wrate;
    }

    public int getDrate() {
        return drate;
    }

    public int getHrate() {
        return hrate;
    }

    public int getWirate() {
        return wirate;
    }

    public int getDirate() {
        return dirate;
    }

    public int getHirate() {
        return hirate;
    }

    public int getKrate() {
        return krate;
    }

    @Override
    public String toString() {
        return "VehicleType{" +
                "vtname='" + vtname + '\'' +
                ", features='" + features + '\'' +
                ", wrate=" + wrate +
                ", drate=" + drate +
                ", hrate=" + hrate +
                ", wirate=" + wirate +
                ", dirate=" + dirate +
                ", hirate=" + hirate +
                ", krate=" + krate +
                '}';
    }

    public String[] getData(){
        String wrate = String.valueOf(getWrate());
        String drate = String.valueOf(getDrate());
        String hrate = String.valueOf(getHrate());
        String wirate = String.valueOf(getWirate());
        String dirate = String.valueOf(getDirate());
        String hirate = String.valueOf(getHirate());
        String krate = String.valueOf(getKrate());
        return new String[]{vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate};
    }
}
