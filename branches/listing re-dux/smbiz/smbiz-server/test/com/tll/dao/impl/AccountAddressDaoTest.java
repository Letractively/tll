/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.Address;
import com.tll.model.impl.AddressType;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.key.PrimaryKey;

/**
 * AccountAddressDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class AccountAddressDaoTest extends NamedEntityDaoTest<AccountAddress> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Address> adrKey;

	/**
	 * Constructor
	 */
	public AccountAddressDaoTest() {
		super(AccountAddress.class, IAccountAddressDao.class, false);
	}

	@Override
	protected void assembleTestEntity(AccountAddress e) throws Exception {

		Account account;
		if(aKey == null) {
			account = getMockEntityProvider().getEntityCopy(Asp.class);
			account.setCurrency(getDao(ICurrencyDao.class).persist(getMockEntityProvider().getEntityCopy(Currency.class)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getDao(IAccountDao.class).persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getDao(IAccountDao.class).load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Address address;
		if(adrKey == null) {
			address = getDao(IAddressDao.class).persist(getMockEntityProvider().getEntityCopy(Address.class, 1));
			adrKey = new PrimaryKey<Address>(address);
		}
		else {
			address = getDao(IAddressDao.class).load(adrKey);
		}
		Assert.assertNotNull(address);
		e.setAddress(address);
	}

	@Override
	protected void afterMethodHook() {
		startNewTransaction();
		setComplete();

		if(aKey != null) {
			try {
				final Account account = getDao(IAccountDao.class).load(aKey);
				getDao(IAccountDao.class).purge(account);
				getDao(ICurrencyDao.class).purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(adrKey != null) {
			try {
				getDao(IAddressDao.class).purge(getDao(IAddressDao.class).load(adrKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			adrKey = null;
		}

		endTransaction();
	}

	@Override
	protected void verifyLoadedEntityState(AccountAddress e) throws Exception {
		Assert.assertNotNull(e.getAccount());
		Assert.assertNotNull(e.getAddress());
		Assert.assertNotNull(e.getParent());
	}

	@Override
	protected void alterEntity(AccountAddress e) {
		Assert.assertFalse(AddressType.CONTACT.equals(e.getType()));
		e.setType(AddressType.CONTACT);
	}

	@Override
	protected void verifyEntityAlteration(AccountAddress e) throws Exception {
		Assert.assertTrue(AddressType.CONTACT.equals(e.getType()));
	}

}
