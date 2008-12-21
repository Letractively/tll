/*
 * Created on Jan 1, 2005
 */
package com.tll.model.impl;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.NamedTimeStampEntity;

/**
 * The account user entity
 * @author jpk
 */
@Entity
@Table(name = "user")
public class User extends NamedTimeStampEntity implements UserDetails, IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = -6126885590318834318L;

	public static final int MAXLEN_NAME = 50;
	public static final int MAXLEN_EMAIL_ADDRESS = 128;
	public static final int MAXLEN_PASSWORD = 255;

	public static final String SUPERUSER = "jpk";

	private String emailAddress;

	private transient String password;

	private boolean locked = true;

	private boolean enabled = true;

	private Date expires;

	private Set<Authority> authorities = new LinkedHashSet<Authority>(3);

	private Account account;

	private Address address;

	public Class<? extends IEntity> entityClass() {
		return User.class;
	}

	@Column
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	@Column(name = "email_address", updatable = false, unique = true)
	@NotEmpty
	@Email
	@Length(max = MAXLEN_EMAIL_ADDRESS)
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return Returns the password.
	 */
	@Column(updatable = false)
	@NotEmpty
	@Length(max = MAXLEN_PASSWORD)
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the expires
	 */
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	public Date getExpires() {
		return expires;
	}

	/**
	 * @param expires the expires to set
	 */
	public void setExpires(Date expires) {
		this.expires = expires;
	}

	/**
	 * @return the locked
	 */
	@Column
	@NotNull
	public Boolean getLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return authorities
	 */
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns = @JoinColumn(name = "aid"))
	@Valid
	public Set<Authority> getAuthoritys() {
		return authorities;
	}

	/**
	 * Convenience method checking for presence of a given role for this user.
	 * @return true if this user is "in" the given role, false otherwise.
	 */
	@Transient
	public boolean inRole(String role) {
		Set<Authority> as = getAuthoritys();
		if(as == null) return false;
		for(Authority a : as) {
			if(a.equals(role)) return true;
		}
		return false;
	}

	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthoritys(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Transient
	public Authority getAuthority(int id) {
		return findEntityInCollection(authorities, id);
	}

	@Transient
	public Authority getAuthority(String name) {
		return findNamedEntityInCollection(authorities, name);
	}

	@Transient
	public void addAuthority(Authority authority) {
		addEntityToCollection(authorities, authority);
	}

	@Transient
	public void addAuthorities(Collection<Authority> clc) {
		addEntitiesToCollection(clc, authorities);
	}

	@Transient
	public void removeAuthority(Authority authority) {
		removeEntityFromCollection(authorities, authority);
	}

	@Transient
	public void removeAuthorities(Collection<Authority> clc) {
		clearEntityCollection(authorities);
	}

	@Transient
	public int getNumAuthorities() {
		return getCollectionSize(authorities);
	}

	/**
	 * @return Returns the account.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aid")
	@NotNull
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account The account to set.
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return Returns the address.
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = {
		CascadeType.MERGE,
		CascadeType.PERSIST })
	@JoinColumn(name = "adr_id")
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address The address to set.
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	@Transient
	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public Integer accountId() {
		try {
			return getAccount().getId();
		}
		catch(NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

	/**
	 * Acegi implementation must not return null
	 */
	@Transient
	public GrantedAuthority[] getAuthorities() {
		return authorities == null ? new Authority[0] : authorities.toArray(new Authority[authorities.size()]);
	}

	@Transient
	public String getUsername() {
		return getEmailAddress();
	}

	public void setUsername(String username) {
		setEmailAddress(username);
	}

	@Transient
	public boolean isAccountNonExpired() {
		return (new Date()).getTime() < (expires == null ? 0L : expires.getTime());
	}

	@Transient
	public boolean isAccountNonLocked() {
		return !getLocked();
	}

	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Column
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
