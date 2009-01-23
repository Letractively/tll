package com.tll.dao;

import org.acegisecurity.acl.basic.BasicAclExtendedDao;
import org.springframework.dao.DataAccessException;

import com.tll.dao.IDao;

/**
 * ACL DAO definition extending Acegi's {@link BasicAclExtendedDao} to support
 * additional ACL management features.
 * 
 * @author jpk
 */
public interface IAclDao extends BasicAclExtendedDao, IDao {

  /**
   * Deletes all ACLs for the given recipient.
   * 
   * @param recipient
   * @throws DataAccessException
   */
  void delete(Object recipient);
}
