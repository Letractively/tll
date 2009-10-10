/*
 * The Logic Lab
 */
package com.tll.dao;

import org.testng.Assert;

import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Currency;
import com.tll.model.PaymentInfo;
import com.tll.model.key.PrimaryKey;
import com.tll.model.test.EntityBeanFactory;

/**
 * AbstractAccountDaoTestHandler
 * @param <A> the account type
 * @author jpk
 */
public abstract class AbstractAccountDaoTestHandler<A extends Account> extends AbstractEntityDaoTestHandler<A> {

	PrimaryKey<PaymentInfo> pkPaymentInfo;
	PrimaryKey<Currency> pkCurrency;
	PrimaryKey<Account> pkAccountParent;

	@Override
	public void persistDependentEntities() {
		final Currency currency = createAndPersist(Currency.class, true);
		final PaymentInfo paymentInfo = createAndPersist(PaymentInfo.class, true);
		pkCurrency = new PrimaryKey<Currency>(currency);

		Account parent = create(Account.class, true);
		parent.setParent(null); // eliminate pointer chasing
		parent.setCurrency(currency);
		parent.setPaymentInfo(paymentInfo);
		parent = persist(parent);
		pkAccountParent = new PrimaryKey<Account>(parent);
		pkPaymentInfo = new PrimaryKey<PaymentInfo>(paymentInfo);
	}

	@Override
	public void purgeDependentEntities() {
		purge(pkAccountParent); pkAccountParent = null;
		purge(pkPaymentInfo); pkPaymentInfo = null;
		purge(pkCurrency); pkCurrency = null;
	}

	@Override
	public void assembleTestEntity(A e) throws Exception {
		e.setCurrency(load(pkCurrency));
		e.setPaymentInfo(load(pkPaymentInfo));
		e.setParent(load(pkAccountParent));

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
				EntityBeanFactory.makeBusinessKeyUnique(aa);
				EntityBeanFactory.makeBusinessKeyUnique(aa.getAddress());
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