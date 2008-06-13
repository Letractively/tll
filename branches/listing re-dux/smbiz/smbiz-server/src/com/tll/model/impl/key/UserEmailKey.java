package com.tll.model.impl.key;

import com.tll.model.impl.User;
import com.tll.model.key.BusinessKey;

public final class UserEmailKey extends BusinessKey<User> {

	private static final long serialVersionUID = 4298176685283833934L;

	private static final String[] FIELDS = new String[] { "emailAddress" };

	public UserEmailKey() {
		super();
	}

	public UserEmailKey(String emailAddress) {
		this();
		setEmailAddress(emailAddress);
	}

	public Class<User> getType() {
		return User.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Username";
	}

	public String getEmailAddress() {
		return (String) getValue(0);
	}

	public void setEmailAddress(String emailAddress) {
		setValue(0, emailAddress);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(User e) {
		e.setEmailAddress(getEmailAddress());
	}

}