package com.tll.dao.impl.jdbc.acl;

import java.sql.Types;

import javax.sql.DataSource;

import org.acegisecurity.acl.basic.jdbc.JdbcExtendedDaoImpl;
import org.springframework.context.ApplicationContextException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import com.tll.dao.IAclDao;

/**
 * The JDBC ACL DAO implementation.
 * @author jpk
 */
// TODO make members final and do constructor injection!!
public class AclDao extends JdbcExtendedDaoImpl implements IAclDao {

	public static final String DEF_ACL_PERMISSION_DELETE_RECIPIENT_STATEMENT =
			"DELETE FROM acl_permission WHERE recipient = ?";

	private AclPermissionDeleteRecipient aclPermissionDeleteRecipient;
	protected String aclPermissionDeleteRecipientStatement;

	/**
	 * 
	 */
	public AclDao() {
		super();
		aclPermissionDeleteRecipientStatement = DEF_ACL_PERMISSION_DELETE_RECIPIENT_STATEMENT;
	}

	public void delete(Object recipient) throws DataAccessException {
		// TODO figure out how to remove from cache by recipient
		// basicAclEntryCache.removeEntriesFromCache(aclObjectIdentity);

		// Delete acl_permission
		aclPermissionDeleteRecipient.delete(recipient.toString());
	}

	public AclPermissionDeleteRecipient getAclPermissionDeleteRecipient() {
		return aclPermissionDeleteRecipient;
	}

	public String getAclPermissionDeleteRecipientStatement() {
		return aclPermissionDeleteRecipientStatement;
	}

	public void setAclPermissionDeleteRecipient(AclPermissionDeleteRecipient aclPermissionDeleteRecipient) {
		this.aclPermissionDeleteRecipient = aclPermissionDeleteRecipient;
	}

	public void setAclPermissionDeleteRecipientStatement(String aclPermissionDeleteRecipientStatement) {
		this.aclPermissionDeleteRecipientStatement = aclPermissionDeleteRecipientStatement;
	}

	@Override
	protected void initDao() throws ApplicationContextException {
		super.initDao();
		aclPermissionDeleteRecipient = new AclPermissionDeleteRecipient(getDataSource());
	}

	protected class AclPermissionDeleteRecipient extends SqlUpdate {

		protected AclPermissionDeleteRecipient(DataSource ds) {
			super(ds, aclPermissionDeleteRecipientStatement);
			declareParameter(new SqlParameter(Types.VARCHAR));
			compile();
		}

		protected void delete(String recipient) throws DataAccessException {
			super.update(new Object[] { recipient });
		}
	}

}
