package com.tll.model.impl.key;

import com.tll.model.impl.Authority;
import com.tll.model.key.BusinessKey;

public final class AuthorityKey extends BusinessKey<Authority> {
  private static final long serialVersionUID = -5426833954947961036L;

  private static final String[] FIELDS = new String[] { "authority" };

	public Class<Authority> getType() {
		return Authority.class;
	}

	@Override
  protected String[] getFields() {
    return FIELDS;
  }

  public void setEntity(Authority e) {
    e.setAuthority(getAuthority());
  }

  @Override
  protected String keyDescriptor() {
    return "Authority";
  }

  public String getAuthority() {
    return (String) getValue(0);
  }

  public void setAuthority(String authority) {
    setValue(0, authority);
  }

}