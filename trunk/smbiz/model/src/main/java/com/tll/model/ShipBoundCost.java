package com.tll.model;

import javax.validation.constraints.NotNull;

import com.tll.model.bk.BusinessKeyDef;
import com.tll.model.bk.BusinessObject;

/**
 * Used to hold shipping rates in terms of upper/lower bounds of a product's
 * "weight"
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Ship Mode Id, Lower Bound and Upper Bound", properties = {
	"shipMode.id", "lbound", "ubound" }))
public class ShipBoundCost extends TimeStampEntity implements IChildEntity<ShipMode>, IAccountRelatedEntity {

	private static final long serialVersionUID = -5074831489410804639L;

	private float lbound = 0f;

	private float ubound = 0f;

	private float cost = 0f;

	private ShipMode shipMode;

	@Override
	public Class<? extends IEntity> entityClass() {
		return ShipBoundCost.class;
	}

	/**
	 * @return Returns the shipMode.
	 */
	@NotNull
	public ShipMode getShipMode() {
		return shipMode;
	}

	/**
	 * @param shipMode The shipMode to set.
	 */
	public void setShipMode(final ShipMode shipMode) {
		this.shipMode = shipMode;
	}

	/**
	 * @return Returns the cost.
	 */
	// @Size(min = 0, max = 999999)
	public float getCost() {
		return cost;
	}

	/**
	 * @param cost The cost to set.
	 */
	public void setCost(final float cost) {
		this.cost = cost;
	}

	/**
	 * @return Returns the lBound.
	 */
	// @Size(min = 0, max = 999999)
	public float getLbound() {
		return lbound;
	}

	/**
	 * @param bound The lBound to set.
	 */
	public void setLbound(final float bound) {
		lbound = bound;
	}

	/**
	 * @return Returns the uBound.
	 */
	// @Size(min = 0, max = 999999)
	public float getUbound() {
		return ubound;
	}

	/**
	 * @param bound The uBound to set.
	 */
	public void setUbound(final float bound) {
		ubound = bound;
	}

	@Override
	public ShipMode getParent() {
		return getShipMode();
	}

	@Override
	public void setParent(final ShipMode e) {
		setShipMode(e);
	}

	@Override
	public Object accountKey() {
		try {
			return getShipMode().getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Object shipModeKey() {
		try {
			return getShipMode().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}
