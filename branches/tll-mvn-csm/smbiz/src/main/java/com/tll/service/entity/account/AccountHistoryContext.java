package com.tll.service.entity.account;

import com.tll.model.Account;
import com.tll.model.CustomerAccount;

public class AccountHistoryContext {
	AccountHistoryOp op;
  Account account;
  CustomerAccount customerAccount;
  
  public AccountHistoryContext(AccountHistoryOp op, Account account) {
    this.op = op;
    this.account = account;
  }
  
  public AccountHistoryContext(AccountHistoryOp op, CustomerAccount customerAccount) {
    this.op = op;
    this.customerAccount = customerAccount;
  }

  public Account getAccount() {
    return account;
  }

  public CustomerAccount getCustomerAccount() {
    return customerAccount;
  }

  public AccountHistoryOp getOp() {
    return op;
  }

}