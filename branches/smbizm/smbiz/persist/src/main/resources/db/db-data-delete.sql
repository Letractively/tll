delete from order_item_trans;
delete from order_trans;
delete from order_item;
delete from orders;

delete from pch;
delete from prod_cat;
delete from product_category;
delete from product_inventory;
delete from product_general;

delete from customer_account;
delete from account_address;
delete from account_history;

delete from authority;
delete from user;
delete from user_authority;

delete from ship_bound_cost;
delete from ship_mode;
delete from sales_tax;
delete from visitor;
delete from site_code;

delete from ioap;
delete from ioa;

delete from currency;

delete from payment_info;

delete from account where parent_aid is not null;
delete from account;

delete from iopd;
delete from interface;

delete from address;

delete from payment_trans;

delete from app_property;

