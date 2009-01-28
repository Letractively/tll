/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.AbstractEntityDaoTest;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.AddressType;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.key.PrimaryKey;

/**
 * AccountAddressDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class AccountAddressDaoTest extends AbstractEntityDaoTest<AccountAddress> {

	PrimaryKey<Account> aKey;
	PrimaryKey<Address> adrKey;

	/**
	 * Constructor
	 */
	public AccountAddressDaoTest() {
		super(AccountAddress.class, false);
	}

	@Override
	protected void assembleTestEntity(AccountAddress e) throws Exception {

		Account account;
		if(aKey == null) {
			account = getMockEntityFactory().getEntityCopy(Asp.class, true);
			account.setCurrency(getEntityDao().persist(getMockEntityFactory().getEntityCopy(Currency.class, true)));
			account.setPaymentInfo(null);
			account.setParent(null);
			account = getEntityDao().persist(account);
			aKey = new PrimaryKey<Account>(account);
		}
		else {
			account = getEntityDao().load(aKey);
		}
		Assert.assertNotNull(account);
		e.setAccount(account);

		Address address;
		if(adrKey == null) {
			address = getEntityDao().persist(getMockEntityFactory().getEntityCopy(Address.class, true));
			adrKey = new PrimaryKey<Address>(address);
		}
		else {
			address = getEntityDao().load(adrKey);
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
				final Account account = getEntityDao().load(aKey);
				getEntityDao().purge(account);
				getEntityDao().purge(account.getCurrency());
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			aKey = null;
		}

		if(adrKey != null) {
			try {
				getEntityDao().purge(getEntityDao().load(adrKey));
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