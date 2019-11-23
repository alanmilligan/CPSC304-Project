package model;

public class Customer {

    private String cell;
    private String name;
    private String address;
    private String dlicense;

    public Customer(String cell, String name, String address, String dlicense) {
        this.cell = cell.trim();
        this.name = name.trim();
        this.address = address.trim();
        this.dlicense = dlicense.trim();
    }

    public String getCell() {
        return cell;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDlicense() {
        return dlicense;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cell='" + cell + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", dlicense='" + dlicense + '\'' +
                '}';
    }

    public String[] getData(){
        return new String[]{cell, name, address, dlicense};
    }
}

