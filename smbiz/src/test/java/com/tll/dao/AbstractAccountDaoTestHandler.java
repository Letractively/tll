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
import com.tll.model.EntityBeanFactory;
import com.tll.model.PaymentInfo;

/**
 * AbstractAccountDaoTestHandler
 * @param <A> the account type
 * @author jpk
 */
public abstract class AbstractAccountDaoTestHandler<A extends Account> extends AbstractEntityDaoTestHandler<A> {

	PaymentInfo paymentInfo;
	Currency currency;
	Account parent;

	@Override
	public void persistDependentEntities() {
		currency = createAndPersist(Currency.class, true);
		paymentInfo = createAndPersist(PaymentInfo.class, true);

		parent = create(Asp.class, true);
		parent.setParent(null); // eliminate pointer chasing
		parent.setCurrency(currency);
		parent.setPaymentInfo(paymentInfo);
		parent = persist(parent);
	}

	@Override
	public void purgeDependentEntities() {
		purge(parent);
		purge(paymentInfo);
		purge(currency);
	}

	@Override
	public void assembleTestEntity(Account e) throws Exception {

		e.setCurrency(currency);
		e.setPaymentInfo(paymentInfo);
		e.setParent(parent);

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

}
