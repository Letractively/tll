/**
 * The Logic Lab
 * @author jpk
 * Jan 24, 2009
 */
package com.tll.test;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.Currency;
import com.tll.model.NestedEntity;
import com.tll.model.key.PrimaryKey;


/**
 * GenericEntityDaoTest
 * @author jpk
 */
public class GenericEntityDaoTest extends AbstractEntityDaoTest<Account> {

	PrimaryKey<NestedEntity> neKey;
	PrimaryKey<Currency> cKey;
	PrimaryKey<Address> a1Key;
	PrimaryKey<Address> a2Key;
	PrimaryKey<Account> parentKey;
	
	/**
	 * Constructor
	 */
	public GenericEntityDaoTest() {
		super(Account.class, false);
	}

	@Override
	protected void assembleTestEntity(Account e) throws Exception {

		Currency currency;
		if(cKey == null) {
			// load stubbed currency
			currency = getMockEntityProvider().getEntityCopy(Currency.class, true);
			currency = getEntityDao().persist(currency);
			cKey = new PrimaryKey<Currency>(currency);
		}
		else {
			currency = getEntityDao().load(cKey);
		}
		Assert.assertNotNull(currency);
		e.setCurrency(currency);

		NestedEntity nestedEntity;
		if(neKey == null) {
			// stub nested entity
			try {
				nestedEntity = getMockEntityProvider().getEntityCopy(NestedEntity.class, true);
			}
			catch(final Exception ex) {
				Assert.fail("Unable to acquire test nested entity");
				return;
			}
			nestedEntity = getEntityDao().persist(nestedEntity);
			neKey = new PrimaryKey<NestedEntity>(nestedEntity);
		}
		else {
			nestedEntity = getEntityDao().load(neKey);
		}
		Assert.assertNotNull(nestedEntity);
		e.setNestedEntity(nestedEntity);

		// stub account parent (if specified)
		if(e.getParent() != null) {
			Account parent = e.getParent();
			if(parent.isNew()) {
				if(parentKey == null) {
					getEntityFactory().setGenerated(parent);
					parent.setParent(null); // eliminate pointer chasing
					parent.setCurrency(currency);
					parent.setNestedEntity(nestedEntity);
					parent = getEntityDao().persist(parent);
					parentKey = new PrimaryKey<Account>(parent);
				}
				else {
					parent = getEntityDao().load(parentKey);
				}
				Assert.assertNotNull(parent);
				e.setParent(parent);
			}
		}

		Address a1;
		if(a1Key == null) {
			a1 = getEntityDao().persist(getMockEntityProvider().getEntityCopy(Address.class, true));
			a1Key = new PrimaryKey<Address>(a1);
		}
		else {
			a1 = getEntityDao().load(a1Key);
		}
		Assert.assertNotNull(a1);

		Address a2;
		if(a2Key == null) {
			a2 = getEntityDao().persist(getMockEntityProvider().getEntityCopy(Address.class, true));
			a2Key = new PrimaryKey<Address>(a2);
		}
		else {
			a2 = getEntityDao().load(a2Key);
		}
		Assert.assertNotNull(a2);

		final AccountAddress aa1 = getMockEntityProvider().getEntityCopy(AccountAddress.class, true);
		final AccountAddress aa2 = getMockEntityProvider().getEntityCopy(AccountAddress.class, true);
		aa1.setAddress(a1);
		aa2.setAddress(a2);
		e.addAccountAddress(aa1);
		e.addAccountAddress(aa2);
	}

	@Override
	protected void afterMethodHook() {
		startNewTransaction();

		if(neKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(neKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			neKey = null;
		}

		if(cKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

		if(a1Key != null) {
			try {
				getEntityDao().purge(getEntityDao().load(a1Key));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			a1Key = null;
		}

		if(a2Key != null) {
			try {
				getEntityDao().purge(getEntityDao().load(a2Key));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			a2Key = null;
		}

		if(parentKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(parentKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			parentKey = null;
		}

		setComplete();
		endTransaction();
	}

	@Override
	protected void uniquify(Account e) {
		super.uniquify(e);

		try {
			if(e.getAddresses() != null) {
				for(final AccountAddress aa : e.getAddresses()) {
					getMockEntityProvider().makeBusinessKeyUnique(aa);
					getMockEntityProvider().makeBusinessKeyUnique(aa.getAddress());
				}
			}
		}
		catch(final BusinessKeyNotDefinedException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Override
	protected void verifyLoadedEntityState(Account e) throws Exception {
		super.verifyLoadedEntityState(e);

		Assert.assertNotNull(e.getCurrency(), "No account currency loaded");
		Assert.assertNotNull(e.getNestedEntity(), "No account nested entity loaded");
		Assert.assertTrue(e.getAddresses() != null && e.getAddresses().size() == 2,
				"No account address collection loaded or invalid number of them");
	}

}
