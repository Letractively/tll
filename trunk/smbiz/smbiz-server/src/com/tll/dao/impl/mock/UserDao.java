/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IUserDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.ChangeUserCredentialsFailedException;
import com.tll.model.impl.User;
import com.tll.model.key.INameKey;
import com.tll.model.key.KeyFactory;

public class UserDao extends EntityDao<User> implements IUserDao, IMockDao<User> {

	@Inject
	public UserDao(Set<User> set) {
		super(User.class, set);
	}

	public User load(INameKey<? extends User> nameKey) {
		return loadByName(nameKey);
	}

	public void setCredentials(Integer userId, String newUsername, String newEncPassword)
			throws ChangeUserCredentialsFailedException {
		User e = load(KeyFactory.getPrimaryKey(User.class, userId));
		if(e == null) {
			throw new ChangeUserCredentialsFailedException("Unable to find user of id: " + userId);
		}
		e.setUsername(newUsername);
		e.setPassword(newEncPassword);
	}
}