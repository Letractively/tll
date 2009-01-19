/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IProductCategoryDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.ProductCategory;
import com.tll.model.key.IBusinessKeyFactory;

public class ProductCategoryDao extends EntityDao<ProductCategory> implements IProductCategoryDao,
		IMockDao<ProductCategory> {

	@Inject
	public ProductCategoryDao(Set<ProductCategory> set, IBusinessKeyFactory bkf) {
		super(ProductCategory.class, set, bkf);
	}
}