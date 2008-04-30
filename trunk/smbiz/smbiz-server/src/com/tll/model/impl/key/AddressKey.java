package com.tll.model.impl.key;

import com.tll.model.impl.Address;
import com.tll.model.key.BusinessKey;

public final class AddressKey extends BusinessKey<Address> {
  private static final long serialVersionUID = 7859574420062671046L;
  
  private static final String[] FIELDS = new String[] { "address1", "postalCode" };

  public AddressKey() {
    super();
  }

  public AddressKey(String address1, String postalCode) {
    this();
    setAddress1(address1);
    setPostalCode(postalCode);
  }

	public Class<Address> getType() {
		return Address.class;
	}

	@Override
  protected String keyDescriptor() {
    return "Address Line 1 and Postal Code";
  }
  
  @Override
  protected String[] getFields() {
    return FIELDS;
  }

  public void setEntity(Address e) {
    e.setAddress1( getAddress1() );
    e.setPostalCode( getPostalCode() );
  }

  public String getAddress1() {
    return (String) getValue(0);
  }

  public void setAddress1(String value) {
    setValue(0, value);
  }

  public String getPostalCode() {
    return (String) getValue(1);
  }

  public void setPostalCode(String value) {
    setValue(1, value);
  }

}