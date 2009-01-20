package com.tll.dao.impl;

import com.tll.dao.INamedEntityDao;
import com.tll.dao.ITimeStampEntityDao;
import com.tll.model.ChangeUserCredentialsFailedException;
import com.tll.model.User;

/**
 * DAO interface for the persistent methods for {@link User} entities
 * @author jpk
 */
public interface IUserDao extends ITimeStampEntityDao<User>, INamedEntityDao<User> {

    /**
     * The way to alter a User's username and password
     * @param userId
     * @param newUsername
     * @param newEncPassword
     * @throws ChangeUserCredentialsFailedException
     */
    void setCredentials(Integer userId, String newUsername, String newEncPassword)
    throws ChangeUserCredentialsFailedException;
}
