/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

import com.tll.TestUtils;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.Model;
import com.tll.common.model.ModelRefProperty;
import com.tll.common.model.RelatedManyProperty;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.Merchant;
import com.tll.model.MockEntityFactory;
import com.tll.model.schema.PropertyType;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;

/**
 * ModelCopyTest - Test the {@link Model#copy(boolean)} method.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelClearTest extends AbstractModelTest {

	AccountAddress getClearTestEntity() throws Exception {
		MockEntityFactory mep = injector.getInstance(MockEntityFactory.class);

		final AccountAddress aa = mep.getEntityCopy(AccountAddress.class, false);
		final Currency currency = mep.getEntityCopy(Currency.class, false);
		final Asp asp = mep.getEntityCopy(Asp.class, false);
		final Merchant merchant = mep.getEntityCopy(Merchant.class, false);
		final Address address = mep.getEntityCopy(Address.class, false);

		asp.setCurrency(currency);
		merchant.setCurrency(currency);

		merchant.setParent(asp);

		// NOTE: we add the account address to the account the objects are wired
		// correctly!
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
		final AccountAddress aa = getClearTestEntity();
		final Model model = em.marshalEntity(aa, MarshalOptions.UNCONSTRAINED_MARSHALING);
		model.clearPropertyValues(true);
		validateClear(model, new ArrayList<Model>());
	}

	private void validateClear(final Model source, final List<Model> visited) throws Exception {
		assert source != null;

		for(final Iterator<IModelProperty> itr = source.iterator(); itr.hasNext();) {
			final IModelProperty srcMp = itr.next();
			final PropertyType pvType = srcMp.getType();
			if(pvType.isValue()) {
				// require cleared property value
				final Object srcValue = srcMp.getValue();
				TestUtils.validateEmpty(srcValue);
			}
			else if(pvType == PropertyType.RELATED_ONE) {
				// drill into if not already visited
				final ModelRefProperty srcMrp = (ModelRefProperty) srcMp;
				final Model m = srcMrp.getModel();
				visited.add(m);
				if(m != null) {
					validateClear(m, visited);
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty srcRmp = (RelatedManyProperty) srcMp;
				final List<Model> srcSet = srcRmp.getList();
				if(srcSet != null) {
					for(final Model m : srcSet) {
						if(!visited.contains(m)) {
							visited.add(m);
							validateClear(m, visited);
						}
					}
				}
			}
		}
	}

}
