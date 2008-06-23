package com.tll.model.impl.key;

import java.util.Date;

import com.tll.key.IKey;
import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.AccountStatus;
import com.tll.model.key.BusinessKey;

public final class AccountHistoryKey extends BusinessKey<AccountHistory> {

	private static final long serialVersionUID = 3274325493560726463L;

	private static final String[] FIELDS = new String[] {
		"account.id",
		"transDate",
		"status" };

	private Integer accountId;
	private Date transDate;
	private AccountStatus accountStatus;

	public AccountHistoryKey() {
		super(AccountHistory.class);
	}

	public AccountHistoryKey(Integer accountId, Date transDate, AccountStatus accountStatus) {
		this();
		setAccountId(accountId);
		setTransDate(transDate);
		setAccountStatus(accountStatus);
	}

	@Override
	protected String keyDescriptor() {
		return "Account Id, Trans Date and Account Status";
	}

	@Override
	public void setEntity(AccountHistory e) {
		super.setEntity(e);
		e.getAccount().setId(getAccountId());
		e.setTransDate(getTransDate());
		e.setStatus(getAccountStatus());
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer value) {
		this.accountId = value;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date value) {
		this.transDate = value;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus value) {
		this.accountStatus = value;
	}

	@Override
	public String[] getFieldNames() {
		return FIELDS;
	}

	@Override
	public void clear() {
		this.accountId = null;
		this.transDate = null;
		this.accountStatus = null;
	}

	@Override
	public boolean isSet() {
		return accountId != null && transDate != null && accountStatus != null;
	}

	@Override
	public int compareTo(IKey<AccountHistory> o) {
		// TODO impl
		return 0;
	}
}