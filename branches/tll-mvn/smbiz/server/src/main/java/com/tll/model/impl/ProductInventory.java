package com.tll.model.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;
import org.hibernate.validator.Valid;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.TimeStampEntity;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * Product inventory entity
 * @author jpk
 */
@Entity
@Table(name = "product_inventory")
@BusinessObject(businessKeys = 
	@BusinessKeyDef(name = "Account Id and SKU", properties = { "account.id", "sku" }))
public class ProductInventory extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 6472483051056869008L;

	public static final int MAXLEN_SKU = 64;
	public static final int MAXLEN_AUX_DESCRIPTOR = 255;

	private String sku;

	private ProductStatus status;

	private float retailPrice = 0f;

	private float salesPrice = 0f;

	private float weight = 0f;

	private boolean onSale;

	private String auxDescriptor;

	private int invInStock = 0;

	private int invCommitted = 0;

	private int invReorderLevel = 0;

	private Account account;

	private ProductGeneral productGeneral;

	public Class<? extends IEntity> entityClass() {
		return ProductInventory.class;
	}

	/**
	 * @return Returns the account.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aid")
	@NotNull
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account The account to set.
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return Returns the auxDescriptor.
	 */
	@Column(name = "aux_descriptor")
	@Length(max = MAXLEN_AUX_DESCRIPTOR)
	public String getAuxDescriptor() {
		return auxDescriptor;
	}

	/**
	 * @param auxDescriptor The auxDescriptor to set.
	 */
	public void setAuxDescriptor(String auxDescriptor) {
		this.auxDescriptor = auxDescriptor;
	}

	/**
	 * @return Returns the invCommitted.
	 */
	@Column(name = "inv_committed")
	@NotNull
	@Range(min = 0L, max = 999999L)
	public int getInvCommitted() {
		return invCommitted;
	}

	/**
	 * @param invCommitted The invCommitted to set.
	 */
	public void setInvCommitted(int invCommitted) {
		this.invCommitted = invCommitted;
	}

	/**
	 * @return Returns the invInStock.
	 */
	@Column(name = "inv_in_stock")
	@NotNull
	@Range(min = 0L, max = 999999L)
	public int getInvInStock() {
		return invInStock;
	}

	/**
	 * @param invInStock The invInStock to set.
	 */
	public void setInvInStock(int invInStock) {
		this.invInStock = invInStock;
	}

	/**
	 * @return Returns the invReorderLevel.
	 */
	@Column(name = "inv_reorder_level")
	@NotNull
	@Range(min = 0L, max = 999999L)
	public int getInvReorderLevel() {
		return invReorderLevel;
	}

	/**
	 * @param invReorderLevel The invReorderLevel to set.
	 */
	public void setInvReorderLevel(int invReorderLevel) {
		this.invReorderLevel = invReorderLevel;
	}

	/**
	 * @return Returns the onSale.
	 */
	@Column(name = "on_sale")
	@NotNull
	public boolean isOnSale() {
		return onSale;
	}

	/**
	 * @param onSale The onSale to set.
	 */
	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}

	/**
	 * @return Returns the productGeneral.
	 */
	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinColumn(name = "pg_id")
	@NotNull
	@Valid
	public ProductGeneral getProductGeneral() {
		return productGeneral;
	}

	/**
	 * @param productGeneral The productGeneral to set.
	 */
	public void setProductGeneral(ProductGeneral productGeneral) {
		this.productGeneral = productGeneral;
	}

	/**
	 * @return Returns the retailPrice.
	 */
	@Column(name = "retail_price", precision = 7, scale = 2)
	@NotNull
	@Range(min = 0L, max = 999999L)
	public float getRetailPrice() {
		return retailPrice;
	}

	/**
	 * @param retailPrice The retailPrice to set.
	 */
	public void setRetailPrice(float retailPrice) {
		this.retailPrice = retailPrice;
	}

	/**
	 * @return Returns the salesPrice.
	 */
	@Column(name = "sales_price", precision = 7, scale = 2)
	@NotNull
	@Range(min = 0L, max = 999999L)
	public float getSalesPrice() {
		return salesPrice;
	}

	/**
	 * @param salesPrice The salesPrice to set.
	 */
	public void setSalesPrice(float salesPrice) {
		this.salesPrice = salesPrice;
	}

	/**
	 * @return Returns the sku.
	 */
	@Column
	@NotNull
	@Length(max = MAXLEN_SKU)
	public String getSku() {
		return sku;
	}

	/**
	 * @param sku The sku to set.
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	/**
	 * @return Returns the status.
	 */
	@Column
	@NotNull
	public ProductStatus getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	/**
	 * @return Returns the weight.
	 */
	@Column(precision = 7, scale = 3)
	@NotNull
	@Range(min = 0L, max = 999999L)
	public float getWeight() {
		return weight;
	}

	/**
	 * @param weight The weight to set.
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Transient
	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public Integer accountId() {
		try {
			return getAccount().getId();
		}
		catch(NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public String d1() {
		try {
			return getProductGeneral().getD1();
		}
		catch(NullPointerException npe) {
			return null;
		}
	}

	public String d2() {
		try {
			return getProductGeneral().getD2();
		}
		catch(NullPointerException npe) {
			return null;
		}
	}
}