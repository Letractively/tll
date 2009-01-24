-- stand alone related
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

alter table account 
	add index fk_prnt_acnt (parent_aid), 
	add constraint fk_prnt_acnt foreign key (parent_aid) references account (id) on delete set null,	
	add index fk_a_c (cur_id), 
	add constraint fk_a_c foreign key (cur_id) references currency (id) on delete set null;

alter table account_address 
	add index fk_aadr_adr (address_id), 
	add index fk_aadr_a (aid), 
	add constraint fk_aadr_adr foreign key (address_id) references address (id) on delete cascade,
	add constraint fk_aadr_a foreign key (aid) references account (id) on delete cascade;

create table hibernate_unique_key (
    next_hi integer 
);
insert into hibernate_unique_key values ( 0 );
