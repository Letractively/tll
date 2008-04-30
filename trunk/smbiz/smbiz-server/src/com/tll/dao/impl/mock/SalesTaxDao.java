/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.ISalesTaxDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.SalesTax;

public class SalesTaxDao extends EntityDao<SalesTax> implements ISalesTaxDao, IMockDao<SalesTax> {

	@Inject
	public SalesTaxDao(Set<SalesTax> set) {
		super(SalesTax.class, set);
	}
}