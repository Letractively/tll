/**
 * The Logic Lab
 * @author jpk
 * Jan 24, 2009
 */
package com.tll.dao.test;

import org.testng.Assert;

import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.test.TestSelectNamedQueries;
import com.tll.dao.AbstractEntityDaoTestHandler;
import com.tll.dao.Sorting;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Currency;
import com.tll.model.EntityBeanFactory;
import com.tll.model.NestedEntity;


/**
 * TestEntityDaoTestHandler
 * @author jpk
 */
public class TestEntityDaoTestHandler extends AbstractEntityDaoTestHandler<Account> {

	// dependent entities
	NestedEntity nestedEntity;
	Currency currency;
	Account parent;


	@Override
	public Class<Account> entityClass() {
		return Account.class;
	}

	@Override
	public boolean supportsPaging() {
		return false;
	}

	@Override
	public void persistDependentEntities() {
		currency = create(Currency.class, true);
		currency = persist(currency);

		nestedEntity = create(NestedEntity.class, true);
		nestedEntity = persist(nestedEntity);

		parent = create(Account.class, true);
		parent.setParent(null); // eliminate pointer chasing
		parent.setCurrency(currency);
		parent.setNestedEntity(nestedEntity);
		parent = persist(parent);
	}

	@Override
	public void purgeDependentEntities() {
		purge(parent);
		purge(nestedEntity);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(Account e) throws Exception {

		e.setCurrency(currency);
		e.setNestedEntity(nestedEntity);
		e.setParent(parent);

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
		Assert.assertTrue(e.getAddresses() != null && e.getAddresses().size() == 2,
		"No account address collection loaded or invalid number of them");
	}

	@Override
	public ISelectNamedQueryDef[] getQueriesToTest() {
		//return TestSelectNamedQueries.values();
		return new ISelectNamedQueryDef[] { TestSelectNamedQueries.ACCOUNT_LISTING };
	}

	@Override
	public Sorting getSortingForTestQuery(ISelectNamedQueryDef qdef) {
		return new Sorting("name");
	}
}
