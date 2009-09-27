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
 * ModelTest - Tests {@link Model} methods.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelTest {

	/**
	 * Test property path resolution by invoking {@link Model#getModelProperty(String)}
	 * @throws Exception Upon any encountered failure
	 */
	public void testGetModelProperty() throws Exception {
		final Model model = TestModelStubber.stubAccount(true);
		IModelProperty prop;
		String path;

		path = "name";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;

		path = "currency.iso4217";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;

		// test non-existant model one before end of path
		path = "parent.name";
		try {
			prop = model.getModelProperty(path);
		}
		catch(final NullNodeInPropPathException e) {
			// expected
		}

		path = "addresses";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;
		assert prop instanceof RelatedManyProperty : "Related many property value is the wrong type";

		path = "addresses[0]";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;
		assert prop instanceof IndexedProperty : "Expected AbstractModelRefProperty impl at property path: " + path;

		path = "addresses[20]";
		try {
			prop = model.getModelProperty(path);
		}
		catch(final IndexOutOfRangeInPropPathException e) {
			// expected
		}

		path = "addresses[20].address";
		try {
			prop = model.getModelProperty(path);
		}
		catch(final IndexOutOfRangeInPropPathException e) {
			// expected
		}

		path = "addresses[0].address.firstName";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;

		// test paymentInfo resolution
		path = "paymentInfo.paymentData_bankName";
		prop = model.getModelProperty(path);
		assert prop != null && prop instanceof StringPropertyValue : "Unable to resolve property path: " + path;

		// node mismatch
		try {
			path = "paymentInfo[2].name";
			prop = model.getModelProperty(path);
		}
		catch(final PropPathNodeMismatchException e) {
			// expected
		}

		// malformed
		try {
			path = "..??-";
			prop = model.getModelProperty(path);
		}
		catch(final PropertyPathException e) {
			// expected
		}
	}

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
	 * Verifies the clear method for clearing all property values.
	 * @throws Exception When the test fails.
	 */
	public void testClearAll() throws Exception {
		final Model model = TestModelStubber.stubAccount(true);
		model.clearPropertyValues(true, false);
		ModelTestUtils.validateClear(model, new ArrayList<Model>());
		Assert.assertTrue(model.getId() == null);
	}

	/**
	 * Verifies the clear method for clearing all property values but the id and version.
	 * @throws Exception
	 */
	public void testClearAllButIdAndVersion() throws Exception {
		final Model model = TestModelStubber.stubAccount(true);
		model.clearPropertyValues(true, true);
		Assert.assertTrue(model.getId() != null);
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

	/**
	 * Verifies the copy method.
	 * @throws Exception When the test fails.
	 */
	@Test(enabled = true)
	public void testCopyAll() throws Exception {
		final Model model = TestModelStubber.stubAccount(true);
		final Model copy = model.copy(CopyCriteria.all());
		ModelTestUtils.validateCopy(model, copy, CopyCriteria.all());
	}

	@Test(enabled = true)
	public void testCopyNoRefs() throws Exception {
		final Model model = TestModelStubber.stubAccount(true);
		final Model copy = model.copy(CopyCriteria.noReferences());
		Assert.assertFalse(copy.propertyExists("currency"));
		Assert.assertFalse(copy.propertyExists("parent"));
	}

	@Test(enabled = true)
	public void testCopyChanges() throws Exception {
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

		m.getNestedModel("addresses[1]").setMarkedDeleted(true);

		final CopyCriteria cc = CopyCriteria.changes(mpSet);

		final Model mcopy = m.copy(cc);

		Assert.assertTrue(mcopy.propertyExists("name"));
		Assert.assertTrue(mcopy.propertyExists("addresses[0].name"));
		Assert.assertFalse(mcopy.propertyExists("addresses[0].emailAddress"));

		// checked marked deleted
		Assert.assertTrue(mcopy.propertyExists("addresses[1]"));
		final Model aa = mcopy.getNestedModel("addresses[1]");
		Assert.assertTrue(aa.isMarkedDeleted());
		Assert.assertTrue(aa.propertyExists(Model.ID_PROPERTY));
		Assert.assertTrue(aa.propertyExists(Model.VERSION_PROPERTY));
		Assert.assertFalse(aa.propertyExists("emailAddress"));

		Assert.assertTrue(mcopy.propertyExists("paymentInfo.paymentData_bankAccountNo"));
		Assert.assertFalse(mcopy.propertyExists("currency"));
		Assert.assertFalse(mcopy.propertyExists("billingModel"));
	}

	@Test(enabled = true)
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
		Assert.assertEquals(changed.size(), 4); // we always copy the id and version too (2 + 2)
	}
}
