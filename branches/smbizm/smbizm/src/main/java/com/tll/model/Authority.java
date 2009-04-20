package com.tll.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.springframework.security.GrantedAuthority;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * Implementation of Acegi's
 * {@link org.springframework.security.GrantedAuthority} interface.
 * @author jpk
 */
@Entity
@Table(name = "authority")
// NOTE: we can't test when the caching is read only!
// TODO fix this to allow for testing yet have read only caching
// @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Authority", properties = { Authority.FIELDNAME_AUTHORITY }))
public class Authority extends EntityBase implements INamedEntity, GrantedAuthority {

	static final long serialVersionUID = -4601781277584062384L;

	public static final String FIELDNAME_AUTHORITY = "authority";

	public static final int MAXLEN_AUTHORITY = 50;

	private String role;

	public Class<? extends IEntity> entityClass() {
		return Authority.class;
	}

	@Column(name = FIELDNAME_AUTHORITY)
	@NotEmpty
	@Length(max = MAXLEN_AUTHORITY)
	public String getAuthority() {
		return role;
	}

	public void setAuthority(String authority) {
		this.role = authority;
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
			return obj.equals(this.role);
		}

		if(obj instanceof GrantedAuthority && this.role != null) {
			GrantedAuthority attr = (GrantedAuthority) obj;
			return this.role.equals(attr.getAuthority());
		}

		return super.equals(obj);
	}

	@Override
	public int compareTo(Object o) {
		if(o != null && o instanceof GrantedAuthority) {
			String rhsRole = ((GrantedAuthority) o).getAuthority();
			if(rhsRole == null) {
				return -1;
			}
			return role.compareTo(rhsRole);
		}
		return -1;
	}

	@Override
	public String descriptor() {
		return getAuthority();
	}
}
