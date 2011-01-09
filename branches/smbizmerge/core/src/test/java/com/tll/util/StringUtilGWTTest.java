/**
 * The Logic Lab
 * @author jpk
 * Feb 13, 2009
 */
package com.tll.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author jpk
 */
@Test(groups = "util")
public class StringUtilGWTTest {

	public void testAbbr() {
		Assert.assertEquals("abbr...", StringUtil.abbr("abbreviated", 7));
	}

	public void testIsEmpty() {
		Assert.assertTrue(StringUtil.isEmpty(""));
		Assert.assertTrue(StringUtil.isEmpty(" "));
		Assert.assertTrue(StringUtil.isEmpty("  "));
		Assert.assertFalse(StringUtil.isEmpty("a"));
		Assert.assertFalse(StringUtil.isEmpty(" a"));
		Assert.assertFalse(StringUtil.isEmpty("a "));
		Assert.assertTrue(StringUtil.isEmpty(null));
	}

	public void testCamelCaseToEnumStyle() {
		Assert.assertEquals("CAMEL_CASE", StringUtil.camelCaseToEnumStyle("camelCase"));
	}

	public void testEnumStyleToCamelCase() {
		Assert.assertEquals("camelCase", StringUtil.enumStyleToCamelCase("CAMEL_CASE", false));
		Assert.assertEquals("CamelCase", StringUtil.enumStyleToCamelCase("CAMEL_CASE", true));
	}

	public void testOgnlToPresentation() {
		Assert.assertEquals("The One Thing", StringUtil.ognlToPresentation("the.one.thing"));
	}

	public void testReplace() {
		Assert.assertEquals("The replaced thing", StringUtil.replace("The one thing", "one", "replaced"));
		Assert.assertEquals("The one thing", StringUtil.replace("The one thing", "nomatch", ""));
	}

	public void testReplaceVariables() {
		Assert.assertEquals("The one thing", StringUtil.replaceVariables("The %1 thing", "one"));
		Assert.assertEquals("The one thing", StringUtil.replaceVariables("The %1 %2", new Object[] {
			"one", "thing" }));
		Assert.assertEquals("Theonething", StringUtil.replaceVariables("The%1%2", new Object[] {
			"one", "thing" }));
	}
}
