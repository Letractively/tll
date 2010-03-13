/**
 * The Logic Lab
 * @author jpk
 * Feb 22, 2009
 */
package com.tll;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;
import com.tll.client.bind.FieldBindingGWTTest;
import com.tll.client.bind.ModelBindingGWTTest;
import com.tll.client.ui.field.FieldGWTTest;
import com.tll.client.ui.field.FieldGroupGWTTest;


/**
 * FieldGWTTestSuite
 * @author jpk
 */
public class FieldGWTTestSuite extends TestSuite {

	public static Test suite() {
		final TestSuite gwtTestSuite = new GWTTestSuite();
    gwtTestSuite.addTestSuite(FieldGWTTest.class);
		gwtTestSuite.addTestSuite(FieldGroupGWTTest.class);
		gwtTestSuite.addTestSuite(ModelBindingGWTTest.class);
		gwtTestSuite.addTestSuite(FieldBindingGWTTest.class);
    return gwtTestSuite;
	}
}
