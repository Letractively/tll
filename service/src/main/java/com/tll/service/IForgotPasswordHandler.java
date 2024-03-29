/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.service;

import com.tll.dao.EntityNotFoundException;
import com.tll.model.IUserRef;

/**
 * IForgotPasswordService - Contract for resetting and providing a user's
 * forgotton password.
 * @author jpk
 */
public interface IForgotPasswordHandler {

	/**
	 * Get a user by the username.
	 * @param username
	 * @return The user
	 * @throws EntityNotFoundException When the user can't be found.
	 */
	IUserRef getUserRef(String username) throws EntityNotFoundException;

	/**
	 * Resets a user's password given the identifying username.
	 * @param userPk The user id
	 * @return the new reset password
	 * @throws ChangeUserCredentialsFailedException When the operation fails
	 */
	String resetPassword(Long userPk) throws ChangeUserCredentialsFailedException;
}
