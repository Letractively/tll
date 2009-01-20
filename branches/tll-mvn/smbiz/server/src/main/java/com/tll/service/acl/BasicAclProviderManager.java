package com.tll.service.acl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acegisecurity.Authentication;
import org.acegisecurity.acl.AclEntry;
import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.BasicAclEntry;
import org.acegisecurity.acl.basic.BasicAclProvider;
import org.acegisecurity.acl.basic.NamedEntityObjectIdentity;
import org.acegisecurity.acl.basic.SimpleAclEntry;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.tll.SystemError;
import com.tll.dao.impl.IAclDao;
import com.tll.model.Authority;
import com.tll.model.User;

/**
 * Abstract class implementing the {@link IBasicAclProviderManager} interface.
 * Sub-classes need to specify the supported class.
 * @author jpk
 * @param <T>
 */
public abstract class BasicAclProviderManager<T> extends BasicAclProvider implements IBasicAclProviderManager<T> {

	private static final Log log = LogFactory.getLog(BasicAclProviderManager.class);

	/**
	 * @return The underlying ACL dao.
	 * @see IAclDao
	 */
	protected IAclDao getAclDao() {
		return (IAclDao) getBasicAclDao();
	}

	@Override
	protected AclObjectIdentity obtainIdentity(Object domainInstance) {
		if(domainInstance instanceof Integer) {
			return new NamedEntityObjectIdentity(getSupportedClass().getName(), ((Integer) domainInstance).toString());
		}
		return super.obtainIdentity(domainInstance);
	}

	/**
	 * Generates a new concrete {@link Authentication} instance given a
	 * {@link User} instance.
	 * @param user The user.
	 * @return newly generated authentication.
	 */
	protected Authentication authFromUser(User user) {
		return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

	/**
	 * Retrieves the permission mask associated with the given acl object id and
	 * user.
	 * @param aclObjectId The acl object id.
	 * @param user The user for which the permission mask is retrieved.
	 * @return the permission mask <code>Integer</code>.
	 */
	protected Integer getMask(Integer aclObjectId, User user) {
		try {
			AclEntry[] acls = getAcls(aclObjectId, authFromUser(user));
			if(acls == null || acls.length < 1) {
				return Permission.NOTHING.intValue();
			}
			// NOTE: multiple acls means the user is expressed
			// in the ACL table's recipient column under both the username
			// and one or more of the user's authority strings (roles).
			// So we bit-wise OR them together.
			int mask = 0;
			for(AclEntry acl : acls) {
				mask = mask | ((BasicAclEntry) acl).getMask();
			}
			return mask;
		}
		catch(DataAccessException dae) {
			throw new SystemError("Unable to retrieve user permissions: " + dae.getMessage(), dae);
		}
	}

	public Map<User, Integer> getUserPermissions(Integer entityId, List<User> users) {
		Map<User, Integer> map = new HashMap<User, Integer>();
		for(User user : users) {
			map.put(user, getMask(entityId, user));
		}
		return map;
	}

	public void setUserPermissions(Integer entityId, Map<User, Integer> userPermissionsMap) {
		AclObjectIdentity aclObjectIdentity = obtainIdentity(entityId);
		if(log.isDebugEnabled()) log.debug("Setting user permissions for: " + aclObjectIdentity.toString() + "...");
		try {
			for(User user : userPermissionsMap.keySet()) {

				// the recipient is the username
				String recipient = user.getUsername();

				// delete acls for this user first
				getAclDao().delete(aclObjectIdentity, recipient);

				// [re-]create acls for the user
				Integer mask = userPermissionsMap.get(user);
				BasicAclEntry aclEntry = new SimpleAclEntry(recipient, aclObjectIdentity, null, mask);
				getAclDao().create(aclEntry);
			}
		}
		catch(DataAccessException dae) {
			throw new SystemError("Unable to update user permissions: " + dae.getMessage(), dae);
		}

	}

	public void deleteAllUserPermissionsForEntity(Integer entityId) {
		AclObjectIdentity aclObjectIdentity = obtainIdentity(entityId);
		if(log.isDebugEnabled()) log.debug("Deleting all user permissions for: " + aclObjectIdentity.toString() + "...");
		getAclDao().delete(aclObjectIdentity);
	}

	public void deleteAllUserPermissions(User user) {
		String recipient = user.getUsername();
		if(log.isDebugEnabled()) log.debug("Deleting all user permissions for: " + recipient + "...");
		getAclDao().delete(recipient);
	}

	public void applyAdministrativePermissions(Integer entityId) {
		AclObjectIdentity aclObjectIdentity = obtainIdentity(entityId);
		if(log.isDebugEnabled())
			log.debug("Applying administrative permissions for: " + aclObjectIdentity.toString() + "...");
		Integer mask = Permission.ADMINISTRATION.intValue();
		Object recipient = Authority.ROLE_ADMINISTRATOR;
		BasicAclEntry aclEntry = new SimpleAclEntry(recipient, aclObjectIdentity, null, mask);
		getAclDao().create(aclEntry);
	}

}
