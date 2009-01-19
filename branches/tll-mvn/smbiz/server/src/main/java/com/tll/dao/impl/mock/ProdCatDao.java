/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IProdCatDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.ProdCat;
import com.tll.model.key.IBusinessKeyFactory;

public class ProdCatDao extends EntityDao<ProdCat> implements IProdCatDao, IMockDao<ProdCat> {

	@Inject
	public ProdCatDao(Set<ProdCat> set, IBusinessKeyFactory bkf) {
		super(ProdCat.class, set, bkf);
	}
}