/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.AddressType;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.key.PrimaryKey;

/**
 * AccountAddressDaoTestHandler
 * @author jpk
 */
public class AccountAddressDaoTestHandler extends AbstractEntityDaoTestHandler<AccountAddress> {

	PrimaryKey<Asp> aKey;
	PrimaryKey<Address> adrKey;

	@Override
	public Class<AccountAddress> entityClass() {
		return AccountAddress.class;
	}

	@Override
	public boolean supportsPaging() {
		return false;
	}

	@Override
	public void assembleTestEntity(AccountAddress e) throws Exception {

		Asp account;
		if(aKey == null) {
			account = mockEntityFactory.getEntityCopy(Asp.class, true);
			account.setCurrency(entityDao.persist(mockEntityFactory.getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = entityDao.persist(account);
			aKey = new PrimaryKey<Asp>(account);
		}
		else {
			account = entityDao.load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Address address;
		if(adrKey == null) {
			address = entityDao.persist(mockEntityFactory.getEntityCopy(Address.class, true));
			adrKey = new PrimaryKey<Address>(address);
		}
		else {
			address = entityDao.load(adrKey);
		}
		Assert.assertNotNull(address);
		e.setAddress(address);
	}

	@Override
	public void teardownTestEntity(AccountAddress e) {
		if(aKey != null) {
			try {
				final Account account = entityDao.load(aKey);
				entityDao.purge(account);
				entityDao.purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(adrKey != null) {
			try {
				entityDao.purge(entityDao.load(adrKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			adrKey = null;
		}
	}

	@Override
	public void verifyLoadedEntityState(AccountAddress e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getAddress());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	public void alterTestEntity(AccountAddress e) {
		super.alterTestEntity(e);
		Assert.assertFalse(AddressType.CONTACT.equals(e.getType()));
		e.setType(AddressType.CONTACT);
	}

	@Override
	public void verifyEntityAlteration(AccountAddress e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertTrue(AddressType.CONTACT.equals(e.getType()));
	}

}
