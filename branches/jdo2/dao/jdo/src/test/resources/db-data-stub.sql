insert into currency
(id,name,symbol,iso_4217,usd_exchange_rate)
values
(1,'dollar','$','usd',1);

insert into nested_entity
(id,name,data)
values
(1,'name1','0'),
(2,'name2','1'),
(3,'name3','2');

insert into address
(id,email_address,first_name,last_name,mi,company,attn,address_1,address_2,city,province,postal_code,country,phone,fax)
values
(1,'name@domain.com','fname1','lname1','m','company1','attn1','adr1_1','adr2_1','city1','AL','00001','usa','3332220001','4445556666'),
(2,'name2@domain.com','fname2','lname2','m','company2','attn2','adr1_2','adr2_2','city2','MI','00002','usa','3332220002','4445556667'),
(3,'name3@domain.com','fname3','lname3','m','company3','attn3','adr1_3','adr2_3','city3','CA','00003','usa','3332220003','4445556668');

insert into account
(id,parent_aid,date_created,date_last_modified,status,name,billing_model,date_last_charged,ne_id,cur_id)
values
(1,null,'2005-02-13','2005-02-13',1,'asp','bm1',null,1,1),
(2,1,'2005-02-13','2005-02-13',0,'isp1','bm1',null,2,1),
(3,1,'2005-02-13','2005-02-13',1,'isp2','bm1',null,3,1),
(4,2,'2005-02-13','2005-02-13',2,'m1','bm1',null,1,1),
(5,2,'2005-02-13','2005-02-13',3,'m2','bm1',null,2,1),
(6,2,'2005-02-13','2005-02-13',4,'m3','bm1',null,3,1),
(7,3,'2005-02-13','2005-02-13',0,'c1','bm1',null,1,1),
(8,3,'2005-02-13','2005-02-13',1,'c2','bm1',null,2,1),
(9,3,'2005-02-13','2005-02-13',2,'c3','bm1',null,3,1),
(10,3,'2005-02-13','2005-02-13',3,'c4','bm1',null,1,1);

insert into account_address
(id,date_created,date_last_modified,name,type,aid,address_id)
values
(1,'2005-02-02','2005-02-02','aaname1',0,1,1),
(2,'2005-02-02','2005-02-02','aaname2',1,2,2),
(3,'2005-02-02','2005-02-02','aaname3',2,3,3),
(4,'2005-02-02','2005-02-02','aaname4',0,4,1),
(5,'2005-02-02','2005-02-02','aaname5',1,5,2),
(6,'2005-02-02','2005-02-02','aaname6',2,6,3),
(7,'2005-02-02','2005-02-02','aaname7',0,7,1),
(8,'2005-02-02','2005-02-02','aaname8',1,8,2),
(9,'2005-02-02','2005-02-02','aaname9',2,9,3),
(10,'2005-02-02','2005-02-02','aaname10',0,10,1);
