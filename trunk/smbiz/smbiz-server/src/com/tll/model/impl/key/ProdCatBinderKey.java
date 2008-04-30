package com.tll.model.impl.key;

import com.tll.model.impl.ProdCat;
import com.tll.model.key.BusinessKey;

/**
 * Business key for {@link ProdCat} holding the account id and the address id.
 * @author jpk
 */
public final class ProdCatBinderKey extends BusinessKey<ProdCat> {

	private static final long serialVersionUID = -4226292692071239941L;

	private static final String[] FIELDS = new String[] { "product.id", "category.id" };

	public ProdCatBinderKey() {
		super();
	}

	public ProdCatBinderKey(Integer productId, Integer categoryId) {
		this();
		setProductId(productId);
		setCategoryId(categoryId);
	}

	public Class<ProdCat> getType() {
		return ProdCat.class;
	}

	@Override
	protected String keyDescriptor() {
		return "Product Category Binder";
	}

	public Integer getProductId() {
		return (Integer) getValue(0);
	}

	public void setProductId(Integer productId) {
		setValue(0, productId);
	}

	public Integer getCategoryId() {
		return (Integer) getValue(1);
	}

	public void setCategoryId(Integer categoryId) {
		setValue(1, categoryId);
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(ProdCat entity) {
		entity.getProduct().setId(getProductId());
		entity.getCategory().setId(getCategoryId());
	}

}