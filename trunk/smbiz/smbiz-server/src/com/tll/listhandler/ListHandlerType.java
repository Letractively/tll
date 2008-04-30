package com.tll.listhandler;

/**
 * Enumerates the defined {@link com.tll.listhandler.IListHandler} types
 * (implementations). See the com.tll.listhandler package.
 * @author jpk
 */
public enum ListHandlerType {
	COLLECTION,
	IDLIST,
	PAGE;

	/**
	 * Does the {@link ListHandlerType} employ search criteria?
	 * @param lht The {@link ListHandlerType}
	 * @return true/false
	 */
	public static boolean isSearchBased(ListHandlerType lht) {
		return ListHandlerType.IDLIST.equals(lht) || ListHandlerType.PAGE.equals(lht);
	}

}