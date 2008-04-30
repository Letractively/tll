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
public class ModelCopyTest extends AbstractModelTest {

	AccountAddress getCopyTestEntity() throws Exception {
		final AccountAddress aa = getMockEntityProvider().getEntityCopy(AccountAddress.class);

		final Currency currency = getMockEntityProvider().getEntityCopy(Currency.class);
		final Asp asp = getMockEntityProvider().getEntityCopy(Asp.class);
		final Merchant merchant = getMockEntityProvider().getEntityCopy(Merchant.class);
		final Address address = getMockEntityProvider().getEntityCopy(Address.class);

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
		final Marshaler em = getMarshaler();
		final AccountAddress aa = getCopyTestEntity();
		final Model model = em.marshalEntity(aa, MarshalOptions.UNCONSTRAINED_MARSHALING);
		final Model copy = model.copy();
		validateCopy(model, copy, new ArrayList<Model>());
	}

	/**
	 * Validates a source against a copied {@link Model}.
	 * @param source
	 * @param copy
	 * @param visited
	 * @throws Exception When a copy discrepancy is encountered
	 */
	@SuppressWarnings("unchecked")
	private void validateCopy(final Model source, final Model copy, final List<Model> visited) throws Exception {
		assert source != null && copy != null;

		for(final Iterator<IPropertyBinding> itr = source.iterator(); itr.hasNext();) {
			final IPropertyBinding srcPv = itr.next();
			final String propName = srcPv.getPropertyName();
			final IPropertyBinding cpyPv = copy.getPropertyBinding(propName);

			// verify like types
			validateEqualTypes(srcPv, cpyPv);

			// ensure the copy is not the source!
			validateNotEqualByMemoryAddress(srcPv, cpyPv);

			final PropertyType pvType = srcPv.getType();
			if(pvType.isValue()) {
				// require logical equals
				final Object srcValue = srcPv.getValue();
				final Object cpyValue = cpyPv.getValue();
				validateEquals(srcValue, cpyValue);
			}
			else if(pvType == PropertyType.RELATED_ONE) {
				// drill into if not already visited
				final ModelRefProperty srcGpv = (ModelRefProperty) srcPv;
				final ModelRefProperty cpyGpv = (ModelRefProperty) cpyPv;
				final Model srcPvg = srcGpv.getModel();
				final Model cpyPvg = cpyGpv.getModel();
				if(srcPvg == null && cpyPvg != null) {
					throw new Exception("Source prop val group found null but not the copy!");
				}
				else if(!srcGpv.isReference() && srcPvg != null && !visited.contains(srcPvg)) {
					visited.add(srcPvg);
					validateCopy(srcPvg, cpyPvg, visited);
				}
			}
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty srcGlpv = (RelatedManyProperty) srcPv;
				final RelatedManyProperty cpyGlpv = (RelatedManyProperty) cpyPv;
				final List<Model> srcList = srcGlpv.getList();
				final List<Model> cpyList = cpyGlpv.getList();

				if((srcList == null && cpyList != null) || (srcList != null && cpyList == null)) {
					throw new Exception("Source and copy group list property values differ by null");
				}
				if(srcList != null) {
					if(srcList.size() != cpyList.size()) {
						throw new Exception("Source and copy group list property sizes differ");
					}
					if(!srcGlpv.isReference()) {
						final Iterator<Model> citr = cpyList.iterator();
						for(final Model srcPvg : srcList) {
							final Model cpyPvg = citr.next();
							if(!visited.contains(srcPvg)) {
								visited.add(srcPvg);
								validateCopy(srcPvg, cpyPvg, visited);
							}
						}
					}
				}
			}
		}
	}

	// TODO create deep copy non-refernce having test!!!
}
