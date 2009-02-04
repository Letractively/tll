package com.tll.service.entity.account;

import com.tll.INameValueProvider;
import com.tll.model.Account;
import com.tll.model.CustomerAccount;

/**
 * AccountHistoryContext
 * @author jpk
 */
public class AccountHistoryContext {
	
	/**
	 * AccountHistoryOp
	 * @author jpk
	 */
	public static enum AccountHistoryOp implements INameValueProvider<String> {
		ACCOUNT_ADDED("Account added"),
		ACCOUNT_UPDATED("Account update"),
		ACCOUNT_DELETED("Account deleted"),
		ACCOUNT_PURGED("Account purget"),
		CUSTOMER_ACCOUNT_ADDED("Customer account added"),
		CUSTOMER_ACCOUNT_DELETED("Customer account deleted"),
		CUSTOMER_ACCOUNT_PURGED("Customer account purged");

		private final String name;

		private AccountHistoryOp(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return name();
		}

	}
	
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