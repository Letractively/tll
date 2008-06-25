package com.tll.criteria;

import com.tll.SystemError;
import com.tll.util.CommonUtil;

/**
 * Object representing a criterion definition. It contains the field name and
 * value as well as the comparator. Any other fields that can be used to
 * describe a criterion should be added here.
 * @author jpk
 */
public class Criterion implements ICriterion {

	private static final long serialVersionUID = -9033958462037763702L;

	private String field;

	private Object value;

	private Comparator comparator;

	private boolean caseSensitive;

	public Criterion() {
		this(null, null);
	}

	public Criterion(String field, Object value) {
		this(field, value, Comparator.EQUALS, true);
	}

	public Criterion(String field, Object value, Comparator comparator, boolean caseSensitive) {
		super();
		setFieldValueComparator(field, value, comparator);
		setCaseSensitive(caseSensitive);
	}

	public boolean isGroup() {
		return false;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getPropertyName() {
		return this.field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setFieldValue(String field, Object value) {
		setField(field);
		setValue(value);
	}

	public void setFieldValueComparator(String field, Object value, Comparator comp) {
		setFieldValue(field, value);
		setComparator(comp);
	}

	public boolean isSet() {
		if(Comparator.LIKE.equals(getComparator()) && "%".equals(getValue())) {
			// if we are doing a like query and the value is just %, then ignore this
			// criterion
			return false;
		}
		return (getValue() != null);
	}

	public void clear() {
		setFieldValue(null, null);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;

		if(obj == null || !(obj instanceof Criterion)) return false;

		final Criterion other = (Criterion) obj;

		if(getField() == null || !getField().equals(other.getField())) {
			return false;
		}
		if(getComparator() == null || !getComparator().equals(other.getComparator())) {
			return false;
		}
		if(isCaseSensitive() != isCaseSensitive()) {
			return false;
		}
		if(getValue() == null || !getValue().equals(other.getValue())) {
			return false;
		}

		return true;
	}

	@Override
	protected ICriterion clone() {
		try {
			final Criterion result = (Criterion) super.clone();
			final Object value = getValue();
			result.setValue(CommonUtil.clone(value));
			return result;
		}
		catch(final CloneNotSupportedException cnse) {
			throw new SystemError("This should never happen!");
		}
	}

	public ICriterion copy() {
		return clone();
	}

}
