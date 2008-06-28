package com.tll.dao.impl;

import com.tll.dao.INamedEntityDao;
import com.tll.dao.ITimeStampEntityDao;
import com.tll.model.impl.AccountAddress;

/**
 * @author jpk
 */
public interface IAccountAddressDao extends ITimeStampEntityDao<AccountAddress>, INamedEntityDao<AccountAddress> {
}