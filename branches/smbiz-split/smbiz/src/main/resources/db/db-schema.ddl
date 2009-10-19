-- stand alone related
create table app_property (
   id int not null,
   version int not null default 0,
   name varchar(128) not null,
   value varchar(255) not null,
   primary key (id),
   unique name (name)
);

create table address (   
   id int not null,
   version int not null default 0,
   last_name varchar(64),
   first_name varchar(64),
   mi char(1),
   company varchar(64),
   attn varchar(64),
   address_1 varchar(128) not null,
   address_2 varchar(128),
   city varchar(128) not null,
   province varchar(128) not null,
   postal_code varchar(15) not null,
   country varchar(128) not null,
   phone varchar(15),
   fax varchar(15),
   email_address varchar(128),
   unique address1__postal_code (address_1, postal_code),
   primary key (id)
);

create table currency (
   id int not null,
   version int not null default 0,
   name varchar(32) not null unique,
   symbol varchar(8) not null unique,
   iso_4217 varchar(16) not null unique,
   usd_exchange_rate float,
   unique(iso_4217),
   primary key (id)
);

create table payment_info (
   id int not null,
   version int not null default 0,
   name varchar(64) not null,
   data blob not null,
   unique(name),
   primary key (id)
);

create table payment_trans (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   pay_trans_date datetime not null,
   pay_op tinyint not null,
   pay_type tinyint not null,
   amount float,
   payment_processor tinyint,
   auth_num varchar(32),
   ref_num varchar(32) not null unique,
   response varchar(32),
   response_msg varchar(128),
   notes text,
   unique(pay_trans_date,pay_op,pay_type),
   primary key (id)
);

-- account related
create table account (
   id int not null,
   version int not null default 0,
   account_type tinyint not null,
   date_created datetime not null,
   date_last_modified datetime not null,
   parent_aid int,
   status tinyint not null,
   name varchar(64) not null,
   persist_pymnt_info boolean,
   billing_model varchar(32),
   billing_cycle varchar(32),
   date_last_charged datetime,
   next_charge_date datetime,
   date_cancelled datetime,
   pi_id int,
   cur_id int,
   store_name varchar(128),
   unique(name),
   primary key (id)
);

create table account_address (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   name varchar(32) not null,
   type tinyint not null,
   aid int not null,
   address_id int not null,
   unique(aid,address_id),
   unique(aid,name),
   primary key (id)
);

create table customer_account (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   source tinyint not null,
   status tinyint not null,
   customer_id int not null,
   aid int not null,
   visitor_id int,
   unique(aid,customer_id),
   primary key (id)
);

create table account_history (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   trans_date datetime not null,
   status tinyint not null,
   notes text,
   aid int not null,
   pt_id int,
   unique(aid, trans_date, status),
   primary key (id)
);

create table visitor (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   remote_host varchar(64) not null,
   remote_addr varchar(64),
   remote_user varchar(64),
   mc varchar(16),
   aid int not null,
   unique(aid,date_created,remote_host),
   primary key (id)
);

-- user related
create table user (
	id int not null,
	version int not null default 0,
	date_created datetime not null,
	date_last_modified datetime not null,
	name varchar(50),
	aid int not null,
	adr_id int,
	email_address varchar(128) not null,
	password varchar(255) not null,
	enabled boolean not null,
	locked boolean not null,
	expires datetime not null,
	unique(email_address),
	primary key (id)
);

create table authority (
	id int not null,
	version int not null default 0,
	authority varchar(50) not null,
	primary key(id)
);
alter table authority add unique index un_authority_authority (authority);

create table user_authority (
	uid int not null,
	aid int not null,
	primary key(uid,aid)
);
alter table user_authority 
	add constraint fk_ua foreign key (uid) references user(id) on delete cascade,
	add constraint fk_aa foreign key (aid) references authority(id) on delete cascade;

-- interface related
create table interface (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   type tinyint not null,
   code varchar(50) not null,
   name varchar(50) not null,
   description varchar(128),
   is_available_asp boolean not null default false,
   is_available_isp boolean not null default false,
   is_available_merchant boolean not null default false,
   is_available_customer boolean not null default false,
   is_required_asp boolean not null default false,
   is_required_isp boolean not null default false,
   is_required_merchant boolean not null default false,
   is_required_customer boolean not null default false,
   unique(code),
   primary key (id)
);

-- (for both interface options and io param defs)
create table iopd (
   id int not null,
   interface_id int default null,
   option_id int default null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   record_type varchar(16) not null,
   code varchar(50) not null,
   name varchar(50) not null,
   description varchar(128),
   is_default boolean not null default false,
   set_up_cost float not null default 0,
   monthly_cost float not null default 0,
   annual_cost float not null default 0,
   base_setup_price float not null default 0,
   base_monthly_price float not null default 0,
   base_annual_price float not null default 0,
   unique(code),
   primary key (id)
);

alter table iopd
	add index fk_io_i (interface_id), 
	add constraint fk_io_i foreign key (interface_id) references interface (id) on delete cascade,
	add index fk_io_iopd (option_id), 
	add constraint fk_io_iopd foreign key (option_id) references iopd (id) on delete cascade;

create table ioa (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   set_up_price float not null default 0,
   monthly_price float not null default 0,
   annual_price float not null default 0,
   option_id int,
   aid int not null,
   unique(option_id, aid),
   primary key (id)
);

-- interface option account
alter table ioa
	add index fk_ia_a (aid), 
	add index fk_ia_i (option_id), 
	add constraint fk_ia_a foreign key (aid) references account (id) on delete cascade,
	add constraint fk_ia_i foreign key (option_id) references iopd (id) on delete cascade;

-- interface option account parameter
create table ioap (
	ioaid int not null,
	name varchar(50) not null,
	value varchar(255),
	primary key (ioaid, name)
);

alter table ioap 
	add constraint fk_ioa_param foreign key (ioaid) references ioa(id) on delete cascade;


-- product related
create table product_general (
   id int not null,
   version int not null default 0,
   d1 varchar(255) not null,
   d2 varchar(255) not null,
   d3 varchar(255) not null,
   image1 varchar(64),
   image2 varchar(64),
   unique(d1,d2),
   primary key (id)
);

create table product_inventory (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   sku varchar(64) not null,
   status tinyint not null,
   retail_price float not null default 0,
   sales_price float not null default 0,
   weight float not null default 0,
   on_sale boolean not null default false,
   aux_descriptor varchar(255),
   inv_in_stock integer not null default 0,
   inv_committed integer not null default 0,
   inv_reorder_level integer not null default 0,
   pg_id int,
   aid int not null,
   unique(aid,sku),
   primary key (id)
);

create table product_category (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   name varchar(128) not null,
   description varchar(255),
   image varchar(64),
   aid int not null,
   unique(aid,name),
   primary key (id)
);

create table prod_cat (
   id int not null,
   version int not null default 0,
   is_featured_product boolean not null default false,
   prodinv_id int,
   prodcat_id int,
   unique(prodinv_id,prodcat_id),
   primary key (id)
);

create table pch (
   id int not null,
   version int not null default 0,
   parent_id int,
   child_id int,
   unique(parent_id,child_id),
   primary key (id)
);

-- merchant account related
create table sales_tax (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   province varchar(64) not null,
   county varchar(64) not null,
   postal_code varchar(16) not null,
   tax float not null,
   aid int not null,
   unique(aid,province,county,postal_code),
   primary key (id)
);

create table ship_mode (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   name varchar(32) not null,
   type tinyint not null,
   surcharge float,
   src_zip varchar(16),
   aid int not null,
   unique(aid,name),
   primary key (id)
);

create table ship_bound_cost (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   l_bound float not null,
   u_bound float not null,
   cost float not null default 0,
   sm_id int not null,
   unique(sm_id,l_bound,u_bound),
   primary key (id)
);

create table site_code (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   code varchar(16) not null,
   name varchar(64),
   expiration_date datetime not null,
   aid int not null,
   unique(code),
   primary key (id)
);

-- order related
create table orders (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   aid int not null,
   cust_id int,
   date_last_modified datetime not null,
   status tinyint not null,
   notes varchar(255),
   site_code varchar(32),
   visitor_id int,
   crncy_id int,
   pymntinfo_id int,
   billto_adr_id int,
   shipto_adr_id int,
   primary key (id)
   -- we can't guarantee this w/ enough certainty!!!
   --unique (date_created, aid, cust_id)
);

create table order_item (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   sku varchar(64) not null,
   o_id int not null,
   item_status tinyint not null,
   pay_status tinyint not null,
   qty integer,
   price float,
   weight float,
   name varchar(128),
   description varchar(255),
   image varchar(32),
   primary key (id),
   unique (sku, o_id)
);

create table order_trans (
   id int not null,
   version int not null default 0,
   date_created datetime not null,
   date_last_modified datetime not null,
   username varchar(32) not null,
   order_trans_op tinyint not null,
   order_trans_result tinyint not null,
   ship_mode_name varchar(64),
   ship_routing_num varchar(64),
   item_total float,
   sales_tax float,
   ship_cost float,
   total float,
   o_id int not null,
   bta_id int,
   sta_id int,
   pi_id int,
   pt_id int,
   unique(o_id, date_created, username),
   primary key (id)
);

create table order_item_trans (
   id int not null,
   version int not null default 0,
   order_item_trans_op tinyint not null,
   amount float,
   oi_id int not null,
   ot_id int not null,
   unique(oi_id,ot_id),
   primary key (id)
);

alter table account 
	add index fk_prnt_acnt (parent_aid), 
	add constraint fk_prnt_acnt foreign key (parent_aid) references account (id) on delete set null,	
	add index fk_a_pi (pi_id), 
	add constraint fk_a_pi foreign key (pi_id) references payment_info (id) on delete set null,
	add index fk_a_c (cur_id), 
	add constraint fk_a_c foreign key (cur_id) references currency (id) on delete set null;

alter table visitor 
	add index fk_s_a (aid), 
	add constraint fk_s_a foreign key (aid) references account (id) on delete cascade;

alter table customer_account 
	add index fk_ca_s (visitor_id), 
	add index fk_ca_c (customer_id), 
	add index fk_ca_a (aid), 
	add constraint fk_ca_s foreign key (visitor_id) references visitor (id) on delete set null,
	add constraint fk_ca_c foreign key (customer_id) references account (id) on delete cascade,
	add constraint fk_ca_a foreign key (aid) references account (id) on delete cascade;

alter table user 
	add index fk_au_a (aid), 
	add index fk_au_adr (adr_id), 
	add constraint fk_au_a foreign key (aid) references account (id) on delete cascade,
	add constraint fk_au_adr foreign key (adr_id) references address (id) on delete set null;

alter table account_history 
	add index fk_ah_pt (pt_id), 
	add index fk_ah_a (aid), 
	add constraint fk_ah_pt foreign key (pt_id) references payment_trans (id) on delete set null,
	add constraint fk_ah_a foreign key (aid) references account (id) on delete cascade;

alter table account_address 
	add index fk_aadr_adr (address_id), 
	add index fk_aadr_a (aid), 
	add constraint fk_aadr_adr foreign key (address_id) references address (id) on delete cascade,
	add constraint fk_aadr_a foreign key (aid) references account (id) on delete cascade;

alter table sales_tax 
	add index fk_st_a (aid), 
	add constraint fk_st_a foreign key (aid) references account (id) on delete cascade;

alter table site_code 
	add index fk_sc_a (aid), 
	add constraint fk_sc_a foreign key (aid) references account (id) on delete cascade;

alter table ship_mode 
	add index fk_sm_a (aid), 
	add constraint fk_sm_a foreign key (aid) references account (id) on delete cascade;

alter table ship_bound_cost 
	add index fk_sbc_sm (sm_id), 
	add constraint fk_sbc_sm foreign key (sm_id) references ship_mode (id) on delete cascade;

alter table product_inventory 
	add index fk_pi_pg (pg_id), 
	add constraint fk_pi_pg foreign key (pg_id) references product_general (id) on delete set null,
	add index fk_pi_a (aid), 
	add constraint fk_pi_a foreign key (aid) references account (id) on delete cascade;

alter table product_category 
	add index fk_pc_a (aid), 
	add constraint fk_pc_a foreign key (aid) references account (id) on delete cascade;
	
alter table prod_cat 
	add index fk_prodcat_pc (prodcat_id), 
	add index fk_prodcat_pi (prodinv_id), 
	add constraint fk_prodcat_pc foreign key (prodcat_id) references product_category (id) on delete cascade,
	add constraint fk_prodcat_pi foreign key (prodinv_id) references product_inventory (id) on delete cascade;

alter table pch 
	add index pch_child (child_id), 
	add index pch_parent (parent_id), 
	add constraint pch_child foreign key (child_id) references product_category (id) on delete cascade,
	add constraint pch_parent foreign key (parent_id) references product_category (id) on delete cascade;

alter table orders 
	add index fk_o_crncy (crncy_id), 
	add index fk_o_s (visitor_id), 
	add index fk_o_pi (pymntinfo_id), 
	add index fk_o_adr_ship (shipto_adr_id), 
	add index fk_o_a (aid), 
	add index fk_o_c (cust_id), 
	add index fk_o_adr_bill (billto_adr_id), 
	add constraint fk_o_a foreign key (aid) references account (id) on delete cascade,
	add constraint fk_o_s foreign key (visitor_id) references visitor (id) on delete set null,
	add constraint fk_o_pi foreign key (pymntinfo_id) references payment_info (id) on delete set null,
	add constraint fk_o_adr_ship foreign key (shipto_adr_id) references address (id) on delete set null,
	add constraint fk_o_c foreign key (cust_id) references account (id) on delete set null,
	add constraint fk_o_adr_bill foreign key (billto_adr_id) references address (id) on delete set null,
	add constraint fk_o_crncy foreign key (crncy_id) references currency (id) on delete set null;

alter table order_item 
	add index fk_oi_o (o_id), 
	add constraint fk_oi_o foreign key (o_id) references orders (id) on delete cascade;

alter table order_trans 
	add index fk_ot_adr_bill (bta_id), 
	add index fk_ot_pi (pi_id), 
	add index fk_ot_pt (pt_id), 
	add index fk_ot_o (o_id), 
	add index fk_ot_adr_ship (sta_id), 
	add constraint fk_ot_o foreign key (o_id) references orders (id) on delete cascade,
	add constraint fk_ot_adr_bill foreign key (bta_id) references address (id) on delete set null,
	add constraint fk_ot_pi foreign key (pi_id) references payment_info (id) on delete set null,
	add constraint fk_ot_pt foreign key (pt_id) references payment_trans (id) on delete set null,
	add constraint fk_ot_adr_ship foreign key (sta_id) references address (id) on delete set null;

alter table order_item_trans 
	add index fk_oit_ot (ot_id), 
	add index fk_oit_oi (oi_id), 
	add constraint fk_oit_ot foreign key (ot_id) references order_trans (id) on delete cascade,
	add constraint fk_oit_oi foreign key (oi_id) references order_item (id) on delete cascade;


create table hibernate_unique_key (
    next_hi integer 
);
insert into hibernate_unique_key values ( 0 );
