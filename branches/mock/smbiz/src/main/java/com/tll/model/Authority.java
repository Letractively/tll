package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;
import org.springframework.security.GrantedAuthority;

/**
 * Implementation of Acegi's
 * {@link org.springframework.security.GrantedAuthority} interface.
 * @author jpk
 */
@PersistenceCapable(table = "authority")
@Uniques(value = @Unique(name = "Authority", members = { Authority.FIELDNAME_AUTHORITY }))
public class Authority extends EntityBase implements INamedEntity, GrantedAuthority {

	static final long serialVersionUID = -4601781277584062384L;

	public static final String FIELDNAME_AUTHORITY = "authority";

	public static final int MAXLEN_AUTHORITY = 50;

	@Persistent
	private String role;

	public Class<? extends IEntity> entityClass() {
		return Authority.class;
	}

	@NotEmpty
	@Length(max = MAXLEN_AUTHORITY)
	public String getAuthority() {
		return role;
	}

	public void setAuthority(String authority) {
		this.role = authority;
	}

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
			final GrantedAuthority attr = (GrantedAuthority) obj;
			return this.role.equals(attr.getAuthority());
		}

		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public int compareTo(Object o) {
		if(o != null && o instanceof GrantedAuthority) {
			final String rhsRole = ((GrantedAuthority) o).getAuthority();
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
