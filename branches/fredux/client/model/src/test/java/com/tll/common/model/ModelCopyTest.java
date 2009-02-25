/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import org.testng.annotations.Test;

import com.tll.common.model.mock.MockModelStubber;
import com.tll.common.model.mock.MockModelStubber.ModelType;

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
		ModelTestUtils.validateCopy(model, copy, true);
	}
}
