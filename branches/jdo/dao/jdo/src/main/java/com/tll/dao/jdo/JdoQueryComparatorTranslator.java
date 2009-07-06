/**
 * The Logic Lab
 * @author jpk
 * @since Jun 29, 2009
 */
package com.tll.dao.jdo;

import org.apache.commons.lang.math.NumberRange;

import com.tll.criteria.IComparatorTranslator;
import com.tll.criteria.ICriterion;
import com.tll.util.DateRange;

/**
 * JdoQueryComparatorTranslator - Creates a JDO-compliant query sub-string.
 * @author jpk
 */
public class JdoQueryComparatorTranslator implements IComparatorTranslator<String> {

	@Override
	public String translate(ICriterion ctn) {
		final String name = ctn.getField();
		final Object value = ctn.getValue();
		final StringBuilder sb = new StringBuilder();
		switch(ctn.getComparator()) {
			case BETWEEN: {
				Object value1 = null;
				Object value2 = null;
				if(value instanceof NumberRange) {
					final NumberRange range = (NumberRange) value;
					value1 = range.getMinimumNumber();
					value2 = range.getMaximumNumber();
				}
				else if(value instanceof DateRange) {
					final DateRange range = (DateRange) value;
					value1 = range.start();
					value2 = range.end();
				}
				else {
					// we will assume that the value is an array
					final Object[] valueArr = (Object[]) value;
					value1 = valueArr[0];
					value2 = valueArr[1];
				}
				if(ctn.isCaseSensitive() && value1 instanceof String) {
					sb.append(name);
					sb.append(">=");
					sb.append(value1);
					sb.append(" && ");
					sb.append(name);
					sb.append("<=");
					sb.append(value2);
				}
				else {
					sb.append(name);
					sb.append(".toLowerCase() >= '");
					sb.append(((String) value1).toLowerCase());
					sb.append("' && ");
					sb.append(name);
					sb.append(".toLowerCase() <= '");
					sb.append(((String) value2).toLowerCase());
					sb.append("'");
				}
				break;
			}
			case CONTAINS:
				if(value instanceof String) {
					sb.append(name);
					if(ctn.isCaseSensitive()) sb.append(".toLowerCase()");
					sb.append(".indexOf(");
					sb.append(ctn.isCaseSensitive() ? ((String) value).toLowerCase() : value);
					sb.append(") >= 0");
				}
				break;
			case ENDS_WITH:
				if(value instanceof String) {
					sb.append(name);
					if(ctn.isCaseSensitive()) sb.append(".toLowerCase()");
					sb.append(".endsWith(");
					sb.append(ctn.isCaseSensitive() ? ((String) value).toLowerCase() : value);
				}
				break;
			case EQUALS:
				sb.append(name);
				if(ctn.isCaseSensitive()) sb.append(".toLowerCase()");
				sb.append(" == ");
				sb.append(ctn.isCaseSensitive() ? ((String) value).toLowerCase() : value);
				break;
			case GREATER_THAN:
				sb.append(name);
				sb.append(" > ");
				sb.append(value);
				break;
			case GREATER_THAN_EQUALS:
				sb.append(name);
				sb.append(" >= ");
				sb.append(value);
				break;
			case IN:
				// not supported
				break;
			case IS:
				// not supported
				break;
			case LESS_THAN:
				sb.append(name);
				sb.append(" < ");
				sb.append(value);
				break;
			case LESS_THAN_EQUALS:
				sb.append(name);
				sb.append(" <= ");
				sb.append(value);
				break;
			case LIKE:
				// not supported
				break;
			case NOT_EQUALS:
				sb.append(name);
				if(ctn.isCaseSensitive()) sb.append(".toLowerCase()");
				sb.append(" != ");
				sb.append(ctn.isCaseSensitive() ? ((String) value).toLowerCase() : value);
				break;
			case STARTS_WITH:
				if(value instanceof String) {
					sb.append(name);
					if(ctn.isCaseSensitive()) sb.append(".toLowerCase()");
					sb.append(".endsWith(");
					sb.append(ctn.isCaseSensitive() ? ((String) value).toLowerCase() : value);
				}
				break;
		}
		if(sb.length() < 1) {
			throw new UnsupportedOperationException("Un-supported criterion state: " + ctn);
		}
		return sb.toString();
	}

}
