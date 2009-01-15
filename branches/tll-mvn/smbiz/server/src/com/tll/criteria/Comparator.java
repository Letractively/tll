package com.tll.criteria;


/**
 * Enumeration class that defines the comparators that can be used to specify a
 * criterion.
 * @author jpk
 */
public enum Comparator {
	EQUALS,
	NOT_EQUALS,
	GREATER_THAN,
	GREATER_THAN_EQUALS,
	LESS_THAN,
	LESS_THAN_EQUALS,
	LIKE,
	CONTAINS,
	STARTS_WITH,
	ENDS_WITH,
	BETWEEN,
	/**
	 * For db null checking.
	 */
	IS,
	IN;
}
