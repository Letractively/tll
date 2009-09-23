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
import com.tll.common.model.test.MockEntityType;
import com.tll.common.model.test.MockModelStubber;
import com.tll.common.model.test.MockModelStubber.ModelType;

/**
 * ModelTest - Test the {@link Model#clearPropertyValues(boolean)} method.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelTest {

	/**
	 * Verifies the clear method.
	 * @throws Exception When the test fails.
	 */
	public void testClear() throws Exception {
		final Model model = MockModelStubber.create(ModelType.COMPLEX);
		model.clearPropertyValues(true);
		ModelTestUtils.validateClear(model, new ArrayList<Model>());
	}

	/**
	 * Verifies the copy method.
	 * @throws Exception When the test fails.
	 */
	public void testCopy() throws Exception {
		final Model model = MockModelStubber.create(ModelType.COMPLEX);
		final Model copy = model.copy(CopyCriteria.COPY_ALL);
		ModelTestUtils.validateCopy(model, copy, true, true);
	}

	/**
	 * Verifies the copy method with the copy marked deleted flag use case.
	 * @throws Exception
	 */
	public void testMarkedDeleted() throws Exception {
		final Model model = MockModelStubber.create(ModelType.COMPLEX);

		IModelRefProperty mrp;

		// mark deleted a related on model
		mrp = (IModelRefProperty) model.getModelProperty("currency");
		assert mrp != null;
		mrp.getModel().setMarkedDeleted(true);

		// mark deleted a related many model element
		mrp = (IModelRefProperty) model.getModelProperty("addresses[1]");
		assert mrp != null;
		mrp.getModel().setMarkedDeleted(true);

		final Model copy = model.copy(new CopyCriteria(true, false, null, null));
		ModelTestUtils.validateCopy(model, copy, true, false);
	}

	public void testGetSetEntityRelatedProps() throws Exception {
		final Model model = MockModelStubber.stubModel(MockEntityType.ADDRESS, null, false, null);

		final String id = model.getId();
		Assert.assertTrue(id != null);
		model.setId("6000");
		Assert.assertTrue(model.getId() != null && model.getId().equals("6000"));

		Assert.assertTrue(model.getVersion() != null);
		model.setVersion(1);
		Assert.assertTrue(model.getVersion() == 1);
	}

	public void testModelCopyNoRefsAndMarkedDeletedAndWhiteList() throws Exception {
		final Model m = MockModelStubber.stubAccount(true);
		IModelProperty mp;
		final HashSet<IModelProperty> mpSet = new HashSet<IModelProperty>();

		mp = m.getModelProperty("name");
		mp.setValue("cname");
		mpSet.add(mp);

		mp = m.getModelProperty("addresses[0].name");
		mp.setValue("caaname");
		mpSet.add(mp);

		mp = m.indexed("addresses[1]");
		((IndexedProperty)mp).getModel().setMarkedDeleted(true);
		mpSet.add(mp);

		final CopyCriteria cc = new CopyCriteria(false, true, null, mpSet);

		final Model mcopy = m.copy(cc);

		Assert.assertTrue(mcopy.propertyExists("name"));
		Assert.assertTrue(mcopy.propertyExists("addresses[0].name"));
		Assert.assertFalse(mcopy.propertyExists("addresses[1]"));
		Assert.assertFalse(mcopy.propertyExists("currency"));
		Assert.assertFalse(mcopy.propertyExists("billingModel"));
	}

	public void testModelCopyNoRefsAndMarkedDeletedAndBlackList() throws Exception {
		//final Model m = MockModelStubber.stubAccount(true);
	}

	public void testModelPropertyChangeTracker() throws Exception {
		final Model m = MockModelStubber.stubAccount(true);
		final ModelPropertyChangeTracker changeTracker = new ModelPropertyChangeTracker();
		changeTracker.set(m);
		m.addPropertyChangeListener(changeTracker);
		// TODO finish
	}
}
