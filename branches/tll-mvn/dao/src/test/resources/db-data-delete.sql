delete from account_address;

delete from currency;

delete from account where parent_aid is not null;
delete from account;

delete from address;
