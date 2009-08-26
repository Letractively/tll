/**
 * The Logic Lab
 * @author jpk
 * @since Jun 29, 2009
 */
package com.tll.dao.jdo;

import org.apache.commons.lang.math.NumberRange;

import com.tll.criteria.Criterion;
import com.tll.criteria.IComparatorTranslator;

/**
 * JdoQueryComparatorTranslator - Creates a JDO-compliant query sub-string.
 * @author jpk
 */
public class JdoQueryComparatorTranslator implements IComparatorTranslator<String> {

	@Override
	public String translate(Criterion ctn) {
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
				if(value1 != null && value2 != null) {
					sb.append(name);
					sb.append(" >= ");
					sb.append(value1);
					sb.append(" && ");
					sb.append(name);
					sb.append(" <= ");
					sb.append(value2);
				}
				break;
			}
			case CONTAINS:
				if(value instanceof String) {
					sb.append(name);
					// if(!ctn.isCaseSensitive()) sb.append(".toLowerCase()");
					sb.append(".indexOf('");
					// sb.append(!ctn.isCaseSensitive() ? ((String) value).toLowerCase()
					// : value);
					sb.append(value);
					sb.append("') >= 0");
				}
				break;
			case ENDS_WITH:
				if(value instanceof String) {
					sb.append(name);
					// if(!ctn.isCaseSensitive()) sb.append(".toLowerCase()");
					sb.append(".endsWith('");
					// sb.append(!ctn.isCaseSensitive() ? ((String) value).toLowerCase()
					// : value);
					sb.append(value);
					sb.append("')");
				}
				break;
			case EQUALS:
				if(value instanceof String) {
					sb.append(name);
					// if(!ctn.isCaseSensitive()) sb.append(".toLowerCase()");
					sb.append(" == '");
					// sb.append(!ctn.isCaseSensitive() ? ((String) value).toLowerCase()
					// : value);
					sb.append(value);
					sb.append("'");
				}
				else {
					sb.append(name);
					sb.append(" == ");
					sb.append(value);
				}
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
				if(value instanceof String) {
					sb.append(name);
					// if(!ctn.isCaseSensitive()) sb.append(".toLowerCase()");
					sb.append(" != '");
					// sb.append(!ctn.isCaseSensitive() ? ((String) value).toLowerCase()
					// : value);
					sb.append(value);
					sb.append("'");
				}
				else {
					sb.append(name);
					sb.append(" == ");
					sb.append(value);
				}
				break;
			case STARTS_WITH:
				if(value instanceof String) {
					sb.append(name);
					// if(!ctn.isCaseSensitive()) sb.append(".toLowerCase()");
					sb.append(".startsWith('");
					// sb.append(!ctn.isCaseSensitive() ? ((String) value).toLowerCase()
					// : value);
					sb.append(value);
					sb.append("'");
				}
				break;
		}
		if(sb.length() < 1) {
			throw new UnsupportedOperationException("Un-supported criterion state: " + ctn);
		}
		return sb.toString();
	}

}
