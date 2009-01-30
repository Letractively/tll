package com.tll.model;

import java.io.Serializable;


public class NestedData implements Serializable {
  private static final long serialVersionUID = 4794589680528322269L;
  
  private String nested1;

  private String nested2;

  public NestedData() {
    super();
  }

  public String getNested1() {
		return nested1;
  }

  public void setNested1(String bankAccountNo) {
		this.nested1 = bankAccountNo;
  }

  public String getNested2() {
		return nested2;
  }

  public void setNested2(String bankName) {
		this.nested2 = bankName;
  }

}
