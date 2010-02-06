/**
 * The Logic Lab
 * @author jpk
 * Jan 24, 2009
 */
package com.tll.dao.test;

import org.testng.Assert;

import com.tll.criteria.Criteria;
import com.tll.dao.AbstractEntityDaoTestHandler;
import com.tll.model.egraph.EntityBeanFactory;
import com.tll.model.test.Account;
import com.tll.model.test.AccountAddress;
import com.tll.model.test.Address;
import com.tll.model.test.Currency;
import com.tll.model.test.NestedEntity;
import com.tll.util.DateRange;

/**
 * TestEntityDaoTestHandler
 * @author jpk
 */
public class TestEntityDaoTestHandler extends AbstractEntityDaoTestHandler<Account> {

	// dependent entities
	Object pkNestedEntity, pkCurrency, pkAccountParent;

	@Override
	public Class<Account> entityClass() {
		return Account.class;
	}

	@Override
	public boolean supportsPaging() {
		return true;
	}

	@Override
	public void persistDependentEntities() {
		Currency currency = create(Currency.class, true);
		currency = persist(currency);
		pkCurrency = currency.getId();

		NestedEntity nestedEntity = create(NestedEntity.class, true);
		nestedEntity = persist(nestedEntity);
		pkNestedEntity = nestedEntity.getId();

		Account parent = create(Account.class, true);
		parent.setName("parent account");
		parent.setParent(null); // eliminate pointer chasing
		parent.setCurrency(currency);
		parent.setNestedEntity(nestedEntity);
		parent = persist(parent);
		pkAccountParent = parent.getId();
	}

	@Override
	public void purgeDependentEntities() {
		purge(Account.class, pkAccountParent); pkAccountParent = null;
		purge(NestedEntity.class, pkNestedEntity); pkNestedEntity = null;
		purge(Currency.class, pkCurrency); pkCurrency = null;
	}

	@Override
	public void assembleTestEntity(Account e) throws Exception {
		e.setCurrency(load(Currency.class, pkCurrency));
		e.setNestedEntity(load(NestedEntity.class, pkNestedEntity));
		e.setParent(load(Account.class, pkAccountParent));

		final Address address1 = create(Address.class, true);
		final Address address2 = create(Address.class, true);

		final AccountAddress aa1 = create(AccountAddress.class, true);
		final AccountAddress aa2 = create(AccountAddress.class, true);
		aa1.setAddress(address1);
		aa2.setAddress(address2);
		e.addAccountAddress(aa1);
		e.addAccountAddress(aa2);
	}

	@Override
	public void makeUnique(Account e) {
		super.makeUnique(e);
		if(e.getAddresses() != null) {
			for(final AccountAddress aa : e.getAddresses()) {
				EntityBeanFactory.makeBusinessKeyUnique(aa);
				EntityBeanFactory.makeBusinessKeyUnique(aa.getAddress());
			}
		}
	}

	@Override
	public void verifyLoadedEntityState(Account e) throws Exception {
		super.verifyLoadedEntityState(e);

		Assert.assertNotNull(e.getCurrency(), "No account currency loaded");
		Assert.assertNotNull(e.getNestedEntity(), "No account nested entity loaded");
		Assert.assertNotNull(e.getNestedEntity().getNestedData(), "No account nested entity nested data loaded");
		Assert.assertTrue(e.getAddresses() != null && e.getAddresses().size() == 2,
		"No account address collection loaded or invalid number of them");
	}

	@Override
	public Criteria<Account> getTestCriteria() {
		final Criteria<Account> c = new Criteria<Account>(Account.class);
		c.getPrimaryGroup().addCriterion("dateLastCharged", new DateRange(DateRange.DATE_PAST, DateRange.DATE_FUTURE));
		return c;
	}

	/*
	@Override
	public ISelectNamedQueryDef[] getQueriesToTest() {
		//return TestSelectNamedQueries.values();
		return new ISelectNamedQueryDef[] { TestSelectNamedQueries.ACCOUNT_LISTING };
	}

	@Override
	public Sorting getSortingForTestQuery(ISelectNamedQueryDef qdef) {
		return new Sorting("name");
	}
	 */
}
