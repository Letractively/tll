/**
 * The Logic Lab
 * @author jpk
 * Feb 13, 2009
 */
package com.tll.client.util;

import java.util.Date;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * FormatGWTTest - Tests {@link Fmt}.
 * @author jpk
 */
public class FormatGWTTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.tll.Core";
	}

	public void testCurrencyFormatting() {
		final String s = Fmt.currency(2.22f);
		Assert.assertTrue(s != null && s.length() > 3 && s.startsWith("US$"));
	}

	@SuppressWarnings("deprecation")
	public void testDateFormatting() {
		final Date now = new Date(2009, 0, 1);
		final String s = Fmt.format(now, GlobalFormat.DATE);
		Assert.assertEquals("1/1/09", s);
	}

	public void testBooleanFormatting() {
		Assert.assertEquals("True", Fmt.format(Boolean.TRUE, GlobalFormat.BOOL_TRUEFALSE));
		Assert.assertEquals("False", Fmt.format(Boolean.FALSE, GlobalFormat.BOOL_TRUEFALSE));
		Assert.assertEquals("Yes", Fmt.format(Boolean.TRUE, GlobalFormat.BOOL_YESNO));
		Assert.assertEquals("No", Fmt.format(Boolean.FALSE, GlobalFormat.BOOL_YESNO));
	}
}
