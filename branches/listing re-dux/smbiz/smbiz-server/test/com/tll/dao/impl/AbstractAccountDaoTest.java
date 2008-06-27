/*
 * The Logic Lab
 */
package com.tll.dao.impl;

import javax.persistence.EntityNotFoundException;

import org.testng.Assert;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.MockEntityProvider;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.Address;
import com.tll.model.impl.Currency;
import com.tll.model.impl.PaymentInfo;
import com.tll.model.key.PrimaryKey;

/**
 * AbstractAccountDaoTest
 * @author jpk
 */
public abstract class AbstractAccountDaoTest<A extends Account> extends NamedEntityDaoTest<A> {

	PrimaryKey<PaymentInfo> piKey;
	PrimaryKey<Currency> cKey;
	PrimaryKey<Address> a1Key;
	PrimaryKey<Address> a2Key;
	PrimaryKey<Account> parentKey;

	/**
	 * Constructor
	 */
	public AbstractAccountDaoTest(Class<A> accountClass) {
		super(accountClass, IAccountDao.class);
	}

	@Override
	protected void assembleTestEntity(A e) throws Exception {

		Currency currency;
		if(cKey == null) {
			// load stubbed currency
			currency = getMockEntityProvider().getEntityCopy(Currency.class);
			currency = getDao(ICurrencyDao.class).persist(currency);
			cKey = new PrimaryKey<Currency>(currency);
		}
		else {
			currency = getDao(ICurrencyDao.class).load(cKey);
		}
		Assert.assertNotNull(currency);
		e.setCurrency(currency);

		PaymentInfo paymentInfo;
		if(piKey == null) {
			// stub payment info
			try {
				paymentInfo = getMockEntityProvider().getEntityCopy(PaymentInfo.class);
			}
			catch(final Exception ex) {
				Assert.fail("Unable to acquire test payment info entity");
				return;
			}
			paymentInfo = getDao(IPaymentInfoDao.class).persist(paymentInfo);
			piKey = new PrimaryKey<PaymentInfo>(paymentInfo);
		}
		else {
			paymentInfo = getDao(IPaymentInfoDao.class).load(piKey);
		}
		Assert.assertNotNull(paymentInfo);
		e.setPaymentInfo(paymentInfo);

		// stub account parent (if specified)
		if(e.getParent() != null) {
			Account parent = e.getParent();
			if(parent.isNew()) {
				if(parentKey == null) {
					getEntityAssembler().setGenerated(parent);
					parent.setParent(null); // eliminate pointer chasing
					parent.setCurrency(currency);
					parent.setPaymentInfo(paymentInfo);
					parent = getDao(IAccountDao.class).persist(parent);
					parentKey = new PrimaryKey<Account>(parent);
				}
				else {
					parent = getDao(IAccountDao.class).load(parentKey);
				}
				Assert.assertNotNull(parent);
				e.setParent(parent);
			}
		}

		Address a1;
		if(a1Key == null) {
			a1 = getDao(IAddressDao.class).persist(getMockEntityProvider().getEntityCopy(Address.class, 1));
			a1Key = new PrimaryKey<Address>(a1);
		}
		else {
			a1 = getDao(IAddressDao.class).load(a1Key);
		}
		Assert.assertNotNull(a1);

		Address a2;
		if(a2Key == null) {
			a2 = getDao(IAddressDao.class).persist(getMockEntityProvider().getEntityCopy(Address.class, 2));
			a2Key = new PrimaryKey<Address>(a2);
		}
		else {
			a2 = getDao(IAddressDao.class).load(a2Key);
		}
		Assert.assertNotNull(a2);

		final AccountAddress aa1 = getMockEntityProvider().getEntityCopy(AccountAddress.class, 1);
		final AccountAddress aa2 = getMockEntityProvider().getEntityCopy(AccountAddress.class, 2);
		aa1.setAddress(a1);
		aa2.setAddress(a2);
		e.addAccountAddress(aa1);
		e.addAccountAddress(aa2);
	}

	@Override
	protected void afterMethodHook() {
		startNewTransaction();

		if(piKey != null) {
			try {
				getDao(IPaymentInfoDao.class).purge(getDao(IPaymentInfoDao.class).load(piKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			piKey = null;
		}

		if(cKey != null) {
			try {
				getDao(ICurrencyDao.class).purge(getDao(ICurrencyDao.class).load(cKey));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			cKey = null;
		}

		if(a1Key != null) {
			try {
				getDao(IAddressDao.class).purge(getDao(IAddressDao.class).load(a1Key));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			a1Key = null;
		}

		if(a2Key != null) {
			try {
				getDao(IAddressDao.class).purge(getDao(IAddressDao.class).load(a2Key));
			}
			catch(final EntityNotFoundException enfe) {
				// ok
			}
			a2Key = null;
		}

		if(parentKey != null) {
			try {
				getDao(IAccountDao.class).purge(getDao(IAccountDao.class).load(parentKey));
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
	protected void uniquify(A e) {
		makeUnique(e);

		try {
			if(e.getAddresses() != null) {
				for(final AccountAddress aa : e.getAddresses()) {
					MockEntityProvider.makeBusinessKeyUnique(aa);
					MockEntityProvider.makeBusinessKeyUnique(aa.getAddress());
				}
			}
		}
		catch(final BusinessKeyNotDefinedException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Override
	protected void verifyLoadedEntityState(A e) throws Exception {
		super.verifyLoadedEntityState(e);

		Assert.assertNotNull(e.getCurrency(), "No account currency loaded");
		Assert.assertNotNull(e.getPaymentInfo(), "No account payment info loaded");
		Assert.assertTrue(e.getAddresses() != null && e.getAddresses().size() == 2,
				"No account address collection loaded or invalid number of them");
	}

}
