package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import com.tll.client.model.IPropertyValue;
import com.tll.client.model.StringPropertyValue;
import com.tll.model.EntityBase;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.key.BusinessKey;

/**
 * Implementation of Acegi's {@link org.acegisecurity.GrantedAuthority}
 * interface.
 * @author jpk
 */
@Entity
@Table(name = "authority")
// NOTE: we can't test when the caching is read only!
// TODO fix this to allow for testing yet have read only caching
// @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Authority extends EntityBase implements INamedEntity, GrantedAuthority {

	static final long serialVersionUID = -4601781277584062384L;

	public static final String FIELDNAME_AUTHORITY = "authority";

	public static final int MAXLEN_AUTHORITY = 50;

	public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";

	public static final String ROLE_USER = "ROLE_USER";

	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

	private String authority;

	public Class<? extends IEntity> entityClass() {
		return Authority.class;
	}

	@Column(name = FIELDNAME_AUTHORITY)
	@NotEmpty
	@Length(max = MAXLEN_AUTHORITY)
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Transient
	public String getName() {
		return getAuthority();
	}

	public void setName(String name) {
		setAuthority(name);
	}

	@Override
	public boolean equals(Object obj) {
		// IMPT: We need to support comparisons to raw strings for ACL related
		// functionality.
		if(obj instanceof String) {
			return obj.equals(getAuthority());
		}

		if(obj instanceof GrantedAuthority && getAuthority() != null) {
			GrantedAuthority attr = (GrantedAuthority) obj;
			return getAuthority().equals(attr.getAuthority());
		}

		return super.equals(obj);
	}

	@Override
	@Transient
	public BusinessKey[] getBusinessKeys() {
		return new BusinessKey[] { new BusinessKey(Authority.class, "Authority",
				new IPropertyValue[] { new StringPropertyValue("authority", getAuthority()) }) };
	}

	@Override
	public String descriptor() {
		return getAuthority();
	}
}
