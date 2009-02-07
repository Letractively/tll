package com.tll.util;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "util")
public class CommonUtilTest {

	@Test
	public void testGetClasses() throws Exception {
		String packageName = getClass().getPackage().getName();
		Class<?>[] classes = CommonUtil.getClasses(packageName);
		Assert.assertNotNull(classes);
		Assert.assertTrue(classes.length > 0);
	}

}
