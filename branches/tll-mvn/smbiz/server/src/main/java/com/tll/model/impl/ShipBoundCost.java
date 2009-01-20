package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.TimeStampEntity;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * Used to hold shipping rates in terms of upper/lower bounds of a product's
 * "weight"
 * @author jpk
 */
@Entity
@Table(name = "ship_bound_cost")
@BusinessObject(businessKeys = 
	@BusinessKeyDef(name = "Ship Mode Id, Lower Bound and Upper Bound", 
			properties = { "shipMode.id", "lbound", "ubound" }))
public class ShipBoundCost extends TimeStampEntity implements IChildEntity<ShipMode>, IAccountRelatedEntity {

	private static final long serialVersionUID = -5074831489410804639L;

	private float lbound = 0f;

	private float ubound = 0f;

	private float cost = 0f;

	private ShipMode shipMode;

	public Class<? extends IEntity> entityClass() {
		return ShipBoundCost.class;
	}

	/**
	 * @return Returns the shipMode.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sm_id")
	@NotNull
	public ShipMode getShipMode() {
		return shipMode;
	}

	/**
	 * @param shipMode The shipMode to set.
	 */
	public void setShipMode(ShipMode shipMode) {
		this.shipMode = shipMode;
	}

	/**
	 * @return Returns the cost.
	 */
	@Column(precision = 7, scale = 2)
	@Range(min = 0L, max = 999999L)
	public float getCost() {
		return cost;
	}

	/**
	 * @param cost The cost to set.
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}

	/**
	 * @return Returns the lBound.
	 */
	@Column(name = "l_bound", precision = 6, scale = 0)
	@Range(min = 0L, max = 999999L)
	public float getLbound() {
		return lbound;
	}

	/**
	 * @param bound The lBound to set.
	 */
	public void setLbound(float bound) {
		lbound = bound;
	}

	/**
	 * @return Returns the uBound.
	 */
	@Column(name = "u_bound", precision = 6, scale = 0)
	@Range(min = 0L, max = 999999L)
	public float getUbound() {
		return ubound;
	}

	/**
	 * @param bound The uBound to set.
	 */
	public void setUbound(float bound) {
		ubound = bound;
	}

	@Transient
	public ShipMode getParent() {
		return getShipMode();
	}

	public void setParent(ShipMode e) {
		setShipMode(e);
	}

	public Integer accountId() {
		try {
			return getShipMode().getAccount().getId();
		}
		catch(NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Integer shipModeId() {
		try {
			return getShipMode().getId();
		}
		catch(NullPointerException npe) {
			return null;
		}
	}
}
