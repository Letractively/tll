package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.validation.constraints.NotNull;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * product cateory binder entity
 * @author jpk
 */
@PersistenceCapable
@BusinessObject(businessKeys =
	@BusinessKeyDef(name = "Product Id and Category Id", properties = { "product.id", "category.id" }))
	public class ProdCat extends EntityBase implements IChildEntity<ProductInventory>, IAccountRelatedEntity {

	private static final long serialVersionUID = -8353863817821839414L;

	@Persistent
	private boolean isFeaturedProduct = false;

	@Persistent
	private ProductInventory product;

	@Persistent
	private ProductCategory category;

	public Class<? extends IEntity> entityClass() {
		return ProdCat.class;
	}

	/**
	 * @return Returns the bIsFeaturedProduct.
	 */
	@NotNull
	public boolean getIsFeaturedProduct() {
		return isFeaturedProduct;
	}

	/**
	 * @param isFeaturedProduct The bIsFeaturedProduct to set.
	 */
	public void setIsFeaturedProduct(boolean isFeaturedProduct) {
		this.isFeaturedProduct = isFeaturedProduct;
	}

	/**
	 * @return Returns the category.
	 */
	@NotNull
	public ProductCategory getCategory() {
		return category;
	}

	/**
	 * @param category The category to set.
	 */
	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	/**
	 * @return Returns the product.
	 */
	@NotNull
	public ProductInventory getProduct() {
		return product;
	}

	/**
	 * @param product The product to set.
	 */
	public void setProduct(ProductInventory product) {
		this.product = product;
	}

	public ProductInventory getParent() {
		return getProduct();
	}

	public void setParent(ProductInventory e) {
		setProduct(e);
	}

	public Integer accountId() {
		try {
			return getProduct().getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Integer productId() {
		try {
			return getProduct().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}

	public Integer categoryId() {
		try {
			return getCategory().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}