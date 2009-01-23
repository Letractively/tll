/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.client.model;

import org.testng.annotations.Test;

import com.tll.model.Asp;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.marshal.Marshaler;

/**
 * PropertyPathModelResolutionTest
 * @author jpk
 */
@Test(groups = "client-model")
public class PropertyPathModelResolutionTest extends AbstractModelTest {

	private Model getTestModel() {
		final Asp asp = new Asp();
		Marshaler m = injector.getInstance(Marshaler.class);
		// TODO fill in asp
		return m.marshalEntity(asp, MarshalOptions.UNCONSTRAINED_MARSHALING);
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
		try {
			prop = model.getModelProperty(path);
		}
		catch(NullNodeInPropPathException e) {
			// expected
		}

		path = "addresses";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;
		assert prop instanceof RelatedManyProperty : "Related many property value is the wrong type";

		path = "addresses[0]";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;
		assert prop instanceof ModelRefProperty : "Expected ModelRefProperty impl at property path: " + path;

		path = "addresses[20]";
		try {
			prop = model.getModelProperty(path);
		}
		catch(IndexOutOfRangeInPropPathException e) {
			// expected
		}

		path = "addresses[0].address.firstName";
		prop = model.getModelProperty(path);
		assert prop != null : "Unable to resolve property path: " + path;

		// test paymentInfo resolution
		path = "paymentInfo.paymentData_bankName";
		prop = model.getModelProperty(path);
		assert prop != null && prop instanceof StringPropertyValue : "Unable to resolve property path: " + path;

		// node mismatch
		try {
			path = "paymentInfo[2].name";
			prop = model.getModelProperty(path);
		}
		catch(final PropPathNodeMismatchException e) {
			// expected
		}

		// malformed
		try {
			path = "..??-";
			prop = model.getModelProperty(path);
		}
		catch(final PropertyPathException e) {
			// expected
		}
	}
}
