package com.tll.model.impl.key;

import com.tll.model.impl.Interface;
import com.tll.model.impl.InterfaceOptionAccount;
import com.tll.model.key.BusinessKey;

/**
 * The {@link Interface} code business key.
 * 
 * @author jpk
 */
public final class InterfaceOptionAccountBinderKey extends BusinessKey<InterfaceOptionAccount> {
  private static final long serialVersionUID = 8254209417586443060L;

  private static final String[] FIELDS = new String[] { "option.id", "account.id" };

  public InterfaceOptionAccountBinderKey() {
    super();
  }

  public InterfaceOptionAccountBinderKey(Integer optionId, Integer accountId) {
    this();
    setOptionId(optionId);
    setAccountId(accountId);
  }

	public Class<InterfaceOptionAccount> getType() {
		return InterfaceOptionAccount.class;
	}

	@Override
  protected String keyDescriptor() {
    return "Binder";
  }

  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  public Integer getAccountId() {
    return (Integer) getValue(0);
  }

  public void setAccountId(Integer id) {
    setValue(0, id);
  }

  public Integer getOptionId() {
    return (Integer) getValue(1);
  }

  public void setOptionId(Integer id) {
    setValue(1, id);
  }

  public void setEntity(InterfaceOptionAccount e) {
    e.getOption().setId(getOptionId());
    e.getAccount().setId(getAccountId());
  }

}