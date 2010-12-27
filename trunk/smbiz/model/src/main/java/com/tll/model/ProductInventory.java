package com.tll.model;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.tll.model.bk.BusinessKeyDef;

/**
 * Product inventory entity
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Account Id and SKU", properties = {
	"account.id", "sku" }))
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

	@Override
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
	public void setAccount(final Account account) {
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
	public void setAuxDescriptor(final String auxDescriptor) {
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
	public void setInvCommitted(final int invCommitted) {
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
	public void setInvInStock(final int invInStock) {
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
	public void setInvReorderLevel(final int invReorderLevel) {
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
	public void setOnSale(final boolean onSale) {
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
	public void setProductGeneral(final ProductGeneral productGeneral) {
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
	public void setRetailPrice(final float retailPrice) {
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
	public void setSalesPrice(final float salesPrice) {
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
	public void setSku(final String sku) {
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
	public void setStatus(final ProductStatus status) {
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
	public void setWeight(final float weight) {
		this.weight = weight;
	}

	@Override
	public Account getParent() {
		return getAccount();
	}

	@Override
	public void setParent(final Account e) {
		setAccount(e);
	}

	@Override
	public Object accountKey() {
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