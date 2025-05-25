create database garagemanagement
use garagemanagement

--tbl user
create table tblUser(
	id int identity(1,1) primary key,
	username varchar(25),
	password varchar(25),
	fullname varchar(25),
	role varchar(25)
)

insert into tblUser(fullname, username, password, role) values
('Nguyen Van Anh', 'techmanager', '123', 'technical_manager'),
('nguyen duc hoa', 'manager', '1234', 'manager'),
('le thi thu', 'thucashier', '321', 'cashier')
select * from tblUser

--tbl customer
create table tblCustomer(
	id int identity(1,1) primary key,
	fullname varchar(25),
	telnum varchar(10),
	address varchar(25),
	note varchar(50))
insert into tblCustomer(fullname, telnum, address, note) values
('Tran Binh An', '0897653458', 'nam dinh', 'kho tinh'),
('Nguyen Tran Binh An', '0112233445', 'Ha Dong', 'sam soi'),
('Tran Binh An Nhien', '1223344556', 'thanh oai', null),
('trinh van quyet', '0765987567', 'thanh oai', 'hay ki keo')

-- table car
create table tblCar(
	id int identity(1,1) primary key,
	plateNumber varchar(25),
	name varchar(25),
	brand varchar(25),
	type varchar(25))
insert into tblCar(plateNumber, name, brand, type) values
('30g-123.12', 'Mazda x6', 'mazda', '4 seats'),
('30g-123.124', 'BMW SE 2023','BMW', '4 seats'),
('27h-321.12', 'toyota cmx', 'toyota', '7 seats')
--tbl invoice
create table tblInvoice(
	id int identity(1,1) primary key,
	createdAt DATETIME default getdate(),
	userID int,
	customerID int,
	foreign key (userid) references tbluser(id),
	foreign key (customerid) references tblcustomer(id))

--tbl slot
create table tblSlot(
	id int identity(1,1) primary key,
	name varchar(10))
insert into tblSlot(name) values ('a01'),('a02'),('a03'),('b01'),('b02'), ('b03'),('c01'),('c02'),('c03')
--tbl carinvoice
create table tblCarInvoice(
	id int identity(1,1) primary key,
	carid int references tblcar(id),
	invoiceid int references tblinvoice(id),
	slotid int references tblslot(id))


--tbl sercom
create table tblSerCom(
	id int identity(1,1) primary key,
	name varchar(50),
	price float(10),
	description varchar(50),
	estimatedTime int)
INSERT INTO tblSerCom (name, price, description, estimatedTime)
VALUES
('Oil change', 100000, 'Change engine oil', 60),
('Tire Rotation', 200000, 'Rotate tires for even wear', 90),
('Tire Replacement', 200000, 'Replace all 4 tires', 90),
('Brake Inspection', 250000, 'Check brake condition', 30),
('Brake Pads Replacement', 150000, 'Change front brake pads', 45),
('Battery Check', 100000, 'Test battery health', 30),
('Coolant Flush', 250000, 'Flush and refill coolant', 40),
('Transmission Fluid Change', 300000, 'Replace transmission fluid', 60),
('Engine Diagnostic', 150000, 'Run full engine check', 20),
('Alternator Replacement', 120000, 'Replace faulty alternator', 90),
('Spark Plug Replacement', 300000, 'Install new spark plugs', 60),
('Oil Filter Replacement', 199000, 'Replace oil filter', 60),
('Battery Replacement', 300000, 'Replace with new battery', 90),
('Air Filter Replacement', 500000, 'Replace engine air filter', 60),
('Cabin Air Filter Replace', 350000, 'Replace cabin filter', 120),
('Fuel Filter Replacement', 350000, 'Change fuel filter', 90);
select * from tblSerCom
--tblService
create table tblService(
	id int references tblsercom(id) primary key)
INSERT INTO tblService(id)
VALUES
(1),   -- Oil Change
(2),   -- Tire Rotation
(3),   -- Brake Inspection
(4),   -- Battery Check
(5),  -- Coolant Flush
(6),  -- Transmission Fluid Change
(7),  -- Engine Diagnostic
(11), (8),(9), (10)
--tbl component
create table tblComponent(
	id int references tblsercom(id) primary key)

INSERT INTO tblComponent (id)
VALUES
(12),   -- Oil Filter Replacement
(13),   -- Tire Replacement
(14),   -- Brake Pads Replacement
(16),(15)
select * from tblSerCom
--tbladdedsercom
create table tblAddedSerCom(
	id int identity(1,1) primary key,
	quantity int,
	carInvoiceID int references tblcarinvoice(id),
	sercomID int references tblsercom(id))
--tbl technician
create table tblTechnician(
	id int identity(1,1) primary key,
	fullname varchar(25),
	telnum varchar(10),
	address varchar(25))

insert into tblTechnician(fullname, telnum, address) values
('nguyen duc canh', '0579367892', 'hai phong'),
('le phong', '0456834694', 'nam dinh'),
('pham thi ca', '0964385735', 'yen bai'),
('Tran Van Hieu', '0912345678', 'Ha Noi'),
('Nguyen Thi Lan', '0934567890', 'Da Nang'),
('Le Van Minh', '0987654321', 'Hue'),
('Pham Van Nam', '0901122334', 'Ho Chi Minh City'),
('Bui Thi Hoa', '0977008899', 'Can Tho');
-- tbl techservice
create table tblTechService(
	id int identity(1,1) primary key,
	timestart datetime,
	timeend datetime,
	serviceID int references tblAddedSerCom(id),
	technicianID int references tblTechnician(id))


insert into tblTechService(timestart, timeend, serviceID, technicianID) values 
('2025-05-23 08:00', '2025-05-23 09:30', 1, 1),
('2025-05-23 08:00', '2025-05-23 09:30', 1, 2),
('2025-05-23 08:35', '2025-05-23 09:35', 2, 3),
('2025-05-23 08:35', '2025-05-23 10:05', 3, 4)

insert into tblCarInvoice(carid, invoiceid, slotid) values
(3, 1, 1),
(2, 2, 2)

insert into tblInvoice(createdat, userid, customerid) values
('2025-05-23 07:58', 1, 3),
('2025-05-23 08:30', 1, 2)

insert into tblAddedSerCom(quantity, carInvoiceID, sercomID) values
(1, 1, 2),
(2, 2, 1),
(1, 2, 9)





select * from tblInvoice

