/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.client.model;

import org.testng.annotations.Test;

import com.tll.dao.impl.IAccountDao;
import com.tll.model.impl.Asp;
import com.tll.model.key.KeyFactory;
import com.tll.server.rpc.MarshalOptions;

/**
 * PropertyPathTest
 * @author jpk
 */
@Test(groups = "client-model")
public class PropertyPathTest extends AbstractModelTest {

	private Model getTestModel() {
		final IAccountDao dao = getDaoFactory().instance(IAccountDao.class);
		final Asp asp = (Asp) dao.load(KeyFactory.getNameKey(Asp.class, Asp.ASP_NAME));
		assert asp != null;
		return getMarshaler().marshalEntity(asp, MarshalOptions.UNCONSTRAINED_MARSHALING);
	}

	/**
	 * Test the property path resolution of EXISTING property valuess
	 * @throws Exception Upon any encountered failure
	 */
	@Test
	public void testResolution() throws Exception {
		IPropertyBinding prop;
		PropertyPath path = new PropertyPath();
		Model model;

		model = getTestModel();

		path.parse("name");
		prop = model.getBinding(path);
		assert prop != null : "Unable to resolve property path: " + path;

		path.parse("currency.iso4217");
		prop = model.getBinding(path);
		assert prop != null : "Unable to resolve property path: " + path;

		// test non-existant model one before end of path
		path.parse("parent.name");
		prop = model.getBinding(path);
		assert prop == null : "Resolved should have been null path node";

		path.parse("addresses");
		prop = model.getBinding(path);
		assert prop != null : "Unable to resolve property path: " + path;
		assert prop instanceof RelatedManyProperty : "Related many property value is the wrong type";

		path.parse("addresses[0]");
		prop = model.getBinding(path);
		assert prop != null : "Unable to resolve property path: " + path;
		assert prop instanceof ModelRefProperty : "Expected ModelRefProperty impl at property path: " + path;

		path.parse("addresses[20]");
		prop = model.getBinding(path);
		assert prop == null : "Got a non-null prop value for a non-existant indexed property!";

		path.parse("addresses[0].address.firstName");
		prop = model.getBinding(path);
		assert prop != null : "Unable to resolve property path: " + path;

		// test paymentInfo resolution
		path.parse("paymentInfo.paymentData");
		prop = model.getBinding(path);
		assert prop == null : "Able to resolve paymentInfo.paymentData path!";

		path.parse("paymentInfo.paymentData.bankName");
		prop = model.getBinding(path);
		assert prop != null && prop instanceof StringPropertyValue : "Unable to resolve property path: " + path;

		// malformed
		try {
			path.parse("paymentInfo[2].name");
			prop = model.getBinding(path);
			assert prop != null && prop instanceof StringPropertyValue : "Unable to resolve property path: " + path;
		}
		catch(final IllegalArgumentException e) {
			// expected
		}
	}
}
