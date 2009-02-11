/**
 * The Logic Lab
 * @author jpk
 * Dec 28, 2008
 */
package com.tll.client.bind;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;
import com.tll.client.ClientTestUtils;
import com.tll.common.bind.IBindable;
import com.tll.common.bind.PropertyChangeSupport;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.IndexOutOfRangeInPropPathException;
import com.tll.common.model.Model;

/**
 * ModelBindingTest - Test that verifies client side data binding using
 * {@link Model} {@link IBindable} implementation.
 * @author jpk
 */
public class ModelBindingTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.tll.Test";
	}

	/**
	 * Verifies the aggregation of a {@link Model}'s {@link PropertyChangeSupport}
	 * instance among its {@link IModelProperty}s.
	 * @throws Exception
	 */
	public void testModelChangeSupportAggregation() throws Exception {

		Model[] lr = stubLeftAndRight();
		Model left = lr[0];
		Model right = lr[1];

		Binding binding = new Binding();
		List<Binding> children = binding.getChildren();
		children.add(new Binding(left, right, Model.ID_PROPERTY));
		children.add(new Binding(left, right, Model.NAME_PROPERTY));
		children.add(new Binding(left, right, Model.DATE_CREATED_PROPERTY));
		children.add(new Binding(left, right, Model.DATE_MODIFIED_PROPERTY));
		children.add(new Binding(left, right, "parent.name"));
		children.add(new Binding(left, right, "addresses[0].address.firstName"));

		binding.unbind();
	}

	/**
	 * Tests the {@link Binding}'s auto-propagation of property changes from one
	 * binding end point to the other (right <--> left).
	 * @throws Exception
	 */
	public void testPropertyChangeSyncing() throws Exception {

		Model[] lr = stubLeftAndRight();
		Model left = lr[0];
		Model right = lr[1];

		Binding binding = new Binding();
		List<Binding> children = binding.getChildren();
		children.add(new Binding(left, right, Model.ID_PROPERTY));
		children.add(new Binding(left, right, Model.NAME_PROPERTY));
		children.add(new Binding(left, right, Model.DATE_CREATED_PROPERTY));
		children.add(new Binding(left, right, Model.DATE_MODIFIED_PROPERTY));
		children.add(new Binding(left, right, "parent.name"));
		children.add(new Binding(left, right, "addresses[0].address.firstName"));

		binding.bind();

		// clear out the bound properties on the right..
		right.clearPropertyValue(Model.ID_PROPERTY);
		right.clearPropertyValue(Model.NAME_PROPERTY);
		right.clearPropertyValue(Model.DATE_CREATED_PROPERTY);
		right.clearPropertyValue(Model.DATE_MODIFIED_PROPERTY);
		right.clearPropertyValue("parent.name");
		right.clearPropertyValue("addresses[0].address.firstName");

		verifyInSync(left, right);

		// set bound properties on the left..
		left.setProperty(Model.ID_PROPERTY, 33);
		left.setProperty(Model.NAME_PROPERTY, "new name");
		left.setProperty(Model.DATE_CREATED_PROPERTY, new Date());
		left.setProperty(Model.DATE_MODIFIED_PROPERTY, new Date());
		left.setProperty("parent.name", "newboy");
		left.setProperty("addresses[0].address.firstName", "new first");

		verifyInSync(left, right);

		binding.unbind();
	}

	/**
	 * Test index property change handling.
	 * @throws Exception
	 */
	public void testIndexedPropertyMutation() throws Exception {

		Model[] lr = stubLeftAndRight();
		Model left = lr[0];
		Model right = lr[1];

		// create the binding
		Binding binding = new Binding();
		binding.getChildren().add(new Binding(left, right, "addresses"));
		binding.bind();

		Object val;

		// remove indexed property on the left
		left.setProperty("addresses[1]", null);

		// verify
		try {
			val = left.getProperty("addresses[1]");
		}
		catch(IndexOutOfRangeInPropPathException e) {
			// expected
		}
		val = left.getProperty("addresses");
		Assert.assertTrue(val instanceof List && ((List<?>) val).size() == 1);

		val = right.getProperty("addresses");
		Assert.assertTrue(val instanceof List && ((List<?>) val).size() == 1);

		// add an indexed property on the right
		Model aa = ClientTestUtils.stubAccountAddress(right, ClientTestUtils.stubAddress(2), 2);
		right.setProperty("addresses[1]", aa);

		// verify
		val = right.getProperty("addresses");
		Assert.assertTrue(val instanceof List && ((List<?>) val).size() == 2);

		binding.unbind();
	}

	/**
	 * Verifies two given {@link Model} instances are memory address distinct but
	 * have equal model property values.
	 * @param left
	 * @param right
	 * @throws Exception
	 */
	protected void verifyInSync(Model left, Model right) throws Exception {
		// verify the properties changes are syncd
		try {
			ClientTestUtils.equals(left, right, true);
		}
		catch(Exception e) {
			throw new Exception("Model " + left.toString() + " is out of sync with: " + right.toString(), e);
		}
	}

	/**
	 * Stubs distinct left and right {@link Model} instances
	 * @return 2 element array where the first element is the left model
	 */
	protected Model[] stubLeftAndRight() throws Exception {
		Model left = ClientTestUtils.getTestRootModel();
		Model right = left.copy(true);
		// right.setAsRoot();

		// sanity check: verify we are equal before we bind
		ClientTestUtils.validateCopy(left, right, true);

		return new Model[] {
			left, right };
	}
}