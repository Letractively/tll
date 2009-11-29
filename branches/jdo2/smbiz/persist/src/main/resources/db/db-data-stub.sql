insert into app_property
(id, name, value)
values
(1, 'locale', 'US')
,(2, 'default.iso4217', 'usd')
,(3, 'default.country', 'usa');

insert into currency
(id,name,symbol,iso_4217,usd_exchange_rate)
values
(1,'dollar','$','usd',1);

insert into address
(id,email_address,first_name,last_name,mi,company,attn,address_1,address_2,city,province,postal_code,country,phone,fax)
values
(1,'name@domain.com','jpk','smith','l','TheLogicLab','Mr.','88bway','suite#1','city','st','88776','usa','3332220000','4445556666');

--insert into payment_info
--(id,data,name)
--values
--(1,'','pi1')
--,(2,'','pi2');

insert into account
(id,account_type,date_created,date_last_modified,status,name,persist_pymnt_info,billing_model,billing_cycle,date_last_charged,next_charge_date,date_cancelled,pi_id,cur_id,store_name,parent_aid)
values
(1,'asp','2005-02-13','2005-02-13',1,'asp',1,'billing_model','billing_cycle',null,null,null,null,1,null,null),
(2,'isp','2005-02-13','2005-02-13',1,'isp1',1,null,null,null,null,null,null,1,null,1),
(3,'isp','2005-02-13','2005-02-13',1,'isp2',1,null,null,null,null,null,null,1,null,1),
(4,'merchant','2005-02-13','2005-02-13',1,'m1',1,null,null,null,null,null,null,1,'store1',1),
(5,'merchant','2005-02-13','2005-02-13',1,'m2',1,null,null,null,null,null,null,1,'store2',2),
(6,'merchant','2005-02-13','2005-02-13',1,'m3',1,null,null,null,null,null,null,1,'store3',3),
(7,'customer','2005-02-13','2005-02-13',1,'c1',1,null,null,null,null,null,null,1,null,null),
(8,'customer','2005-02-13','2005-02-13',1,'c2',1,null,null,null,null,null,null,1,null,null),
(9,'customer','2005-02-13','2005-02-13',1,'c3',1,null,null,null,null,null,null,1,null,null),
(10,'customer','2005-02-13','2005-02-13',1,'c4',1,null,null,null,null,null,null,1,null,null);

insert into account_address
(id,date_created,date_last_modified,name,type,aid,address_id)
values
(1,'2005-02-02','2005-02-02','name',0,1,1);

insert into account_history
(id,date_created,date_last_modified,trans_date,status,notes,aid,pt_id)
values
(1,'2005-02-02','2005-02-02','2005-02-02',1,'notes',1,null);

insert into user
(id,date_created,date_last_modified,name,aid,adr_id,email_address,password,enabled,locked,expires) 
values
(1,'2005-02-02','2005-02-02','jpk',1,1,'jopaki@gmail.com','0b3bbae8eb5a9d2e9bd1043611a9be38',true,false,'2010-01-01');

INSERT INTO authority 
(id,authority)
VALUES 
(1,'ROLE_ADMINISTRATOR'),
(2,'ROLE_USER'),
(3,'ROLE_ANONYMOUS');

insert into user_authority
(uid, aid)
values
(1,1);

insert into visitor
(id,date_created,date_last_modified,remote_host,remote_addr,remote_user,mc,aid)
values
(1,'2005-02-02','2005-02-02','remote host','remote addr','remote user','mc',1);

insert into customer_account
(id,date_created,date_last_modified,source,status,customer_id,aid,visitor_id)
values
(1,'2005-02-02','2005-02-02',0,1,7,2,1);

insert into site_code
(id,date_created,date_last_modified,code,name,expiration_date,aid)
values
(1,'2005-02-02','2005-02-02','mc','mc','2006-02-15',1);


-- interface records
insert into interface
(id, type, date_created, date_last_modified, 
	code, name, description,
	is_available_asp, is_available_isp, is_available_merchant, is_available_customer, 
	is_required_asp, is_required_isp, is_required_merchant, is_required_customer)
values

(1,1, '2005-02-13','2005-02-13'
,'pymntproc','Payment Processor','gateway for online payments'
,true,true,true,true
,true,true,true,true
)


,(2,1,'2005-02-13','2005-02-13'
,'shipmethod','Shipping Method','method of shipmennt'
,true,true,true,true
,true,true,true,true
)

,(3,1,'2005-02-13','2005-02-13'
,'salestax','Sales Tax','sales tax model'
,true,true,true,true
,true,true,true,true
)

,(4,2,'2005-02-13','2005-02-13'
,'pymntmethod','Payment Method','Supported payment methods'
,true,true,true,true
,true,true,true,true
)

,(5,0,'2005-02-13','2005-02-13'
,'crosssell','Cross Selling','Merchant product cross selling'
,false,false,true,false
,false,false,false,false
);

-- Interface Option records
insert into iopd
(id, record_type, date_created, date_last_modified, 
	code, name, description,
	is_default, 
	set_up_cost, monthly_cost, annual_cost, 
	base_setup_price, base_monthly_price, base_annual_price, 
	interface_id)
values

(1,'option','2005-02-13','2005-02-13'
,'native pay proc','Native','Native payment processor'
,true
,0,0,0
,0,0,0
,1)

,(2,'option','2005-02-13','2005-02-13'
,'verisign','VeriSign','VeriSign payment processor'
,true
,0,0,0
,0,0,0
,1)

,(3,'option','2005-02-13','2005-02-13'
,'native ship method','Native Shipping Method','native method of shipmennt'
,false
,0,0,0
,0,0,0
,2)

,(4,'option','2005-02-13','2005-02-13'
,'native sales tax','Native Sales Tax','native sales tax model'
,false
,0,0,0
,0,0,0
,3)

,(5,'option','2005-02-13','2005-02-13'
,'visa','VISA','visa'
,true
,0,0,0
,0,0,0
,4)

,(6,'option','2005-02-13','2005-02-13'
,'mc','MasterCard','MasterCard'
,false
,0,0,0
,0,0,0
,4)

,(7,'option','2005-02-13','2005-02-13'
,'crosssell-switch','Cross Selling Switch',''
,false
,0,0,0
,0,0,0
,5)
;

-- Interface Option Parameter Definition records
insert into iopd
(id, record_type, date_created, date_last_modified, 
code, name, description, option_id)
values

(201, 'paramdef', '2005-02-13', '2005-02-13', 'verisignP1', 'VeriSign Parameter 1', 'VeriSign Parameter 1 description', 2)
,(202, 'paramdef', '2005-02-13', '2005-02-13', 'verisignP2', 'VeriSign Parameter 2', 'VeriSign Parameter 2 description', 2)
;


insert into product_general
(id,d1,d2,d3,image1,image2)
values
(1,'pg1_d1','pg1_d2','pg1_d3','pg1_image1','pg1_image2')
,(2,'pg2_d2','pg2_d2','pg2_d3','pg2_image2','pg2_image2')
,(3,'pg3_d3','pg3_d3','pg3_d3','pg3_image3','pg3_image3');

insert into product_inventory
(id,date_created,date_last_modified,sku,status,retail_price,sales_price,weight,on_sale,aux_descriptor,inv_in_stock,inv_committed,inv_reorder_level,pg_id,aid)
values
 (1,'2005-02-17','2005-02-17','sku1',0,2.22,1.98,2.01,0,'auxdescriptor1',88,2,20,1,4)
,(2,'2005-02-17','2005-02-17','sku2',0,3.56,5.99,5,0,'auxdescriptor2',88,2,20,2,4)
,(3,'2005-02-17','2005-02-17','sku3',0,66.31,20,1,1,'auxdescriptor3',88,2,20,3,4)
,(4,'2005-02-17','2005-02-17','sku1',0,2.22,1.98,2.01,0,'auxdescriptor1',88,2,20,1,5)
,(5,'2005-02-17','2005-02-17','sku2',0,3.56,5.99,5,0,'auxdescriptor2',88,2,20,2,5)
,(6,'2005-02-17','2005-02-17','sku3',0,66.31,20,1,1,'auxdescriptor3',88,2,20,3,5)
,(7,'2005-02-17','2005-02-17','sku1',0,2.22,1.98,2.01,0,'auxdescriptor1',88,2,20,1,6)
,(8,'2005-02-17','2005-02-17','sku2',0,3.56,5.99,5,0,'auxdescriptor2',88,2,20,2,6)
,(9,'2005-02-17','2005-02-17','sku3',0,66.31,20,1,1,'auxdescriptor3',88,2,20,3,6);

insert into product_category
(id,date_created,date_last_modified,name,description,image,aid)
values
(1,'2005-02-13','2005-02-13','pc1','',null,4)
,(2,'2005-02-13','2005-02-13','pc2','',null,4)
,(3,'2005-02-13','2005-02-13','pc3','',null,4)
,(4,'2005-02-13','2005-02-13','pc4','',null,4)
,(5,'2005-02-13','2005-02-13','pc5','',null,4)
,(6,'2005-02-13','2005-02-13','pc6','',null,4)
,(7,'2005-02-13','2005-02-13','pc7','',null,4)
,(8,'2005-02-13','2005-02-13','pc8','',null,4)
,(9,'2005-02-13','2005-02-13','pc9','',null,4)
,(10,'2005-02-13','2005-02-13','pc10','',null,4)
,(11,'2005-02-16','2005-02-16','pc1','',null,5)
,(12,'2005-02-16','2005-02-16','pc2','',null,5)
,(13,'2005-02-16','2005-02-16','pc1','',null,6);

insert into prod_cat
(id,is_featured_product,prodinv_id,prodcat_id)
values
 (1,0,1,1)
,(2,0,2,2)
,(3,0,3,3)
,(4,0,1,4)
,(5,0,2,5)
,(6,0,3,6)
,(7,0,1,7)
,(8,0,2,8)
,(9,0,3,9)
,(10,0,1,10);

insert into pch
(id,child_id,parent_id)
values
 (1,1,null)
,(2,2,null)
,(3,3,null)
,(4,4,1)
,(5,5,1)
,(6,6,1)
,(7,7,2)
,(8,8,2)
,(9,9,7)
,(10,10,9);

insert into payment_trans
(id,date_created,date_last_modified,pay_trans_date,pay_op,pay_type,amount,payment_processor,auth_num,ref_num,response,response_msg,notes)
values
(1,'2005-02-13','2005-02-13','2005-02-13',0,1,22.32,0,'auth_num','ref_num','response','response_msg','notes');

insert into orders
(id,date_created,date_last_modified,status,notes,site_code,aid,visitor_id,cust_id,crncy_id,pymntinfo_id,billto_adr_id,shipto_adr_id)
values
(1,'2005-02-13','2005-02-13',0,'notes','site_code',4,1,7,1,null,1,1);

insert into order_item
(id,date_created,date_last_modified,item_status,pay_status,qty,price,weight,sku,name,description,image,o_id)
values
(1,'2005-02-13','2005-02-13',0,0,1,3.33,99,'sku1','pg1_d1','pg1_d2','image.jpg',1);

insert into order_trans
(id,date_created,date_last_modified,username,order_trans_op,order_trans_result,ship_mode_name,item_total,sales_tax,ship_cost,total,o_id,bta_id,sta_id,pi_id,pt_id)
values
(1,'2005-02-13','2005-02-13','username',0,0,'UPS',22.23,1.23,0.33,26.78,1,1,1,null,1);

insert into order_item_trans
(id,order_item_trans_op,amount,oi_id,ot_id)
values
(1,0,22.23,1,1);
