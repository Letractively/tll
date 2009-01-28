/**
 * The Logic Lab
 * @author jpk
 * Jan 24, 2009
 */
package com.tll.dao;

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
 * TestEntityDaoTestHandler
 * @author jpk
 */
public class TestEntityDaoTestHandler extends AbstractEntityDaoTestHandler<Account> {

	PrimaryKey<NestedEntity> neKey;
	PrimaryKey<Currency> cKey;
	PrimaryKey<Address> a1Key;
	PrimaryKey<Address> a2Key;
	PrimaryKey<Account> parentKey;

	@Override
	public Class<Account> entityClass() {
		return Account.class;
	}

	@Override
	public boolean supportsPaging() {
		return false;
	}

	@Override
	public void assembleTestEntity(Account e) throws Exception {

		Currency currency;
		if(cKey == null) {
			// load stubbed currency
			currency = mockEntityFactory.getEntityCopy(Currency.class, true);
			currency = entityDao.persist(currency);
			cKey = new PrimaryKey<Currency>(currency);
		}
		else {
			currency = entityDao.load(cKey);
		}
		Assert.assertNotNull(currency);
		e.setCurrency(currency);

		NestedEntity nestedEntity;
		if(neKey == null) {
			// stub nested entity
			try {
				nestedEntity = mockEntityFactory.getEntityCopy(NestedEntity.class, true);
			}
			catch(final Exception ex) {
				Assert.fail("Unable to acquire test nested entity");
				return;
			}
			nestedEntity = entityDao.persist(nestedEntity);
			neKey = new PrimaryKey<NestedEntity>(nestedEntity);
		}
		else {
			nestedEntity = entityDao.load(neKey);
		}
		Assert.assertNotNull(nestedEntity);
		e.setNestedEntity(nestedEntity);

		// stub account parent (if specified)
		if(e.getParent() != null) {
			Account parent = e.getParent();
			if(parent.isNew()) {
				if(parentKey == null) {
					entityFactory.setGenerated(parent);
					parent.setParent(null); // eliminate pointer chasing
					parent.setCurrency(currency);
					parent.setNestedEntity(nestedEntity);
					parent = entityDao.persist(parent);
					parentKey = new PrimaryKey<Account>(parent);
				}
				else {
					parent = entityDao.load(parentKey);
				}
				Assert.assertNotNull(parent);
				e.setParent(parent);
			}
		}

		Address a1;
		if(a1Key == null) {
			a1 = mockEntityFactory.getEntityCopy(Address.class, true);
			a1 = entityDao.persist(a1);
			a1Key = new PrimaryKey<Address>(a1);
		}
		else {
			a1 = entityDao.load(a1Key);
		}
		Assert.assertNotNull(a1);

		Address a2;
		if(a2Key == null) {
			a2 = entityDao.persist(mockEntityFactory.getEntityCopy(Address.class, true));
			a2Key = new PrimaryKey<Address>(a2);
		}
		else {
			a2 = entityDao.load(a2Key);
		}
		Assert.assertNotNull(a2);

		final AccountAddress aa1 = mockEntityFactory.getEntityCopy(AccountAddress.class, true);
		final AccountAddress aa2 = mockEntityFactory.getEntityCopy(AccountAddress.class, true);
		aa1.setAddress(a1);
		aa2.setAddress(a2);
		e.addAccountAddress(aa1);
		e.addAccountAddress(aa2);
	}

	@Override
	public void teardownTestEntity(Account e) {
		if(neKey != null) {
			try {
				entityDao.purge(entityDao.load(neKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			neKey = null;
		}

		if(cKey != null) {
			try {
				entityDao.purge(entityDao.load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

		if(a1Key != null) {
			try {
				entityDao.purge(entityDao.load(a1Key));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			a1Key = null;
		}

		if(a2Key != null) {
			try {
				entityDao.purge(entityDao.load(a2Key));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			a2Key = null;
		}

		if(parentKey != null) {
			try {
				entityDao.purge(entityDao.load(parentKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			parentKey = null;
		}
	}

	@Override
	public void makeUnique(Account e) {
		try {
			if(e.getAddresses() != null) {
				for(final AccountAddress aa : e.getAddresses()) {
					mockEntityFactory.makeBusinessKeyUnique(aa);
					mockEntityFactory.makeBusinessKeyUnique(aa.getAddress());
				}
			}
		}
		catch(final BusinessKeyNotDefinedException ex) {
			Assert.fail(ex.getMessage());
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
	public void alterTestEntity(Account e) {
		super.alterTestEntity(e);
	}

	@Override
	public void verifyEntityAlteration(Account e) throws Exception {
		super.verifyEntityAlteration(e);
	}
}
