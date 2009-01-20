/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IProductInventoryDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.ProductInventory;

public class ProductInventoryDao extends EntityDao<ProductInventory> implements IProductInventoryDao,
		IMockDao<ProductInventory> {

	@Inject
	public ProductInventoryDao(Set<ProductInventory> set) {
		super(ProductInventory.class, set);
	}
}