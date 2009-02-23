/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import java.util.ArrayList;

import com.tll.common.model.mock.MockModelStubber;
import com.tll.common.model.mock.MockModelStubber.ModelType;

/**
 * ModelCopyTest - Test the {@link Model#copy(boolean)} method.
 * @author jpk
 */
public class ModelClearTest {

	/**
	 * Verifies the copy method.
	 * @throws Exception When the test fails.
	 */
	public void test() throws Exception {
		final Model model = MockModelStubber.create(ModelType.COMPLEX);
		model.clearPropertyValues(true);
		ModelTestUtils.validateClear(model, new ArrayList<Model>());
	}
}
