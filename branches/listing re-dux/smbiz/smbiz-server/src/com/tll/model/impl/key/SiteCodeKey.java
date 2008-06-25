package com.tll.model.impl.key;

import com.tll.model.impl.SiteCode;
import com.tll.model.key.BusinessKey;

public final class SiteCodeKey extends BusinessKey<SiteCode> {

	private static final long serialVersionUID = 3252694003721719978L;

	private static final String[] FIELDS = new String[] { "code" };

	public SiteCodeKey() {
		super();
	}

	public SiteCodeKey(String code) {
		this();
		setCode(code);
	}

	public Class<SiteCode> getType() {
		return SiteCode.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Code";
	}

	public String getCode() {
		return (String) getValue(0);
	}

	public void setCode(String code) {
		setValue(0, code);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(SiteCode e) {
		e.setCode(getCode());
	}

}