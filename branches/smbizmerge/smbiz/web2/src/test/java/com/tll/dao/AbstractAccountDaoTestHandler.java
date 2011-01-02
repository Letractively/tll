/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.PaymentInfo;
import com.tll.model.bk.BusinessKeyFactory;

/**
 * AbstractAccountDaoTestHandler
 * @param <A> the account type
 * @author jpk
 */
public abstract class AbstractAccountDaoTestHandler<A extends Account> extends AbstractEntityDaoTestHandler<A> {

	Long pkPaymentInfo, pkCurrency, pkAccountParent;

	@Override
	public void doPersistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		final PaymentInfo paymentInfo = createAndPersist(PaymentInfo.class, true);
		pkCurrency = currency.getId();

		Asp parent = create(Asp.class, true);
		parent.setParent(null); // eliminate pointer chasing
		parent.setCurrency(currency);
		parent.setPaymentInfo(paymentInfo);
		parent = persist(parent);
		pkAccountParent = parent.getId();
		pkPaymentInfo = paymentInfo.getId();
	}

	@Override
	public void doPurgeDependentEntities() {
		purge(Account.class, pkAccountParent); pkAccountParent = null;
		purge(PaymentInfo.class, pkPaymentInfo); pkPaymentInfo = null;
		purge(Currency.class, pkCurrency); pkCurrency = null;
	}

	@Override
	public void assembleTestEntity(A e) throws Exception {
		e.setCurrency(load(Currency.class, pkCurrency));
		e.setPaymentInfo(load(PaymentInfo.class, pkPaymentInfo));
		e.setParent(load(Account.class, pkAccountParent));

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
	public void makeUnique(A e) {
		super.makeUnique(e);
		if(e.getAddresses() != null) {
			for(final AccountAddress aa : e.getAddresses()) {
				BusinessKeyFactory.makeBusinessKeyUnique(aa);
				BusinessKeyFactory.makeBusinessKeyUnique(aa.getAddress());
			}
		}
	}

	@Override
	public void verifyLoadedEntityState(A e) throws Exception {
		super.verifyLoadedEntityState(e);

		Assert.assertNotNull(e.getCurrency(), "No account currency loaded");
		Assert.assertNotNull(e.getPaymentInfo(), "No account payment info loaded");
		Assert.assertNotNull(e.getPaymentInfo().getPaymentData(), "No account payment info data loaded");
		Assert.assertTrue(e.getAddresses() != null && e.getAddresses().size() == 2,
		"No account address collection loaded or invalid number of them");
	}

	@Override
	public void verifyEntityAlteration(A e) throws Exception {
		super.verifyEntityAlteration(e);
		Assert.assertNotNull(e.getCurrency(), "No account currency loaded");
		Assert.assertNotNull(e.getPaymentInfo(), "No account payment info loaded");
		Assert.assertNotNull(e.getPaymentInfo().getPaymentData(), "No account payment info data loaded");
	}
}
