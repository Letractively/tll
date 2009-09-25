/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import java.util.ArrayList;
import java.util.HashSet;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.client.model.ModelPropertyChangeTracker;
import com.tll.common.model.test.TestEntityType;
import com.tll.common.model.test.TestModelStubber;

/**
 * ModelTest - Test the {@link Model#clearPropertyValues(boolean)} method.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelTest {

	/**
	 * Tests {@link Model#getRelPath(IModelProperty)}
	 * @throws Exception
	 */
	public void testGetRelPath() throws Exception {
		final Model m = TestModelStubber.stubAccount(true);
		final String[][] arrPropExp = new String[][] {
			{"parent", "parent"},
			{"billingModel", "billingModel"},
			{"paymentInfo", "paymentInfo"},
			{"paymentInfo.paymentData_bankAccountNo", "paymentInfo.paymentData_bankAccountNo"},
			{"addresses", "addresses"},
			{"addresses[0]", "addresses[0]"},
			{"addresses[0].address", "addresses[0].address"},
			{"addresses[0].address.firstName", "addresses[0].address.firstName"},
			{"addresses[0].account", null},	// make sure we properly handle circularity
		};
		for(final String[] pe : arrPropExp) {
			final String prop = pe[0];
			final IModelProperty descendant = m.getModelProperty(prop);
			final String actual = m.getRelPath(descendant);
			final String expected = pe[1];
			Assert.assertEquals(actual, expected);

			// verify the converse
			if(actual != null) {
				final IModelProperty mp = m.getModelProperty(actual);
				Assert.assertEquals(mp, descendant);
			}
		}
	}

	/**
	 * Verifies the clear method.
	 * @throws Exception When the test fails.
	 */
	public void testClear() throws Exception {
		final Model model = TestModelStubber.stubAccount(true);
		model.clearPropertyValues(true);
		ModelTestUtils.validateClear(model, new ArrayList<Model>());
	}

	/**
	 * Verifies the copy method.
	 * @throws Exception When the test fails.
	 */
	public void testCopy() throws Exception {
		final Model model = TestModelStubber.stubAccount(true);
		final Model copy = model.copy(CopyCriteria.COPY_ALL);
		ModelTestUtils.validateCopy(model, copy, CopyCriteria.COPY_ALL);
	}

	/**
	 * Verifies the copy method with the copy marked deleted flag use case.
	 * @throws Exception
	 */
	public void testMarkedDeleted() throws Exception {
		final Model model = TestModelStubber.stubAccount(true);

		IModelRefProperty mrp;

		// mark deleted a related on model
		mrp = (IModelRefProperty) model.getModelProperty("currency");
		assert mrp != null;
		mrp.getModel().setMarkedDeleted(true);

		// mark deleted a related many model element
		mrp = (IModelRefProperty) model.getModelProperty("addresses[0]");
		assert mrp != null;
		mrp.getModel().setMarkedDeleted(true);

		final CopyCriteria criteria = new CopyCriteria(true, true, false, null);
		final Model copy = model.copy(criteria);
		ModelTestUtils.validateCopy(model, copy, criteria);
	}

	public void testGetSetEntityRelatedProps() throws Exception {
		final Model model = TestModelStubber.stubModel(TestEntityType.ADDRESS, null, false, null);

		final String id = model.getId();
		Assert.assertTrue(id != null);
		model.setId("6000");
		Assert.assertTrue(model.getId() != null && model.getId().equals("6000"));

		Assert.assertTrue(model.getVersion() == null);
		model.setVersion(1);
		Assert.assertTrue(model.getVersion() == 1);
	}

	public void testModelCopyNoRefsAndMarkedDeletedAndWhiteList() throws Exception {
		final Model m = TestModelStubber.stubAccount(true);
		IModelProperty mp;
		final HashSet<IModelProperty> mpSet = new HashSet<IModelProperty>();

		mp = m.getModelProperty("name");
		mp.setValue("cname");
		mpSet.add(mp);

		mp = m.getModelProperty("addresses[0].name");
		mp.setValue("caaname");
		mpSet.add(mp);

		mp = m.getModelProperty("paymentInfo.paymentData_bankAccountNo");
		mp.setValue("altereD");
		mpSet.add(mp);

		final CopyCriteria cc = new CopyCriteria(true, false, true, mpSet);

		final Model mcopy = m.copy(cc);

		Assert.assertTrue(mcopy.propertyExists("name"));
		Assert.assertTrue(mcopy.propertyExists("addresses[0].name"));
		Assert.assertTrue(mcopy.propertyExists("paymentInfo.paymentData_bankAccountNo"));
		Assert.assertFalse(mcopy.propertyExists("currency"));
		Assert.assertFalse(mcopy.propertyExists("billingModel"));
	}

	public void testModelPropertyChangeTracker() throws Exception {
		final Model m = TestModelStubber.stubAddress(1);
		final ModelPropertyChangeTracker changeTracker = new ModelPropertyChangeTracker();
		changeTracker.set(m);
		for(final IModelProperty mp : m) {
			mp.addPropertyChangeListener(changeTracker);
		}
		m.getModelProperty("emailAddress").setValue("changed email");
		m.getModelProperty("address1").setValue("change address1");

		final Model changed = changeTracker.generateChangeModel();
		Assert.assertEquals(changed.size(), 4); // id and version are there by default (2 + 2)
	}
}
