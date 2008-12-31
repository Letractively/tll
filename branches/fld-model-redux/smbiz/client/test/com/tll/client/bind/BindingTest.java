/**
 * The Logic Lab
 * @author jpk
 * Dec 28, 2008
 */
package com.tll.client.bind;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

import com.tll.TestUtils;
import com.tll.client.model.DatePropertyValue;
import com.tll.client.model.EnumPropertyValue;
import com.tll.client.model.IntPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.model.RelatedOneProperty;
import com.tll.client.model.StringPropertyValue;
import com.tll.model.EntityType;
import com.tll.model.impl.AddressType;

/**
 * BindingTest - Test that verifies client side data binding.
 * @author jpk
 */
@Test(groups = "client-bind")
public class BindingTest {

	@Test
	public void test() throws Exception {

		Model left = stubModel();
		Model right = left.copy(true);

		// sanity check: verify we are equal before we bind
		TestUtils.validateCopy(left, right);

		// right.clearPropertyValues(true);

		Binding binding = new Binding();
		List<Binding> children = binding.getChildren();
		children.add(new Binding(left, right, Model.ID_PROPERTY));
		children.add(new Binding(left, right, Model.NAME_PROPERTY));
		children.add(new Binding(left, right, Model.DATE_CREATED_PROPERTY));
		children.add(new Binding(left, right, Model.DATE_MODIFIED_PROPERTY));
		children.add(new Binding(left, right, "parent.name"));
		children.add(new Binding(left, right, "addresses[0].address.firstName"));

		binding.bind();

		// validate setting right side..
		binding.setRight();
		TestUtils.validateCopy(left, right);

		// validate setting left side..
		binding.setLeft();
		TestUtils.validateCopy(left, right);

		binding.unbind();
	}

	Model stubModel() {

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
