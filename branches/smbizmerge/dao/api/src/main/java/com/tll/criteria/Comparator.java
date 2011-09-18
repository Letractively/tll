package com.tll.criteria;

import java.util.Date;

import org.apache.commons.lang.math.NumberRange;

import com.tll.util.CommonUtil;
import com.tll.util.DateRange;

/**
 * Comparator - Enumarates comparators that can be used to specify a criterion.
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

	/**
	 * Generalized way to compare to objects subject to a {@link Comparator}
	 * instance.
	 * @param actualValue The value being checked. May be <code>null</code>
	 * @param checkValue The sought value. May be <code>null</code>
	 * @param caseSensitive Enforce case sensitivity? Not applicable in all cases.
	 * @param cmp the required comparator
	 * @return true/false
	 * @throws IllegalArgumentException
	 */
	public static boolean matches(final Object actualValue, final Object checkValue, boolean caseSensitive, Comparator cmp)
			throws IllegalArgumentException {
		if(cmp == null) {
			throw new IllegalArgumentException("The comparator argument may not be null");
		}
		switch(cmp) {
			case EQUALS:
				return CommonUtil.equals(actualValue, checkValue, caseSensitive);
			case NOT_EQUALS:
				return !CommonUtil.equals(actualValue, checkValue, caseSensitive);
			case GREATER_THAN:
				try {
					final int c = CommonUtil.compare(actualValue, checkValue);
					return c > 0;
				}
				catch(final IllegalArgumentException e) {
					return false;
				}
			case GREATER_THAN_EQUALS:
				try {
					final int c = CommonUtil.compare(actualValue, checkValue);
					return c >= 0;
				}
				catch(final IllegalArgumentException e) {
					return false;
				}
			case LESS_THAN:
				try {
					final int c = CommonUtil.compare(actualValue, checkValue);
					return c < 0;
				}
				catch(final IllegalArgumentException e) {
					return false;
				}
			case LESS_THAN_EQUALS:
				try {
					final int c = CommonUtil.compare(actualValue, checkValue);
					return c <= 0;
				}
				catch(final IllegalArgumentException e) {
					return false;
				}
			case LIKE:
				// TODO impl
				throw new IllegalArgumentException("LIKE comparisons are not supported yet");
			case CONTAINS:
				if(checkValue instanceof String == false || actualValue instanceof String == false) {
					return false;
				}
				if(!caseSensitive) {
					return ((String) checkValue).toLowerCase().indexOf(((String) actualValue).toLowerCase()) >= 0;
				}
				return ((String) actualValue).indexOf((String) checkValue) >= 0;
			case STARTS_WITH:
				if(checkValue instanceof String == false || actualValue instanceof String == false) {
					return false;
				}
				if(!caseSensitive) {
					return ((String) checkValue).toLowerCase().startsWith(((String) actualValue).toLowerCase());
				}
				return ((String) checkValue).startsWith((String) actualValue);
			case ENDS_WITH:
				if(checkValue instanceof String == false || actualValue instanceof String == false) {
					return false;
				}
				return ((String) checkValue).endsWith((String) actualValue);
			case BETWEEN: {
				if(checkValue instanceof NumberRange) {
					if(actualValue instanceof Number == false) {
						return false;
					}
					final Number number = (Number) actualValue;
					final NumberRange range = (NumberRange) checkValue;
					return range.containsNumber(number);
				}
				else if(checkValue instanceof DateRange) {
					if(actualValue instanceof Date == false) {
						return false;
					}
					final Date date = (Date) actualValue;
					final DateRange range = (DateRange) checkValue;
					return range.includes(date);
				}
				return false;
			}
			case IS: {
				if(checkValue instanceof DBType == false) {
					return false;
				}
				final DBType dbType = (DBType) checkValue;
				if(dbType == DBType.NULL) {
					return actualValue != null;
				}
				return actualValue == null;
			}
			case IN: {
				throw new UnsupportedOperationException("Need to re-factor to eliminate spring dependency!");
//				if(checkValue.getClass().isArray()) {
//					return org.springframework.util.ObjectUtils.containsElement((Object[]) actualValue, checkValue);
//				}
//				else if(checkValue instanceof Collection<?>) {
//					return ((Collection<?>) checkValue).contains(actualValue);
//				}
//				else if(checkValue instanceof String) {
//					// assume comma-delimited string
//					final Object[] arr =
//							org.springframework.util.ObjectUtils.toObjectArray(org.springframework.util.StringUtils
//									.commaDelimitedListToStringArray((String) actualValue));
//					return org.springframework.util.ObjectUtils.containsElement(arr, actualValue);
//				}
//				return false;
			}
			default:
				throw new IllegalStateException("Unhandled Comparator: " + cmp);
		}
	}

}
