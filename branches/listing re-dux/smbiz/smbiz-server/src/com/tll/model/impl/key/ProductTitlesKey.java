package com.tll.model.impl.key;

import com.tll.model.impl.ProductGeneral;
import com.tll.model.key.BusinessKey;

public final class ProductTitlesKey extends BusinessKey<ProductGeneral> {

	private static final long serialVersionUID = -2901641577638225499L;

	private static final String[] FIELDS = new String[] { "d1", "d2" };

	public ProductTitlesKey() {
		super();
	}

	public ProductTitlesKey(String d1, String d2) {
		this();
		setD1(d1);
		setD2(d2);
	}

	public Class<ProductGeneral> getType() {
		return ProductGeneral.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Product Title and Short Description";
	}

	public String getD1() {
		return (String) getValue(0);
	}

	public void setD1(String d1) {
		setValue(0, d1);
	}

	public String getD2() {
		return (String) getValue(1);
	}

	public void setD2(String d2) {
		setValue(1, d2);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(ProductGeneral e) {
		e.setD1(getD1());
		e.setD2(getD2());
	}

}