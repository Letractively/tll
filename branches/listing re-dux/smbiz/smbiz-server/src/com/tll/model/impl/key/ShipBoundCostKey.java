package com.tll.model.impl.key;

import com.tll.model.impl.ShipBoundCost;
import com.tll.model.key.BusinessKey;

public final class ShipBoundCostKey extends BusinessKey<ShipBoundCost> {

	private static final long serialVersionUID = 6169319000168774873L;

	private static final String[] FIELDS = new String[] { "shipMode.id", "lbound", "ubound" };

	public ShipBoundCostKey() {
		super();
	}

	public ShipBoundCostKey(Integer shipModeId, float lbound, float ubound) {
		this();
		setShipModeId(shipModeId);
		setLbound(lbound);
		setUbound(ubound);
	}

	public Class<ShipBoundCost> getType() {
		return ShipBoundCost.class;
	}

	public Integer getShipModeId() {
		return (Integer) getValue(0);
	}

	public void setShipModeId(Integer shipModeId) {
		setValue(0, shipModeId);
	}

	public Float getLbound() {
		return (Float) getValue(1);
	}

	public void setLbound(Float lbound) {
		setValue(1, lbound);
	}

	public Float getUbound() {
		return (Float) getValue(2);
	}

	public void setUbound(Float ubound) {
		setValue(2, ubound);
	}

	@Override
	protected String keyDescriptor() {
		return "Ship Mode Id, Lower bound and Upper bound";
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(ShipBoundCost e) {
		e.getShipMode().setId(getShipModeId());
		e.setLbound(getLbound());
		e.setUbound(getUbound());
	}

}