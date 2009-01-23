/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.client.model;

import org.testng.annotations.Test;

import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Merchant;
import com.tll.model.MockEntityProvider;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;

/**
 * ModelCopyTest - Test the {@link Model#copy(boolean)} method.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelCopyTest extends AbstractModelTest {

	AccountAddress getCopyTestEntity() throws Exception {
		MockEntityProvider mep = injector.getInstance(MockEntityProvider.class);

		final AccountAddress aa = mep.getEntityCopy(AccountAddress.class, false);
		final Currency currency = mep.getEntityCopy(Currency.class, false);
		final Asp asp = mep.getEntityCopy(Asp.class, false);
		final Merchant merchant = mep.getEntityCopy(Merchant.class, false);
		final Address address = mep.getEntityCopy(Address.class, false);

		asp.setCurrency(currency);
		merchant.setCurrency(currency);

		merchant.setParent(asp);

		// NOTE: we add the account address to the account to ensure the objects are
		// wired correctly!
		merchant.addAccountAddress(aa);
		aa.setAddress(address);

		return aa;
	}

	/**
	 * Verifies the copy method.
	 * @throws Exception When the test fails.
	 */
	@Test
	public void test() throws Exception {
		final Marshaler em = injector.getInstance(Marshaler.class);
		final AccountAddress aa = getCopyTestEntity();
		final Model model = em.marshalEntity(aa, MarshalOptions.UNCONSTRAINED_MARSHALING);
		final Model copy = model.copy(true);
		validateCopy(model, copy, true);
	}
}
