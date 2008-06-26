/**
 * 
 */
package com.tll.dao.impl.hibernate;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.IComparatorTranslator;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.hibernate.TimeStampEntityDao;
import com.tll.dao.impl.IUserDao;
import com.tll.model.ChangeUserCredentialsFailedException;
import com.tll.model.IEntity;
import com.tll.model.impl.User;
import com.tll.model.key.NameKey;

/**
 * UserDao
 * @author jpk
 */
public class UserDao extends TimeStampEntityDao<User> implements IUserDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public UserDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}

	public void setCredentials(final Integer userId, final String newUsername, final String newEncPassword)
			throws ChangeUserCredentialsFailedException {
		try {
			getEntityManager().createNamedQuery("user.setCredentials").setParameter(IEntity.PK_FIELDNAME, userId.intValue())
					.setParameter("username", newUsername).setParameter("password", newEncPassword).executeUpdate();
		}
		catch(final HibernateException he) {
			throw new ChangeUserCredentialsFailedException(he.getMessage());
		}
	}

	@Override
	public User load(NameKey<User> nameKey) {
		return (User) loadByName(nameKey);
	}
}
