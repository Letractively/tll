/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.client.model;

import org.testng.annotations.Test;

import com.tll.dao.impl.IAccountDao;
import com.tll.model.impl.Account;
import com.tll.model.impl.Asp;
import com.tll.model.key.NameKey;
import com.tll.server.rpc.MarshalOptions;

/**
 * PropertyPathModelResolutionTest
 * @author jpk
 */
@Test(groups = "client-model")
public class PropertyPathModelResolutionTest extends AbstractModelTest {

	private Model getTestModel() {
		final IAccountDao dao = getDaoFactory().instance(IAccountDao.class);
		final Asp asp = (Asp) dao.load(new NameKey<Account>(Account.class, Asp.ASP_NAME));
		assert asp != null;
		return getMarshaler().marshalEntity(asp, MarshalOptions.UNCONSTRAINED_MARSHALING);
	}

	/**
	 * Test the property path resolution of EXISTING property values
	 * @throws Exception Upon any encountered failure
	 */
	@Test
	public void testResolution() throws Exception {
		IModelProperty prop;
		String path;
		Model model;

		model = getTestModel();

		path = "name";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;

		path = "currency.iso4217";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;

		// test non-existant model one before end of path
		path = "parent.name";
		prop = model.getModelProperty(path);
		assert prop == null : "Resolved should have been null path node";

		path = "addresses";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;
		assert prop instanceof RelatedManyProperty : "Related many property value is the wrong type";

		path = "addresses[0]";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;
		assert prop instanceof ModelRefProperty : "Expected ModelRefProperty impl at property path: " + path;

		path = "addresses[20]";
		prop = model.getModelProperty(path);
		assert prop == null : "Got a non-null prop value for a non-existant indexed property!";

		path = "addresses[0].address.firstName";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;

		// test paymentInfo resolution
		path = "paymentInfo.paymentData_bankName";
		prop = model.getModelProperty(path);
		assert prop != null && prop instanceof StringPropertyValue : "Unable to resolve property path: " + path;

		// malformed
		try {
			path = "paymentInfo[2].name";
			prop = model.getModelProperty(path);
			assert prop != null && prop instanceof StringPropertyValue : "Unable to resolve property path: " + path;
		}
		catch(final IllegalArgumentException e) {
			// expected
		}
	}
}
