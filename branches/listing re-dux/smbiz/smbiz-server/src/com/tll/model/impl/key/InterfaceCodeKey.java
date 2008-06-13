package com.tll.model.impl.key;

import com.tll.model.impl.Interface;
import com.tll.model.key.BusinessKey;

/**
 * The {@link Interface} code business key.
 * 
 * @author jpk
 */
public final class InterfaceCodeKey extends BusinessKey<Interface> {
  private static final long serialVersionUID = -6645245708826397142L;

  private static final String[] FIELDS = new String[] { "code" };

  public InterfaceCodeKey() {
    super();
  }

  public InterfaceCodeKey(String code) {
    this();
    setCode(code);
  }

	public Class<Interface> getType() {
		return Interface.class;
	}

	@Override
  protected String keyDescriptor() {
    return "Code";
  }
  
  /*
   * @see com.tll.model.key.EntityKey#getFields()
   */
  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  /*
   * @see com.tll.model.key.IEntityKey#setEntity(com.tll.model.IEntity)
   */
  public void setEntity(Interface e) {
    e.setCode(getCode());
  }

  public String getCode() {
    return (String) getValue(0);
  }

  public void setCode(String code) {
    setValue(0, code);
  }

}