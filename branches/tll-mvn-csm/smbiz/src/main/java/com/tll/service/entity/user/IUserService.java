/**
 * The Logic Lab
 */
package com.tll.service.entity.user;

import javax.persistence.EntityExistsException;

import org.acegisecurity.userdetails.UserDetailsService;
import org.hibernate.validator.InvalidStateException;

import com.tll.model.Account;
import com.tll.model.ChangeUserCredentialsFailedException;
import com.tll.model.User;
import com.tll.service.entity.INamedEntityService;

/**
 * IUserService
 * @author jpk
 */
public interface IUserService extends INamedEntityService<User>, UserDetailsService {

	/**
	 * Create a user given an account, username and password.
	 * @param account
	 * @param emailAddress
	 * @param password
	 * @return the created user
	 * @throws InvalidStateException
	 * @throws EntityExistsException
	 */
	public User create(Account account, String emailAddress, String password) throws InvalidStateException,
			EntityExistsException;

	/**
	 * @param userId
	 * @param newUsername
	 * @param newRawPassword
	 * @throws ChangeUserCredentialsFailedException
	 */
	public void setCredentialsById(Integer userId, String newUsername, String newRawPassword)
			throws ChangeUserCredentialsFailedException;

	/**
	 * @param username
	 * @param newUsername
	 * @param newRawPassword
	 * @throws ChangeUserCredentialsFailedException
	 */
	void setCredentialsByUsername(String username, String newUsername, String newRawPassword)
			throws ChangeUserCredentialsFailedException;

	/**
	 * @param userId
	 * @return the new reset password
	 * @throws ChangeUserCredentialsFailedException
	 */
	String resetPassword(Integer userId) throws ChangeUserCredentialsFailedException;

	/**
	 * Loads a {@link User} by email address auto-deproxying the related account.
	 * @param emailAddress
	 * @return the matching user
	 */
	User findByUsername(String emailAddress);
}
