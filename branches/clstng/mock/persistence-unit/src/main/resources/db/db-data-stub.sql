insert into currency
(id,name,symbol,iso_4217,usd_exchange_rate)
values
(1,'dollar','$','usd',1);

insert into address
(id,email_address,first_name,last_name,mi,company,attn,address_1,address_2,city,province,postal_code,country,phone,fax)
values
(1,'name@domain.com','jpk','smith','l','TheLogicLab','Mr.','88bway','suite#1','city','st','88776','usa','3332220000','4445556666');

insert into account
(id,account_type,date_created,date_last_modified,status,name,persist_pymnt_info,billing_model,billing_cycle,date_last_charged,next_charge_date,date_cancelled,pi_id,cur_id,store_name,parent_aid)
values
(1,0,'2005-02-13','2005-02-13',1,'asp',1,'billing_model','billing_cycle',null,null,null,null,1,null,null),
(2,1,'2005-02-13','2005-02-13',1,'isp1',1,null,null,null,null,null,null,1,null,1),
(3,1,'2005-02-13','2005-02-13',1,'isp2',1,null,null,null,null,null,null,1,null,1),
(4,2,'2005-02-13','2005-02-13',1,'m1',1,null,null,null,null,null,null,1,'store1',1),
(5,2,'2005-02-13','2005-02-13',1,'m2',1,null,null,null,null,null,null,1,'store2',2),
(6,2,'2005-02-13','2005-02-13',1,'m3',1,null,null,null,null,null,null,1,'store3',3),
(7,3,'2005-02-13','2005-02-13',1,'c1',1,null,null,null,null,null,null,1,null,null),
(8,3,'2005-02-13','2005-02-13',1,'c2',1,null,null,null,null,null,null,1,null,null),
(9,3,'2005-02-13','2005-02-13',1,'c3',1,null,null,null,null,null,null,1,null,null),
(10,3,'2005-02-13','2005-02-13',1,'c4',1,null,null,null,null,null,null,1,null,null);

insert into account_address
(id,date_created,date_last_modified,name,type,aid,address_id)
values
(1,'2005-02-02','2005-02-02','name',0,1,1);
