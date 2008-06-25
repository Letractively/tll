package com.tll.criteria;

import com.tll.client.model.IPropertyValue;

/**
 * Object representing a criterion definition. It contains the field name and
 * value as well as the comparator. Any other fields that can be used to
 * describe a criterion should be added here.
 * @author jpk
 */
public class Criterion implements ICriterion {

	private static final long serialVersionUID = -9033958462037763702L;

	private IPropertyValue propertyValue;

	private Comparator comparator;

	private boolean caseSensitive;

	public Criterion() {
		this(null, null, false);
	}

	public Criterion(IPropertyValue propertyValue, Comparator comparator, boolean isCaseSensitive) {
		this.propertyValue = propertyValue;
		this.comparator = comparator;
		this.caseSensitive = isCaseSensitive;
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

	public IPropertyValue getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(IPropertyValue propertyValue) {
		this.propertyValue = propertyValue;
	}

	public boolean isSet() {
		if(propertyValue == null || propertyValue.getValue() == null || comparator == null) return false;
		if(Comparator.LIKE.equals(getComparator()) && "%".equals(propertyValue.getValue())) {
			// if we are doing a like query and the value is just %, then ignore this
			// criterion
			return false;
		}
		return true;
	}

	public void clear() {
		this.propertyValue = null;
		this.comparator = null;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final Criterion other = (Criterion) obj;
		if(caseSensitive != other.caseSensitive) return false;
		if(comparator == null) {
			if(other.comparator != null) return false;
		}
		else if(!comparator.equals(other.comparator)) return false;
		if(propertyValue == null) {
			if(other.propertyValue != null) return false;
		}
		else if(!propertyValue.equals(other.propertyValue)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (caseSensitive ? 1231 : 1237);
		result = prime * result + ((comparator == null) ? 0 : comparator.hashCode());
		result = prime * result + ((propertyValue == null) ? 0 : propertyValue.hashCode());
		return result;
	}
}
