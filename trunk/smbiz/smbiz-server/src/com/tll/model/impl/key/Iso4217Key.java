package com.tll.model.impl.key;

import com.tll.model.impl.Currency;
import com.tll.model.key.BusinessKey;

public final class Iso4217Key extends BusinessKey<Currency> {

	private static final long serialVersionUID = -264411502392330708L;

	private static final String[] FIELDS = new String[] { "iso4217" };

	public Iso4217Key() {
		super();
	}

	public Iso4217Key(String iso4217) {
		this();
		setIso4217(iso4217);
	}

	public Class<Currency> getType() {
		return Currency.class;
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(Currency e) {
		e.setIso4217(getIso4217());
	}

	@Override
	protected String keyDescriptor() {
		return "ISO-4217";
	}

	public String getIso4217() {
		return (String) getValue(0);
	}

	public void setIso4217(String iso4217) {
		setValue(0, iso4217);
	}

}