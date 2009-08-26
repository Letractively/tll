/**
 * The Logic Lab
 */
package com.tll.service.entity.user;

import javax.persistence.EntityExistsException;
import javax.validation.ValidationException;

import org.springframework.security.userdetails.UserDetailsService;

import com.tll.model.Account;
import com.tll.model.User;
import com.tll.service.ChangeUserCredentialsFailedException;
import com.tll.service.IForgotPasswordHandler;
import com.tll.service.entity.INamedEntityService;

/**
 * IUserService
 * @author jpk
 */
public interface IUserService extends INamedEntityService<User>, UserDetailsService, IForgotPasswordHandler {

	/**
	 * Create a user given an account, username and password.
	 * @param account
	 * @param emailAddress
	 * @param password
	 * @return the created user
	 * @throws ValidationException
	 * @throws EntityExistsException
	 */
	public User create(Account account, String emailAddress, String password) throws ValidationException,
	EntityExistsException;

	/**
	 * @param userId
	 * @param newUsername
	 * @param newRawPassword
	 * @throws ChangeUserCredentialsFailedException
	 */
	public void setCredentialsById(String userId, String newUsername, String newRawPassword)
	throws ChangeUserCredentialsFailedException;

	/**
	 * @param username
	 * @param newUsername
	 * @param newRawPassword
	 * @throws ChangeUserCredentialsFailedException When the operation fails
	 */
	void setCredentialsByUsername(String username, String newUsername, String newRawPassword)
	throws ChangeUserCredentialsFailedException;
}
