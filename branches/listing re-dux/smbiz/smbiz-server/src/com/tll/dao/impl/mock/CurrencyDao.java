/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.ICurrencyDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.Currency;

public class CurrencyDao extends EntityDao<Currency> implements ICurrencyDao, IMockDao<Currency> {

	@Inject
	public CurrencyDao(Set<Currency> set) {
		super(Currency.class, set);
	}
}