/*
 * The Logic Lab 
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.AddressType;
import com.tll.model.Asp;
import com.tll.model.Currency;

/**
 * AccountAddressDaoTestHandler
 * @author jpk
 */
public class AccountAddressDaoTestHandler extends AbstractEntityDaoTestHandler<AccountAddress> {

	Currency currency;
	Asp account;
	Address address;

	@Override
	public Class<AccountAddress> entityClass() {
		return AccountAddress.class;
	}

	@Override
	public boolean supportsPaging() {
		return false;
	}

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);

		account = create(Asp.class, true);
		account.setCurrency(currency);
		account.setPaymentInfo(null);
		account.setParent(null);
		account = persist(account);
		
		address = createAndPersist(Address.class, true);
	}

	@Override
	public void purgeDependentEntities() {
		purge(address);
		purge(account);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(AccountAddress e) throws Exception {
		e.setAccount(account);
		e.setAddress(address);
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
