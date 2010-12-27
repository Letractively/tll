package com.tll.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.GrantedAuthority;

import com.tll.model.bk.BusinessKeyDef;

/**
 * Implementation of Acegi's
 * {@link org.springframework.security.GrantedAuthority} interface.
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Authority", properties = { Authority.FIELDNAME_AUTHORITY }))
public class Authority extends EntityBase implements INamedEntity, GrantedAuthority {

	static final long serialVersionUID = -4601781277584062384L;

	public static final String FIELDNAME_AUTHORITY = "authority";

	public static final int MAXLEN_AUTHORITY = 50;

	/**
	 * I.e. the role.
	 */
	private String authority;

	@Override
	public Class<? extends IEntity> entityClass() {
		return Authority.class;
	}

	@Override
	@NotEmpty
	@Length(max = MAXLEN_AUTHORITY)
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(final String authority) {
		this.authority = authority;
	}

	@Override
	public String getName() {
		return getAuthority();
	}

	@Override
	public void setName(final String name) {
		setAuthority(name);
	}

	@Override
	public boolean equals(final Object obj) {
		// IMPT: We need to support comparisons to raw strings for ACL related
		// functionality.
		if(obj instanceof String) {
			return obj.equals(this.authority);
		}

		if(obj instanceof GrantedAuthority && this.authority != null) {
			final GrantedAuthority attr = (GrantedAuthority) obj;
			return this.authority.equals(attr.getAuthority());
		}

		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public int compareTo(final Object o) {
		if(o != null && o instanceof GrantedAuthority) {
			final String rhsRole = ((GrantedAuthority) o).getAuthority();
			if(rhsRole == null) {
				return -1;
			}
			return authority.compareTo(rhsRole);
		}
		return -1;
	}

	@Override
	public String descriptor() {
		return getAuthority();
	}
}
