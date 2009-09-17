/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import org.testng.annotations.Test;

import com.tll.common.model.test.MockModelStubber;
import com.tll.common.model.test.MockModelStubber.ModelType;

/**
 * ModelCopyTest - Test the {@link Model#copy(boolean)} method.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelCopyTest {

	/**
	 * Verifies the copy method.
	 * @throws Exception When the test fails.
	 */
	public void test() throws Exception {
		final Model model = MockModelStubber.create(ModelType.COMPLEX);
		final Model copy = model.copy(true);
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
		
		final Model copy = model.copy(true, false);
		ModelTestUtils.validateCopy(model, copy, true, false);
	}
}
