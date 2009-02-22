/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import com.tll.common.model.mock.TestModelStubber;

/**
 * ModelCopyTest - Test the {@link Model#copy(boolean)} method.
 * @author jpk
 */
public class ModelCopyTest {

	/**
	 * Verifies the copy method.
	 * @throws Exception When the test fails.
	 */
	public void test() throws Exception {
		final Model model = TestModelStubber.stubTestModel();
		final Model copy = model.copy(true);
		ModelTestUtils.validateCopy(model, copy, true);
	}
}
