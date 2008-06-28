package com.tll.service.acl;

import java.util.List;
import java.util.Map;

import org.acegisecurity.acl.AclProvider;

import com.tll.model.impl.User;

/**
 * Provides and manages ACL permissions. Services wishing to alter ACLs in the
 * system, should call on this interface.
 * @author jpk
 */
public interface IBasicAclProviderManager<T> extends AclProvider {

	/**
	 * @return The class that is used to generate
	 *         {@link org.acegisecurity.acl.basic.NamedEntityObjectIdentity}
	 *         instances.
	 */
	Class<T> getSupportedClass();

	/**
	 * Retrieves permission masks for a given acl object identity class and id for
	 * the given list of users. The mask corresponds to Acegi's ACL mask
	 * implementation.
	 * @param entityId The id of the supporting entity type which comprises the
	 *        acl object identity for which the given user permission masks are
	 *        retrieved.
	 * @param users List of users for which to retrieve permissions.
	 * @return serviceMap of integer permission masks keyed by {@link User}.
	 * @see org.acegisecurity.acl.basic.BasicAclEntry for mask info.
	 */
	Map<User, Integer> getUserPermissions(Integer entityId, List<User> users);

	/**
	 * Sets the given user permission masks for a given acl object identity class
	 * and id.
	 * @param entityId The id of the supporting entity type which comprises the
	 *        acl object identity for which the given user permission masks are
	 *        updated.
	 * @param userPermissionsMap Map of permission masks of desired users for the
	 *        given category id.
	 * @see org.acegisecurity.acl.basic.BasicAclEntry for mask info.
	 */
	void setUserPermissions(Integer entityId, Map<User, Integer> userPermissionsMap);

	/**
	 * Deletes all user permissions relating to the entity specified by the given
	 * id.
	 * @param entityId The id of the entity.
	 */
	void deleteAllUserPermissionsForEntity(Integer entityId);

	/**
	 * Deletes all ACL permissions that exist for the given user.
	 * @param user The user for which all related ACLs are DELETED.
	 */
	void deleteAllUserPermissions(User user);

	/**
	 * Sets user permissions for all administrative users for the entity specified
	 * by the given id.
	 * @param entityId id of the entity.
	 */
	void applyAdministrativePermissions(Integer entityId);
}
