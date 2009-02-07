/*
 * The Logic Lab 
 */
package com.tll.service.entity;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.dao.IEntityDao;
import com.tll.model.Account;
import com.tll.model.Authority;
import com.tll.model.AuthorityRoles;
import com.tll.model.User;
import com.tll.model.key.PrimaryKey;
import com.tll.service.entity.user.IUserService;
import com.tll.service.entity.user.UserService;

/**
 * AccountRelatedServiceTest
 * @author jpk
 */
@Test(groups = "service.entity")
public class UserServiceTest extends AccountRelatedServiceTest {

	/**
	 * Constructor
	 */
	public UserServiceTest() {
		super();
	}

	private void stubAuthorities() {
		final IEntityDao dao = getEntityDao();
		startNewTransaction();
		setComplete();
		try {
			for(AuthorityRoles role : AuthorityRoles.values()) {
				Authority a = getMockEntityFactory().getEntityCopy(Authority.class, false);
				a.setAuthority(role.toString());
				dao.persist(a);
			}
		}
		finally {
			endTransaction();
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

			startNewTransaction();
			final User dbUser = getEntityFromDb(getEntityDao(), new PrimaryKey<User>(user));
			endTransaction();
			Assert.assertEquals(dbUser, user);
		}
		catch(final Throwable t) {
			Assert.fail(t.getMessage(), t);
		}
	}
}
