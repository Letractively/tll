/*
 * The Logic Lab
 */
package com.tll.service.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.DbTestSupport;
import com.tll.dao.IEntityDao;
import com.tll.model.Account;
import com.tll.model.Authority;
import com.tll.model.AuthorityRoles;
import com.tll.model.User;
import com.tll.model.key.PrimaryKey;
import com.tll.service.entity.user.IUserService;
import com.tll.service.entity.user.UserService;

/**
 * UserServiceTest
 * @author jpk
 */
@Test(groups = "service.entity")
public class UserServiceTest extends AccountRelatedServiceTest {

	private void stubAuthorities() {
		final IEntityDao dao = getEntityDao();
		getDbSupport().startNewTransaction();
		getDbSupport().setComplete();
		try {
			for(final AuthorityRoles role : AuthorityRoles.values()) {
				final Authority a = getMockEntityFactory().getEntityCopy(Authority.class, false);
				a.setAuthority(role.toString());
				dao.persist(a);
			}
		}
		finally {
			getDbSupport().endTransaction();
		}
	}

	/**
	 * Tests {@link UserService#encodePassword(String, Object)}.
	 */
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

	@Test
	public void testUserCreate() throws Exception {
		Account account = null;
		try {
			// stub the related account
			account = stubValidAccount(true);

			// stub the authorities
			stubAuthorities();

			// the service test (should be transactional)
			final IUserService userService = getEntityServiceFactory().instance(IUserService.class);
			final User user = userService.create(account, "name@domain.com", "password");
			Assert.assertNotNull(user);

			getDbSupport().startNewTransaction();
			final User dbUser = DbTestSupport.getEntityFromDb(getEntityDao(), new PrimaryKey<User>(user));
			getDbSupport().endTransaction();
			Assert.assertEquals(dbUser, user);
		}
		catch(final Throwable t) {
			Assert.fail(t.getMessage(), t);
		}
	}
}
