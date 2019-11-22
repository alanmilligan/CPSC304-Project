DROP TABLE Return;
DROP TABLE Rent;
DROP TABLE Vehicle;
DROP TABLE Reservation;
DROP TABLE VehicleType;
DROP TABLE Customer;

CREATE TABLE VehicleType
 (vtname char(20), features char(140), wrate int, drate int, hrate int, wirate int, dirate int, hirate int, krate int,
 PRIMARY KEY (vtname));

insert into VehicleType values ('SUV', '5 seats but larger', 500, 100, 15, 50, 10, 2,  5);
insert into VehicleType values ('Truck', 'Large boy for heavy lifting', 700, 200, 20, 75, 30, 4, 10);
insert into VehicleType values ('Sedan', '5 seats and stuff',  400, 80, 10, 25, 10, 2, 4);



CREATE TABLE Customer
(cellphone char(11), name char(20), address char(40), dlicense char(7),
PRIMARY KEY (dlicense));

INSERT INTO Customer values ('17781231234', 'Lebron James', '1468 Akron St..', 'A34JK1Q');
INSERT INTO Customer values ('17781231235', 'Giannis Antekounmpo', '1421 Georgia St.', 'B54MK19');
INSERT INTO Customer values ('17783214321', 'Kobe Bryant', '9999 Bloor St.', 'MAMBA24');
INSERT INTO Customer values ('6049231234', 'Tragic Bronson', '1031 Apple Dr.', '71A8F39');


CREATE TABLE Vehicle
(vlicense char(7), make char(20), model char(20), year int, color char(20), odometer int, status char(20), vtname char(20), location char(20), city char(20),
PRIMARY KEY (vlicense),
FOREIGN KEY (vtname) REFERENCES VehicleType);

insert into Vehicle values('1144444', 'Lamborghini', 'Aventador', 2016, 'Orange', '200000', 'Available', 'Sedan', 'UBC', 'Vancouver');
insert into Vehicle values('2948323', 'Ford', 'F-150', 2010, 'Blue', '300000', 'Available', 'Truck', 'UBC', 'Vancouver');
insert into Vehicle values('2123919', 'Audi', 'Q7', 2010, 'Blue', '300000', 'Available', 'SUV', 'UBC', 'Vancouver');
insert into Vehicle values('1112333', 'Audi', 'Q7', 2010, 'Silver', '400000', 'Available', 'SUV', 'Kits', 'Vancouver');
insert into Vehicle values('2133333', 'Audi', 'R8', 2013, 'Silver', '100000', 'Available', 'Sedan', 'Kits', 'Vancouver');
insert into Vehicle values('2123239', 'BMW', 'M3', 2010, 'Blue', '300000', 'Rented', 'Sedan', 'UBC', 'Vancouver');
insert into Vehicle values('2121139', 'BMW', 'i7', 2014, 'Blue', '300000', 'Rented', 'SUV', 'UBC', 'Vancouver');
insert into Vehicle values('3433139', 'Dodge', 'RAM', 2014, 'Red', '300000', 'Rented', 'Truck', 'UBC', 'Vancouver');
insert into Vehicle values('3444139', 'Dodge', 'RAM', 2015, 'Red', '300000', 'In shop', 'Truck', 'UBC', 'Vancouver');
insert into Vehicle values('4444433', 'Audi', 'R8', 2013, 'Silver', '300000', 'In shop', 'Sedan', 'Kits', 'Vancouver');






CREATE TABLE Reservation
(confNo int, vtname char(20), dlicense char(7), fromDate timestamp, toDate timestamp,
PRIMARY KEY (confNo),
FOREIGN KEY (dlicense) REFERENCES Customer,
FOREIGN KEY (vtname) REFERENCES VehicleType);

INSERT INTO Reservation values (000000, 'SUV', 'MAMBA24', TO_TIMESTAMP('2019-01-01 05:14:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-01-15 05:14:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO Reservation values (000001, 'SUV', 'MAMBA24', TO_TIMESTAMP('2019-06-01 03:14:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-05 03:14:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO Reservation values (000002, 'Truck', '71A8F39', TO_TIMESTAMP('2019-07-01 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-23 05:10:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO Reservation values (000003, 'Sedan', 'B54MK19', TO_TIMESTAMP('2019-02-01 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-03-01 05:10:00', 'YYYY-MM-DD HH24:MI:SS'));




CREATE TABLE Rent
(rid int, vlicense char(7), dlicense char(7),  fromDate timestamp, toDate timestamp, odometer int, cardName char(30), cardNo int, ExpDate int, confNo int,
PRIMARY KEY (rid),
FOREIGN KEY (dlicense) REFERENCES Customer,
FOREIGN KEY (vlicense) REFERENCES Vehicle,
FOREIGN KEY (confNo) REFERENCES Reservation);

INSERT INTO Rent values (1, '1144444',  'B54MK19',  TO_TIMESTAMP('2019-02-01 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-03-01 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), 200000, 'Kobe Bryant', 24242424, 082024, 000000);

INSERT INTO Rent values (3, '2948323',  '71A8F39',  TO_TIMESTAMP('2019-07-01 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-23 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), 300000, 'Tragic Bronson', 242421233, 092024, 000002);

INSERT INTO Rent values (2, '2133333',  'B54MK19',  TO_TIMESTAMP('2019-07-01 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-07-23 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), 200000, 'Tragic Bronson', 24242424, 082024, NULL);

CREATE TABLE Return
(rid int, rdate timestamp, odometer int, fulltank char(1), value int,
PRIMARY KEY (rid),
FOREIGN KEY (rid) REFERENCES Rent);

INSERT INTO Return values (1, TO_TIMESTAMP('2019-03-01 05:10:00', 'YYYY-MM-DD HH24:MI:SS'), 240000, 'Y', 3849723);
