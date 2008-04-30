package com.tll.model.impl.key;

import java.util.Date;

import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.AccountStatus;
import com.tll.model.key.BusinessKey;

public final class AccountHistoryKey extends BusinessKey<AccountHistory> {

	private static final long serialVersionUID = 3274325493560726463L;

	private static final String[] FIELDS = new String[] { "account.id", "transDate", "status" };

	public AccountHistoryKey() {
		super();
	}

	public AccountHistoryKey(Integer accountId, Date transDate, AccountStatus status) {
		this();
		setAccountId(accountId);
		setTransDate(transDate);
		setAccountStatus(status);
	}

	public Class<AccountHistory> getType() {
		return AccountHistory.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Account Id, Trans Date and Account Status";
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(AccountHistory e) {
		e.getAccount().setId(getAccountId());
		e.setTransDate(getTransDate());
		e.setStatus(getAccountStatus());
	}

	public Integer getAccountId() {
		return (Integer) getValue(0);
	}

	public void setAccountId(Integer value) {
		setValue(0, value);
	}

	public Date getTransDate() {
		return (Date) getValue(1);
	}

	public void setTransDate(Date value) {
		setValue(1, value);
	}

	public AccountStatus getAccountStatus() {
		return (AccountStatus) getValue(2);
	}

	public void setAccountStatus(AccountStatus value) {
		setValue(2, value);
	}

}