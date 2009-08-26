package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;

/**
 * Product inventory entity
 * @author jpk
 */
@PersistenceCapable
@Uniques(value =
	@Unique(name = "Account Id and SKU", members = { "account.id", "sku" }))
public class ProductInventory extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 6472483051056869008L;

	public static final int MAXLEN_SKU = 64;
	public static final int MAXLEN_AUX_DESCRIPTOR = 255;

	@Persistent
	private String sku;

	@Persistent
	private ProductStatus status;

	@Persistent
	private float retailPrice = 0f;

	@Persistent
	private float salesPrice = 0f;

	@Persistent
	private float weight = 0f;

	@Persistent
	private boolean onSale;

	@Persistent
	private String auxDescriptor;

	@Persistent
	private int invInStock = 0;

	@Persistent
	private int invCommitted = 0;

	@Persistent
	private int invReorderLevel = 0;

	@Persistent
	private Account account;

	@Persistent
	private ProductGeneral productGeneral;

	public Class<? extends IEntity> entityClass() {
		return ProductInventory.class;
	}

	/**
	 * @return Returns the account.
	 */
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
	@NotNull
	@Min(value = 0)
	@Max(value = 999999)
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
	@NotNull
	@Min(value = 0)
	@Max(value = 999999)
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
	@NotNull
	@Min(value = 0)
	@Max(value = 999999)
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
	@NotNull
	// @Size(min = 0, max = 999999)
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
	@NotNull
	// @Size(min = 0, max = 999999)
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
	@NotNull
	// @Size(min = 0, max = 999999)
	public float getWeight() {
		return weight;
	}

	/**
	 * @param weight The weight to set.
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public String accountId() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public String d1() {
		try {
			return getProductGeneral().getD1();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}

	public String d2() {
		try {
			return getProductGeneral().getD2();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}