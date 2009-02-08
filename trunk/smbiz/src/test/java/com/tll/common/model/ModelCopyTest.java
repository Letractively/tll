/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import org.testng.annotations.Test;

import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Merchant;
import com.tll.model.mock.MockEntityFactory;
import com.tll.server.marshal.MarshalOptions;

/**
 * ModelCopyTest - Test the {@link Model#copy(boolean)} method.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelCopyTest extends AbstractModelTest {

	AccountAddress getCopyTestEntity() throws Exception {
		MockEntityFactory mep = getMockEntityFactory();

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
		final AccountAddress aa = getCopyTestEntity();
		final Model model = getMarshaler().marshalEntity(aa, MarshalOptions.UNCONSTRAINED_MARSHALING);
		final Model copy = model.copy(true);
		validateCopy(model, copy, true);
	}
}
