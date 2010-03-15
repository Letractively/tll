/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2009
 */
package com.tll;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;
import com.tll.client.bind.ModelBindingGWTTest;

/**
 * DatabindGWTTestSuite
 * @author jpk
 */
public class DatabindGWTTestSuite extends TestSuite {

	public static Test suite() {
		final TestSuite gwtTestSuite = new GWTTestSuite();
		gwtTestSuite.addTestSuite(ModelBindingGWTTest.class);
		return gwtTestSuite;
	}
}
