/**
 * The Logic Lab
 * @author jpk
 * Dec 28, 2008
 */
package com.tll.client.bind;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.TestUtils;
import com.tll.client.model.DatePropertyValue;
import com.tll.client.model.EnumPropertyValue;
import com.tll.client.model.IModelProperty;
import com.tll.client.model.IntPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.model.RelatedOneProperty;
import com.tll.client.model.StringPropertyValue;
import com.tll.model.EntityType;
import com.tll.model.impl.AddressType;

/**
 * ModelBindingTest - Test that verifies client side data binding using
 * {@link Model} {@link IBindable} implementation.
 * @author jpk
 */
@Test(groups = {
	"client-bind", "client-model" })
public class ModelBindingTest {

	/**
	 * Verifies the aggregation of a {@link Model}'s {@link PropertyChangeSupport}
	 * instance among its {@link IModelProperty}s.
	 * @throws Exception
	 */
	@Test(enabled = false)
	public void testModelChangeSupportAggregation() throws Exception {

		Model left = stubModel();
		Model right = left.copy(true);

		// sanity check: verify we are equal before we bind
		verifyInSync(left, right);

		Binding binding = new Binding();
		List<Binding> children = binding.getChildren();
		children.add(new Binding(left, right, Model.ID_PROPERTY));
		children.add(new Binding(left, right, Model.NAME_PROPERTY));
		children.add(new Binding(left, right, Model.DATE_CREATED_PROPERTY));
		children.add(new Binding(left, right, Model.DATE_MODIFIED_PROPERTY));
		children.add(new Binding(left, right, "parent.name"));
		children.add(new Binding(left, right, "addresses[0].address.firstName"));

		Object[] pcls;

		binding.bind();
		pcls = left.getPropertyChangeListeners();
		assert pcls != null && pcls.length > 0;

		binding.unbind();
		pcls = left.getPropertyChangeListeners();
		assert pcls == null || pcls.length == 0;

		binding.unbind();
	}

	/**
	 * Tests the {@link Binding}'s auto-propagation of property changes from one
	 * binding end point to the other (right <--> left).
	 * @throws Exception
	 */
	@Test(enabled = false)
	public void testPropertyChangeSyncing() throws Exception {

		Model left = stubModel();
		Model right = left.copy(true);

		// sanity check: verify we are equal before we bind
		verifyInSync(left, right);

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
	@Test
	public void testIndexedPropertyRemoval() throws Exception {
		Model left = stubModel();
		Model right = left.copy(true);

		// sanity check: verify we are equal before we bind
		verifyInSync(left, right);

		// create the binding
		// Binding binding = new Binding(left, right, "addresses");
		Binding binding = new Binding();
		// binding.getChildren().add(new Binding(left, right, "addresses[0]"));
		binding.getChildren().add(new Binding(left, right, "id"));
		binding.getChildren().add(new Binding(left, right, "addresses[0].address.firstName"));
		binding.bind();

		// mutate the left
		left.setProperty("addresses[0].address", null);
		binding.setRight();

		// verify the the property was removed on the right
		verifyInSync(left, right);

		// mutate the right
		right.setProperty("addresses[1].address", null);

		// verify the the property was removed on the right
		verifyInSync(left, right);

		binding.unbind();
	}

	/**
	 * Verifies two given {@link Model} instances are memory address distinct but
	 * have equal model property values.
	 * @param left
	 * @param right
	 * @throws Exception
	 */
	private void verifyInSync(Model left, Model right) throws Exception {
		// verify the properties changes are syncd
		try {
			TestUtils.validateCopy(left, right, true);
		}
		catch(Exception e) {
			Assert.fail("Model " + left.toString() + " is out of sync with: " + right.toString(), e);
		}
	}

	/**
	 * @return A stubbed model for testing.
	 */
	protected Model stubModel() {

		Model parentAccount = new Model(EntityType.ASP);
		parentAccount.set(new IntPropertyValue(Model.ID_PROPERTY, 1));
		parentAccount.set(new StringPropertyValue(Model.NAME_PROPERTY, "The Asp"));
		parentAccount.set(new DatePropertyValue(Model.DATE_CREATED_PROPERTY, new Date()));
		parentAccount.set(new DatePropertyValue(Model.DATE_MODIFIED_PROPERTY, new Date()));

		Model account = new Model(EntityType.ISP);
		account.set(new IntPropertyValue(Model.ID_PROPERTY, 2));
		account.set(new StringPropertyValue(Model.NAME_PROPERTY, "An ISP"));
		account.set(new DatePropertyValue(Model.DATE_CREATED_PROPERTY, new Date()));
		account.set(new DatePropertyValue(Model.DATE_MODIFIED_PROPERTY, new Date()));
		account.set(new RelatedOneProperty(EntityType.ACCOUNT, "parent", true, parentAccount));

		Model address1 = new Model(EntityType.ADDRESS);
		address1.set(new IntPropertyValue(Model.ID_PROPERTY, 1));
		address1.set(new StringPropertyValue("firstName", "jon"));
		address1.set(new StringPropertyValue("lastName", "doe"));

		Model address2 = new Model(EntityType.ADDRESS);
		address2.set(new IntPropertyValue(Model.ID_PROPERTY, 2));
		address2.set(new StringPropertyValue("firstName", "mary"));
		address2.set(new StringPropertyValue("lastName", "jane"));

		Model aa1 = new Model(EntityType.ACCOUNT_ADDRESS);
		aa1.set(new IntPropertyValue(Model.ID_PROPERTY, 1));
		aa1.set(new EnumPropertyValue("type", AddressType.HOME));
		aa1.set(new RelatedOneProperty(EntityType.ACCOUNT, "account", true, account));
		aa1.set(new RelatedOneProperty(EntityType.ADDRESS, "address", false, address1));

		Model aa2 = new Model(EntityType.ACCOUNT_ADDRESS);
		aa2.set(new IntPropertyValue(Model.ID_PROPERTY, 2));
		aa2.set(new EnumPropertyValue("type", AddressType.CONTACT));
		aa2.set(new RelatedOneProperty(EntityType.ACCOUNT, "account", true, account));
		aa2.set(new RelatedOneProperty(EntityType.ADDRESS, "address", false, address2));

		List<Model> addresses = new ArrayList<Model>();
		addresses.add(aa1);
		addresses.add(aa2);
		account.set(new RelatedManyProperty(EntityType.ACCOUNT_ADDRESS, "addresses", false, addresses));

		return account;
	}
}
