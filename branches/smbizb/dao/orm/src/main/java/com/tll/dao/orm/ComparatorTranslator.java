/*
 * The Logic Lab 
 */
package com.tll.dao.orm;

import java.util.Collection;

import org.apache.commons.lang.math.NumberRange;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.tll.criteria.Comparator;
import com.tll.criteria.DBType;
import com.tll.criteria.IComparatorTranslator;
import com.tll.util.DateRange;

/**
 * ComparatorTranslator
 * @author jpk
 */
public class ComparatorTranslator implements IComparatorTranslator<Criterion> {

	@SuppressWarnings("unchecked")
	public Criterion translate(Comparator c, String name, Object value) {

		switch(c) {
			case BETWEEN: {
				Object value1 = null;
				Object value2 = null;
				if(value instanceof NumberRange) {
					NumberRange range = (NumberRange) value;
					value1 = range.getMinimumNumber();
					value2 = range.getMaximumNumber();
				}
				else if(value instanceof DateRange) {
					DateRange range = (DateRange) value;
					value1 = range.start();
					value2 = range.end();
				}
				else {
					// we will assume that the value is an array
					Object[] valueArr = (Object[]) value;
					value1 = valueArr[0];
					value2 = valueArr[1];
				}
				return Restrictions.between(name, value1, value2);
			}
			case CONTAINS:
				return Restrictions.like(name, "%" + value + "%");
			case ENDS_WITH:
				return Restrictions.like(name, "%" + value);
			case EQUALS:
				if(value == DBType.NULL) {
					return Restrictions.isNull(name);
				}
				return Restrictions.eq(name, value);
			case GREATER_THAN:
				return Restrictions.gt(name, value);
			case GREATER_THAN_EQUALS:
				return Restrictions.ge(name, value);
			case IN:
				if(value.getClass().isArray()) {
					return Restrictions.in(name, ObjectUtils.toObjectArray(value));
				}
				else if(value instanceof Collection) {
					return Restrictions.in(name, (Collection) value);
				}
				else if(value instanceof String) { // assume comma-delimited string
					return Restrictions.in(name, ObjectUtils.toObjectArray(StringUtils
							.commaDelimitedListToStringArray((String) value)));
				}
				return Restrictions.in(name, new Object[] { value });
			case IS:
				if(value == DBType.NULL) {
					return Restrictions.isNull(name);
				}
				else if(value == DBType.NOT_NULL) {
					return Restrictions.isNotNull(name);
				}
				throw new IllegalArgumentException("Value for comparator IS must be either DBType.NULL or DBType.NOT_NULL");
			case LESS_THAN:
				return Restrictions.lt(name, value);
			case LESS_THAN_EQUALS:
				return Restrictions.le(name, value);
			case LIKE:
				return Restrictions.like(name, value);
			case NOT_EQUALS:
				return Restrictions.ne(name, value);
			case STARTS_WITH:
				Restrictions.like(name, value + "%");
		}

		throw new IllegalArgumentException("Unhandled comparator type: " + c);
	}
}
