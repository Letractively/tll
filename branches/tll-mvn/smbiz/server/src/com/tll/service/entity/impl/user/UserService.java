package com.tll.service.entity.impl.user;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.acegisecurity.Authentication;
import org.acegisecurity.acl.AclProvider;
import org.acegisecurity.acl.AclProviderManager;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.impl.IAuthorityDao;
import com.tll.dao.impl.IUserDao;
import com.tll.model.ChangeUserCredentialsFailedException;
import com.tll.model.EntityAssembler;
import com.tll.model.EntityCache;
import com.tll.model.EntityType;
import com.tll.model.impl.Account;
import com.tll.model.impl.Authority;
import com.tll.model.impl.User;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.service.acl.IBasicAclProviderManager;
import com.tll.service.entity.StatefulEntityService;

/**
 * UserService - {@link IUserService} impl
 * @author jpk
 */
@Transactional
public class UserService extends StatefulEntityService<User, IUserDao> implements IUserService {

	private static PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

	/**
	 * @param password
	 * @param salt
	 * @return the encoded password
	 * @throws IllegalArgumentException
	 */
	public static String encodePassword(String password, Object salt) throws IllegalArgumentException {
		if(StringUtils.isEmpty(password)) throw new IllegalArgumentException("Can't encode an empty password");
		return passwordEncoder.encodePassword(password, salt);
	}

	/**
	 * @param rawPasswordToCheck
	 * @param encPassword
	 * @param salt
	 * @return true/false
	 * @throws IllegalArgumentException
	 */
	public static boolean isPasswordValid(String rawPasswordToCheck, String encPassword, Object salt)
			throws IllegalArgumentException {
		if(StringUtils.isEmpty(rawPasswordToCheck)) throw new IllegalArgumentException("Empty raw password specified");
		if(StringUtils.isEmpty(encPassword)) throw new IllegalArgumentException("Empty encoded password specified");
		return passwordEncoder.isPasswordValid(encPassword, rawPasswordToCheck, salt);
	}

	private final IAuthorityDao authorityDao;

	private final AclProviderManager aclProviderManager;

	private final UserCache userCache;

	/**
	 * Constructor
	 * @param dao
	 * @param authorityDao
	 * @param entityAssembler
	 * @param aclProviderManager
	 * @param userCache
	 */
	@Inject
	public UserService(IUserDao dao, IAuthorityDao authorityDao, EntityAssembler entityAssembler,
			AclProviderManager aclProviderManager, UserCache userCache) {
		super(IUserDao.class, dao, entityAssembler);
		this.authorityDao = authorityDao;
		this.aclProviderManager = aclProviderManager;
		this.userCache = userCache;
	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}

	@Transactional
	public User create(Account account, String emailAddress, String password) throws InvalidStateException,
			EntityExistsException {
		final User user = entityAssembler.assembleEntity(EntityType.USER, new EntityCache(account), true);

		String encPassword = null;
		try {
			encPassword = encodePassword(password, emailAddress);
		}
		catch(final IllegalArgumentException iae) {
			// TODO verify this is cool
			final InvalidValue[] ivs =
					new InvalidValue[] { new InvalidValue("Invalid password", User.class, "password", password, null) };
			throw new InvalidStateException(ivs);
		}

		user.setEmailAddress(emailAddress);
		user.setPassword(encPassword);

		// set default expiry date to 1 day from now
		final Calendar clndr = Calendar.getInstance();
		clndr.add(Calendar.DAY_OF_MONTH, 1);
		final Date expires = clndr.getTime();
		user.setExpires(expires);

		// set the user as un-locked by default
		user.setLocked(false);

		// set the role as user by default
		user.addAuthority(authorityDao.load(new NameKey<Authority>(Authority.class, Authority.ROLE_USER,
				Authority.FIELDNAME_AUTHORITY)));

		persist(user);

		return user;
	}

	/**
	 * {@link UserDetailsService} implementation
	 * @param username
	 * @return the found user
	 * @throws UsernameNotFoundException
	 * @throws DataAccessException
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		try {
			return findByUsername(username);
		}
		catch(final EntityNotFoundException enfe) {
			throw new UsernameNotFoundException("Username '" + username + "' not found");
		}
	}

	public User findByUsername(String emailAddress) throws EntityNotFoundException {
		User user;
		try {
			Criteria<User> criteria = new Criteria<User>(User.class);
			criteria.getPrimaryGroup().addCriterion("emailAddress", emailAddress, false);
			user = dao.findEntity(criteria);
		}
		catch(final InvalidCriteriaException e) {
			throw new SystemError("Unexpected invalid criteria exception occurred");
		}
		if(user == null) {
			throw new EntityNotFoundException("User with username: " + emailAddress + " was not found.");
		}
		final Account ua = user.getAccount();
		ua.getName(); // force de-proxy
		user.setAccount(ua);
		return user;
	}

	public void delete(User user) {
		user.setLocked(true);
		super.persist(user);
		updateSecurityContextIfNecessary(user.getUsername(), null, null, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void purge(User user) throws EntityNotFoundException {

		// remove any ACL entries for this user
		final List<AclProvider> providers = aclProviderManager.getProviders();
		for(final AclProvider provider : providers) {
			if(provider instanceof IBasicAclProviderManager) {
				((IBasicAclProviderManager) provider).deleteAllUserPermissions(user);
			}
		}

		super.purge(user);

		updateSecurityContextIfNecessary(user.getUsername(), null, null, true);
	}

	private User getUserById(Integer userId) throws EntityNotFoundException {
		final User user = dao.load(new PrimaryKey<User>(User.class, userId));
		if(user == null) throw new EntityNotFoundException("User of id '" + userId + "' not found");
		return user;
	}

	@Transactional(rollbackFor = {
		ChangeUserCredentialsFailedException.class,
		RuntimeException.class })
	public void setCredentialsById(Integer userId, String newUsername, String newRawPassword)
			throws ChangeUserCredentialsFailedException {

		try {
			// get the old username
			final String oldUsername = getUserById(userId).getUsername();

			// encode the new password
			final String encNewPassword = encodePassword(newRawPassword, newUsername);

			// set the credentials
			dao.setCredentials(userId, newUsername, encNewPassword);

			updateSecurityContextIfNecessary(oldUsername, newUsername, newRawPassword, false);
		}
		catch(final EntityNotFoundException nfe) {
			throw new ChangeUserCredentialsFailedException("Unable to set user credentials: User of id: " + userId
					+ " not found");
		}
	}

	@Transactional(rollbackFor = {
		ChangeUserCredentialsFailedException.class,
		RuntimeException.class })
	public void setCredentialsByUsername(String username, String newUsername, String newRawPassword)
			throws ChangeUserCredentialsFailedException {

		try {
			// get the user
			Criteria<User> criteria = new Criteria<User>(User.class);
			criteria.getPrimaryGroup().addCriterion("emailAddress", username, false);
			final User user = dao.findEntity(criteria);

			// encode the new password
			final String encNewPassword = encodePassword(newRawPassword, newUsername);

			// set the credentials
			dao.setCredentials(user.getId(), newUsername, encNewPassword);

			updateSecurityContextIfNecessary(user.getUsername(), newUsername, newRawPassword, false);
		}
		catch(final InvalidCriteriaException e) {
			throw new SystemError("Unable to chnage user credentials due to an unexpected invalid criteria exception: "
					+ e.getMessage(), e);
		}
		catch(final EntityNotFoundException nfe) {
			throw new ChangeUserCredentialsFailedException("Unable to set user credentials: Username: '" + username
					+ "' not found");
		}
	}

	@Transactional(rollbackFor = {
		ChangeUserCredentialsFailedException.class,
		RuntimeException.class })
	public String resetPassword(Integer userId) throws ChangeUserCredentialsFailedException {

		try {
			// get the user
			final User user = dao.load(new PrimaryKey<User>(User.class, userId));
			final String username = user.getUsername();

			// encode the new password
			final String random = RandomStringUtils.randomAlphanumeric(8);
			final String encNewPassword = encodePassword(random, username);

			// set the credentials
			dao.setCredentials(userId, username, encNewPassword);

			updateSecurityContextIfNecessary(username, username, random, false);

			return random;
		}
		catch(final EntityNotFoundException nfe) {
			throw new ChangeUserCredentialsFailedException("Unable to re-set user password: User of id: " + userId
					+ " not found");
		}

	}

	private final void updateSecurityContextIfNecessary(final String originalUsername, final String newUsername,
			final String newPassword, final boolean justRemove) {

		final SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext == null) return;

		final Authentication authentication = securityContext.getAuthentication();
		if(authentication == null) return;

		final Object principal = authentication.getPrincipal();
		if(principal instanceof User == false) return;
		final User user = (User) authentication.getPrincipal();

		if(user.getUsername().equals(originalUsername)) {
			if(userCache != null) {
				userCache.removeUserFromCache(originalUsername);
			}
			if(justRemove) {
				SecurityContextHolder.clearContext();
			}
			else {
				final UsernamePasswordAuthenticationToken token =
						new UsernamePasswordAuthenticationToken(newUsername, newPassword);
				token.setDetails(authentication.getDetails());
				SecurityContextHolder.getContext().setAuthentication(token);
			}
			log.info((justRemove ? "Removed" : "Reset") + " security context for user: " + originalUsername);
		}

	}

}
