/**
 * The Logic Lab
 * @author jpk
 * Jan 24, 2009
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.mock.model.Account;
import com.tll.mock.model.AccountAddress;
import com.tll.mock.model.Address;
import com.tll.mock.model.Currency;
import com.tll.mock.model.MockEntityFactory;
import com.tll.mock.model.NestedEntity;


/**
 * TestEntityDaoTestHandler
 * @author jpk
 */
public class TestEntityDaoTestHandler extends AbstractEntityDaoTestHandler<Account> {
	
	private static enum SelectNamedQueries implements ISelectNamedQueryDef {
		ACCOUNT_LISTING("account.testScalarQuery", Account.class, true, true);

		private final String queryName;
		private final Class<?> entityType;
		private final boolean scalar;
		private final boolean supportsPaging;

		private SelectNamedQueries(String queryName, Class<?> entityType, boolean scalar, boolean supportsPaging) {
			this.queryName = queryName;
			this.entityType = entityType;
			this.scalar = scalar;
			this.supportsPaging = supportsPaging;
		}

		public String getQueryName() {
			return queryName;
		}

		public Class<?> getEntityType() {
			return entityType;
		}

		public boolean isScalar() {
			return scalar;
		}

		public boolean isSupportsPaging() {
			return supportsPaging;
		}

		@Override
		public String toString() {
			return queryName;
		}
	}
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

		Address address1 = create(Address.class, true);
		Address address2 = create(Address.class, true);
		
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
				MockEntityFactory.makeBusinessKeyUnique(aa);
				MockEntityFactory.makeBusinessKeyUnique(aa.getAddress());
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
		return SelectNamedQueries.values();
	}

	@Override
	public Sorting getSortingForTestQuery(ISelectNamedQueryDef qdef) {
		return new Sorting("name");
	}
}
