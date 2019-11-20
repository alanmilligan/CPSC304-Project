package database;

public class DataBaseHandler {




    public DataBaseHandler() {

    }

    //prompt oracle login
    public void login() {

    }

    //run sql scripts/populate database
    private void setup(){

    }


     public void addCustomer(String address, String CellNumber, String Licence, String name) {
         //INSERT INTO Customers VALUES(licence,name,cellnumber,address);

     }

     //TODO figure out return type
     public void searchCars(String type,String location,String from,String to) {

         if (type.equals("")){type = "*";}
         //some sorta blank date functionality that removes the where not exists


         //TODO figure this one out since reservations are done by type not by actual vehicle so it will be hard to count

         //SELECT V.type, V.location, from, to, COUNT(DISTINCT(V.vid))
         //FROM Vehicle V,
         //WHERE NOT EXISTS (SELECT R.rid
         //                  FROM Rent R
         //                  WHERE R.vid = V.vid AND (R.fromDate < DATE(to) AND R.toDate > DATE(from))
         //                        OR R.fromDate > DATE(to))
         //GROUP BY V.type
         //ORDER BY COUNT(DISTINCT(vid)) ASC;
    }

    //TODO figure out return type
    public void verifyReservation() {
        //Query to find there exists a car that is allowed with constraints

    }

    //TODO figure out return type
    public void findReservation() {
        //SELECT *
        //FROM Reservation R
        //WHERE R.confNumber = ConfNumber
    }




}
