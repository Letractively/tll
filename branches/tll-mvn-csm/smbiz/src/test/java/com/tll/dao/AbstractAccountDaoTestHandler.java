/*
 * The Logic Lab
 */
package com.tll.dao;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Currency;
import com.tll.model.PaymentInfo;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractAccountDaoTestHandler
 * @param <A> the account type
 * @author jpk
 */
public abstract class AbstractAccountDaoTestHandler<A extends Account> extends AbstractEntityDaoTestHandler<A> {

	PrimaryKey<PaymentInfo> piKey;
	PrimaryKey<Currency> cKey;
	PrimaryKey<Address> a1Key;
	PrimaryKey<Address> a2Key;
	PrimaryKey<Account> parentKey;

	@Override
	public void assembleTestEntity(A e) throws Exception {

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

		PaymentInfo paymentInfo;
		if(piKey == null) {
			// stub payment info
			try {
				paymentInfo = mockEntityFactory.getEntityCopy(PaymentInfo.class, true);
			}
			catch(final Exception ex) {
				Assert.fail("Unable to acquire test payment info entity");
				return;
			}
			paymentInfo = entityDao.persist(paymentInfo);
			piKey = new PrimaryKey<PaymentInfo>(paymentInfo);
		}
		else {
			paymentInfo = entityDao.load(piKey);
		}
		Assert.assertNotNull(paymentInfo);
		e.setPaymentInfo(paymentInfo);

		// stub account parent (if specified)
		if(e.getParent() != null) {
			Account parent = e.getParent();
			if(parent.isNew()) {
				if(parentKey == null) {
					entityFactory.setGenerated(parent);
					parent.setParent(null); // eliminate pointer chasing
					parent.setCurrency(currency);
					parent.setPaymentInfo(paymentInfo);
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
			a1 = entityDao.persist(mockEntityFactory.getEntityCopy(Address.class, true));
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
	public void teardownTestEntity(A e) {
		if(piKey != null) {
			try {
				entityDao.purge(entityDao.load(piKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			piKey = null;
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
	public void makeUnique(A e) {
		super.makeUnique(e);
		if(e.getAddresses() != null) {
			for(final AccountAddress aa : e.getAddresses()) {
				mockEntityFactory.makeBusinessKeyUnique(aa);
				mockEntityFactory.makeBusinessKeyUnique(aa.getAddress());
			}
		}
	}

	@Override
	public void verifyLoadedEntityState(A e) throws Exception {
		super.verifyLoadedEntityState(e);

		Assert.assertNotNull(e.getCurrency(), "No account currency loaded");
		Assert.assertNotNull(e.getPaymentInfo(), "No account payment info loaded");
		Assert.assertTrue(e.getAddresses() != null && e.getAddresses().size() == 2,
				"No account address collection loaded or invalid number of them");
	}

}
