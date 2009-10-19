/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.common.search;

import com.tll.util.StringUtil;

/**
 * AccountInterfaceData
 * @author jpk
 */
public class AccountInterfaceDataSearch extends SearchBase {

	private String accountId, interfaceId;

	/**
	 * Constructor
	 */
	public AccountInterfaceDataSearch() {
		super();
		
	}

	/**
	 * Constructor
	 * @param accountId
	 * @param interfaceId
	 */
	public AccountInterfaceDataSearch(String accountId, String interfaceId) {
		super();
		this.accountId = accountId;
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean isSet() {
		return !StringUtil.isEmpty(accountId) && !StringUtil.isEmpty(interfaceId);
	}

	public String getAccountId() {
		return accountId;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

}
