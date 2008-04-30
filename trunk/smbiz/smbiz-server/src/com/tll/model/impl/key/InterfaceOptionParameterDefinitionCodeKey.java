package com.tll.model.impl.key;

import com.tll.model.impl.InterfaceOptionParameterDefinition;
import com.tll.model.key.BusinessKey;

/**
 * The {@link InterfaceOptionParameterDefinition} code business key.
 * 
 * @author jpk
 */
public final class InterfaceOptionParameterDefinitionCodeKey extends BusinessKey<InterfaceOptionParameterDefinition> {
  private static final long serialVersionUID = 5190275037935183400L;

  private static final String[] FIELDS = new String[] { "code" };

  public InterfaceOptionParameterDefinitionCodeKey() {
    super();
  }

  public InterfaceOptionParameterDefinitionCodeKey(String code) {
    this();
    setCode(code);
  }

	public Class<InterfaceOptionParameterDefinition> getType() {
		return InterfaceOptionParameterDefinition.class;
	}

	@Override
  protected String keyDescriptor() {
    return "Code";
  }

  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  public void setEntity(InterfaceOptionParameterDefinition e) {
    e.setCode(getCode());
  }

  public String getCode() {
    return (String) getValue(0);
  }

  public void setCode(String code) {
    setValue(0, code);
  }

}