package com.tll.dao.impl;

import com.tll.dao.INamedEntityDao;
import com.tll.dao.ITimeStampEntityDao;
import com.tll.model.Account;

/**
 * @author jpk
 */
public interface IAccountDao extends ITimeStampEntityDao<Account>, INamedEntityDao<Account> {
}