/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.Address;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.Merchant;
import com.tll.model.schema.PropertyType;
import com.tll.server.rpc.MarshalOptions;
import com.tll.server.rpc.Marshaler;

/**
 * ModelCopyTest - Test the {@link Model#copy()} method.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelClearTest extends AbstractModelTest {

	AccountAddress getClearTestEntity() throws Exception {
		final AccountAddress aa = getMockEntityProvider().getEntityCopy(AccountAddress.class, false);

		final Currency currency = getMockEntityProvider().getEntityCopy(Currency.class, false);
		final Asp asp = getMockEntityProvider().getEntityCopy(Asp.class, false);
		final Merchant merchant = getMockEntityProvider().getEntityCopy(Merchant.class, false);
		final Address address = getMockEntityProvider().getEntityCopy(Address.class, false);

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
		final Marshaler em = getMarshaler();
		final AccountAddress aa = getClearTestEntity();
		final Model model = em.marshalEntity(aa, MarshalOptions.UNCONSTRAINED_MARSHALING);
		model.clearPropertyValues();
		validateClear(model, new ArrayList<Model>());
	}

	private void validateClear(final Model source, final List<Model> visited) throws Exception {
		assert source != null;

		for(final Iterator<IModelProperty> itr = source.iterator(); itr.hasNext();) {
			final IModelProperty srcPv = itr.next();
			final PropertyType pvType = srcPv.getType();
			if(pvType.isValue()) {
				// require cleared property value
				final Object srcValue = srcPv.getValue();
				validateEmpty(srcValue);
			}
			else if(pvType == PropertyType.RELATED_ONE) {
				// drill into if not already visited
				final ModelRefProperty srcGpv = (ModelRefProperty) srcPv;
				final Model srcPvg = srcGpv.getModel();
				visited.add(srcPvg);
				if(srcPvg != null) {
					validateClear(srcPvg, visited);
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty srcGlpv = (RelatedManyProperty) srcPv;
				final List<Model> srcList = srcGlpv.getList();
				if(srcList != null) {
					for(final Model srcPvg : srcList) {
						if(!visited.contains(srcPvg)) {
							visited.add(srcPvg);
							validateClear(srcPvg, visited);
						}
					}
				}
			}
		}
	}

}
