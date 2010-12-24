package com.tll.model;

import javax.validation.constraints.NotNull;

/**
 * product cateory binder entity
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Product Id and Category Id", properties = {
	"product.id", "category.id" }))
public class ProdCat extends EntityBase implements IChildEntity<ProductInventory>, IAccountRelatedEntity {

	private static final long serialVersionUID = -8353863817821839414L;

	private boolean isFeaturedProduct = false;

	private ProductInventory product;

	private ProductCategory category;

	@Override
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
	public void setIsFeaturedProduct(final boolean isFeaturedProduct) {
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
	public void setCategory(final ProductCategory category) {
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
	public void setProduct(final ProductInventory product) {
		this.product = product;
	}

	@Override
	public ProductInventory getParent() {
		return getProduct();
	}

	@Override
	public void setParent(final ProductInventory e) {
		setProduct(e);
	}

	@Override
	public Object accountKey() {
		try {
			return getProduct().getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	public Object productId() {
		try {
			return getProduct().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}

	public Object categoryKey() {
		try {
			return getCategory().getId();
		}
		catch(final NullPointerException npe) {
			return null;
		}
	}
}