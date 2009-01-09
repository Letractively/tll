/**
 * The Logic Lab
 * @author jpk
 * Jan 7, 2009
 */
package com.tll.client.ui.field;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.junit.client.GWTTestCase;
import com.tll.client.ClientTestUtils;

/**
 * FieldGroupTest
 * @author jpk
 */
public class FieldGroupTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.tll.Test";
	}

	private static void fillPropNames(IField<?> f, Collection<String> propNames) {
		if(f instanceof FieldGroup) {
			for(IField<?> c : (FieldGroup) f) {
				fillPropNames(c, propNames);
			}
		}
		else {
			propNames.add(f.getPropertyName());
		}
	}

	private Collection<String> getPropNames(FieldGroup fg) {
		Set<String> set = new HashSet<String>();
		fillPropNames(fg, set);
		return set;
	}

	/**
	 * Tests {@link FieldGroup#getField(String)}.
	 */
	public void testGetField() {
		FieldGroup fg = ClientTestUtils.getRootFieldGroupProvider().getFieldGroup();
		Collection<String> propNames = getPropNames(fg);
		assert propNames != null && propNames.size() > 0;
		for(String prop : propNames) {
			IField<?> f = fg.getField(prop);
			assert f != null;
			assert f.getPropertyName() != null && f.getPropertyName().equals(prop);
		}
	}
}
