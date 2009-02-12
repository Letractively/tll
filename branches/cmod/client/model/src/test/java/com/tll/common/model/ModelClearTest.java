/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.tll.common.model.mock.TestModelStubber;

/**
 * ModelCopyTest - Test the {@link Model#copy(boolean)} method.
 * @author jpk
 */
@Test(groups = "client-model")
public class ModelClearTest extends AbstractModelTest {

	/**
	 * Verifies the copy method.
	 * @throws Exception When the test fails.
	 */
	@Test
	public void test() throws Exception {
		final Model model = TestModelStubber.stubTestModel();
		model.clearPropertyValues(true);
		ModelTestUtils.validateClear(model, new ArrayList<Model>());
	}
}
