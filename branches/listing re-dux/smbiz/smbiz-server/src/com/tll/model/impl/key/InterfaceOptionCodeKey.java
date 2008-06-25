package com.tll.model.impl.key;

import com.tll.model.impl.InterfaceOption;
import com.tll.model.key.BusinessKey;

/**
 * The {@link InterfaceOption} code business key.
 * 
 * @author jpk
 */
public final class InterfaceOptionCodeKey extends BusinessKey<InterfaceOption> {
  private static final long serialVersionUID = 1740448672909212771L;

  private static final String[] FIELDS = new String[] { "code" };

  public InterfaceOptionCodeKey() {
    super();
  }

  public InterfaceOptionCodeKey(String code) {
    this();
    setCode(code);
  }

	public Class<InterfaceOption> getType() {
		return InterfaceOption.class;
	}

	@Override
  protected String keyDescriptor() {
    return "Code";
  }

  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  public void setEntity(InterfaceOption e) {
    e.setCode(getCode());
  }

  public String getCode() {
    return (String) getValue(0);
  }

  public void setCode(String code) {
    setValue(0, code);
  }

}