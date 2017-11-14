insert into user_role (eid, name, description) values (0, 'USER', 'User');
insert into user_role (eid, name, description) values (1, 'ADMIN', 'Administrator')

insert into user (eid, username, fullname, password) values (0, 'a@a.com', 'a', 'a')
insert into user_roles (user_eid, roles_eid) values (0,0)
insert into user_roles (user_eid, roles_eid) values (0,1)


insert into financial_institution (eid, name, logo_url) values (0, 'Desjardins', 'https://www.ratehub.ca/assets/images/provider-logos/desjardins.png')
insert into financial_institution (eid, name, logo_url) values (1, 'Royal Bank', 'https://www.ratehub.ca/assets/images/provider-logos/rbc-royal-bank.png')
insert into financial_institution (eid, name, logo_url) values (2, 'CIBC', 'https://www.ratehub.ca/assets/images/provider-logos/cibc.png')
insert into financial_institution (eid, name, logo_url) values (3, 'Scotiabank', 'https://www.ratehub.ca/assets/images/provider-logos/scotiabank.png')

--insert into financial_institution (eid, name, logo_url) values (1, 'The Mortgage Emporium', ',
--insert into financial_institution (eid, name, logo_url) values (2, 'CanWise Financial', ',
--insert into financial_institution (eid, name, logo_url) values (3, 'Mortgage Intelligence Dereck Landry', ',
--insert into financial_institution (eid, name, logo_url) values (4, 'Laurentian', ',
--insert into financial_institution (eid, name, logo_url) values (5, 'Bank of Montreal', ',
--insert into financial_institution (eid, name, logo_url) values (6, 'First National', ',
--insert into financial_institution (eid, name, logo_url) values (7, 'TD Bank', ',
--insert into financial_institution (eid, name, logo_url) values (8, 'National Bank', ',
--insert into financial_institution (eid, name, logo_url) values (9, 'Duca', ',
--insert into financial_institution (eid, name, logo_url) values (10, 'MCAP', ',
--insert into financial_institution (eid, name, logo_url) values (11, 'Tangerine', ',
--insert into financial_institution (eid, name, logo_url) values (12, 'PC Financial', ',
--insert into financial_institution (eid, name, logo_url) values (13, 'Scotiabank', ',
--insert into financial_institution (eid, name, logo_url) values (14, 'HSBC', ',
--insert into financial_institution (eid, name, logo_url) values (15, 'RBC Royal Bank', ',
--insert into financial_institution (eid, name, logo_url) values (16, 'CIBC', ',


INSERT INTO ADDRESS VALUES(1,'419 rue des Navigateurs','','Gatineau','CA','J9J 2L6','QC')
INSERT INTO ADDRESS VALUES(2,'252 rue du Louvre','','Gatineau','CA','J9X 1HA','QC')
INSERT INTO ADDRESS VALUES(5,'84 rue Front','app 104','Gatineau','CA','J9H 6E9','QC')
INSERT INTO ADDRESS VALUES(6,'88 rue Front','App 403','Gatineau','CA','J9H 6E9','QC')

INSERT INTO PROPERTY VALUES(1,1990,400000,'2015-12-12 22:14:42.000000',TRUE,45.4022859,-75.8348175,'David Gaulin','2008-12-12 22:14:42.000000',275000,'1',3,1,0)
INSERT INTO PROPERTY VALUES(2,2005,460000,'2014-01-01 22:14:42.000000',TRUE,45.4409151,-75.8101482,'3plex du Louvre','2007-07-01 22:14:42.000000',342000,'3',2,2,0)
INSERT INTO PROPERTY VALUES(5,1900,88000.0E0,'1970-01-17 23:46:51.600000',TRUE,45.4019219E0,-75.8544806E0,'84 Front 104','1970-01-17 23:46:51.600000',99000.0E0,'2',-1,5,0)
INSERT INTO PROPERTY VALUES(6,1900,109000.0E0,'1970-01-17 17:12:14.400000',TRUE,45.4024085E0,-75.85459000000002E0,'88 front 403','1970-01-14 08:13:44.400000',109000.0E0,'2',-3,6,0)

-- Property Table
--		eid bigint generated by default as identity (start with 1),
--        construction_year integer not null,
--        evaluation double not null,
--        evaluation_date timestamp,
--        type varchar9255)
--        financed boolean not null,
--        name varchar(255),
--        purchase_date timestamp,
--        purchase_price double not null,
--        yearly_appreciation_percentage double not null,
--        address_eid bigint,
--        user_eid bigint not null,
--        primary key (eid)


INSERT INTO UNIT VALUES(1,1700.0E0,0,'2','3','ASD','1',1200.0E0,0)
INSERT INTO PROPERTY_UNITS VALUES(1,1)

INSERT INTO UNIT VALUES(2,900.0E0,0,'1','3','Basement','1',825.0E0,0)
INSERT INTO UNIT VALUES(3,900.0E0,0,'1','3','Middle','2',850.0E0,0)
INSERT INTO UNIT VALUES(4,900.0E0,0,'1','3','Top Floor','3',850.0E0,0)

INSERT INTO PROPERTY_UNITS VALUES(2,2)
INSERT INTO PROPERTY_UNITS VALUES(2,3)
INSERT INTO PROPERTY_UNITS VALUES(2,4)

INSERT INTO UNIT VALUES(5,900.0E0,0,'1','2','basement','104',660.0E0,0)
INSERT INTO UNIT VALUES(6,900.0E0,0,'1','2','','403',660.0E0,0)

--eid,
--area double not null, 
--area_unit integer, 
--bathrooms varchar(255), 
--bedrooms varchar(255), 
--description varchar(255), 
--number varchar(255), 
--projected_rent double not null, 
--rent_period integer

INSERT INTO PROPERTY_UNITS VALUES(5,5)
INSERT INTO PROPERTY_UNITS VALUES(6,6)

--
--        eid bigint generated by default as identity (start with 1),
--        area double not null,
--        area_unit integer,
--        bathrooms varchar(255),
--        bedrooms varchar(255),
--        description varchar(255),
--        number varchar(255),
--        projected_rent double not null,
--        rent_period integer,
--        primary key (eid)

INSERT INTO Loan VALUES (1, 20, 225000, 0, '2017-01-15 22:14:42.000000', 'daily', 5, '2008-12-01 22:14:42.000000', '1', '2021-12-01 22:14:42.000000', 60, 0, 1, 0)
INSERT INTO Loan VALUES (2, 25, 342000, 278497.68, '2016-09-27 22:14:42.000000', 'daily', 3, '2007-07-01 22:14:42.000000',  '1', '2018-12-01 22:14:42.000000', 48, 0, 2, 0)
INSERT INTO Loan VALUES (3, 25, 90000, 60348.33,  '2016-01-03 22:14:42.000000', 'daily', 3.5, '2008-12-01 22:14:42.000000', '1', '2020-08-17 22:14:42.000000', 36, 2, 5, 0)
INSERT INTO Loan VALUES (4, 25, 95000, 60166.36,  '2017-01-03 22:14:42.000000', 'daily', 3.8, '2008-12-01 22:14:42.000000', '1', '2017-11-01 22:14:42.000000', 60, 3, 6, 0)

--		  eid bigint generated by default as identity (start with 1),
--		  amortization double not null,
--        amount double not null,
--        balance double not null,
--        balance_date timestamp,
--        compounding varchar(255),
--        interest_rate double not null,
--        loan_creation_date timestamp,
--        payment_frequency varchar(255),
--        financial_institution_eid bigint,
--        property_eid bigint,
--        user_eid bigint not null,

--insert into Tenant values (1, '1977-09-01 13:00:00.000000', 'tenant1@davidgaulin.com', 'Narcisse', 'Fournier', '123-456-789', '(123)456-1789' null, null, 0)
--eid, 
--birthday timestamp,
--email varchar(255), 
--first_name varchar(255), 
--last_name varchar(255), 
--sin_ssn varchar(255),
--telephone varchar(255), -- TODO temporary
--picture_eid bigint, 
--previous_address_eid bigint, 
--user_eid bigint not null, 
--primary key (eid))	
--

--insert into lease_rate values (1, null, 660, '2015-08-01 12:01:01.000000')
--
--eid bigint generated by default as identity (start with 1), 
--end_date timestamp, 
--rate double not null, 
--start_date timestamp


--insert into lease values (1, true, true, 12, 0, 0, '2015-08-01 12:01:01.000000', 3, 0, 0, 6)
--
--eid, active boolean not null, 
--auto_renew boolean not null, 
--duration integer not null, 
--duration_unit integer, 
--lease_renewal_option integer, 
--start_date timestamp, 
--termination_notice_length integer not null, 
--termination_notice_period integer, 
--unit_eid bigint, 
--user_eid bigint not null, 
--primary key (eid))




--
--Hibernate: create table lease (eid bigint generated by default as identity (start with 1), active boolean not null, auto_renew boolean not null, duration integer not null, duration_unit integer, lease_renewal_option integer, start_date timestamp, termination_notice_length integer not null, termination_notice_period integer, unit_eid bigint, user_eid bigint not null, primary key (eid))
--Hibernate: create table lease_documents (lease_eid bigint not null, documents_eid bigint not null, primary key (lease_eid, documents_eid))
--Hibernate: create table lease_rates (lease_eid bigint not null, rates_eid bigint not null)
--Hibernate: create table lease_tenants (lease_eid bigint not null, tenants_eid bigint not null, primary key (lease_eid, tenants_eid))
--
--
--
--

INSERT INTO LEASE VALUES(1,TRUE,TRUE,12,0,0,850.0E0,'M','2017-09-30 20:00:00.000000',3,0,2,3,0)
INSERT INTO TENANT VALUES(1,NULL,'geatan','Geatan','Lafontaine',NULL,NULL,NULL,NULL,0)
INSERT INTO TENANT VALUES(2,NULL,'information@davidgaulin.com','Shakira','Shakira',NULL,NULL,NULL,NULL,0)
INSERT INTO LEASE_TENANTS VALUES(1,1)
INSERT INTO LEASE_TENANTS VALUES(1,2)
