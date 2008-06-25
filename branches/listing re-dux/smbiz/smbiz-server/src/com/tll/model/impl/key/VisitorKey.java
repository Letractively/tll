package com.tll.model.impl.key;

import java.util.Date;

import com.tll.model.impl.Visitor;
import com.tll.model.key.BusinessKey;

public final class VisitorKey extends BusinessKey<Visitor> {

	private static final long serialVersionUID = 1486076538757390619L;

	private static final String[] FIELDS = new String[] { "account.id", "dateCreated", "remoteHost" };

	public VisitorKey() {
		super();
	}

	public VisitorKey(Integer accountId, Date dateCreated, String remoteHost) {
		this();
		setAccountId(accountId);
		setDateCreated(dateCreated);
		setRemoteHost(remoteHost);
	}

	public Class<Visitor> getType() {
		return Visitor.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Shop Date and Remote Host";
	}

	public Integer getAccountId() {
		return (Integer) getValue(0);
	}

	public void setAccountId(Integer accountId) {
		setValue(0, accountId);
	}

	public Date getDateCreated() {
		return (Date) getValue(1);
	}

	public void setDateCreated(Date dateCreated) {
		setValue(1, dateCreated);
	}

	public String getRemoteHost() {
		return (String) getValue(2);
	}

	public void setRemoteHost(String remoteHost) {
		setValue(2, remoteHost);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(Visitor e) {
		e.getAccount().setId(getAccountId());
		e.setDateCreated(getDateCreated());
		e.setRemoteHost(getRemoteHost());
	}

}