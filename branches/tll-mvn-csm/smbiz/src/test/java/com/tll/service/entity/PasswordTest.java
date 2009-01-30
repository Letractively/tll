/**
 * The Logic Lab
 * @author jpk
 * Apr 27, 2008
 */
package com.tll.service.entity;

import org.testng.annotations.Test;

import com.tll.service.entity.user.UserService;

/**
 * PasswordTest
 * @author jpk
 */
@Test(groups = "entity.service")
public class PasswordTest {

	@Test
	public void testEncodePassword() {
		final String[][] args = new String[][] {
			{
				"superuser", "abc123" }, {
				"jopaki@gmail.com", "bleamin" } };
		for(final String[] arg : args) {
			System.out.println(arg[0] + ": " + UserService.encodePassword(arg[1], arg[0]));
		}
	}

}
