package com.tll.dao;

import com.tll.IMarshalable;
import com.tll.criteria.IPropertyNameProvider;

/**
 * Represents a sort directive for a single "column".
 * @author jpk
 */
public class SortColumn implements IMarshalable, IPropertyNameProvider {

	private static final long serialVersionUID = -4966927388892147102L;

	public static final Boolean DEFAULT_IGNORE_CASE = Boolean.TRUE;

	public static final SortDir DEFAULT_SORT_DIR = SortDir.ASC;

	/**
	 * The property name
	 */
	private String propertyName;

	/**
	 * The parent ref alias name. Intended mainly to be used in sql/orm queries.
	 * In an ORM context, this property is necessary to avoid ambiguity in the
	 * query.
	 */
	private String parentAlias;

	/**
	 * The sort direction: ascending or descending
	 */
	private SortDir direction = DEFAULT_SORT_DIR;

	/**
	 * Ignore upper/lower case when column is text-based?
	 */
	private Boolean ignoreCase = DEFAULT_IGNORE_CASE;

	/**
	 * Constructor
	 */
	public SortColumn() {
		super();
	}

	/**
	 * Constructor
	 * @param propertyName
	 */
	public SortColumn(String propertyName) {
		this(propertyName, null, DEFAULT_SORT_DIR, DEFAULT_IGNORE_CASE);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param parentAlias
	 */
	public SortColumn(String propertyName, String parentAlias) {
		this(propertyName, parentAlias, DEFAULT_SORT_DIR, DEFAULT_IGNORE_CASE);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param parentAlias
	 * @param direction
	 */
	public SortColumn(String propertyName, String parentAlias, SortDir direction) {
		this(propertyName, parentAlias, direction, DEFAULT_IGNORE_CASE);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param parentAlias
	 * @param ignoreCase
	 */
	public SortColumn(String propertyName, String parentAlias, Boolean ignoreCase) {
		this(propertyName, parentAlias, DEFAULT_SORT_DIR, ignoreCase);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param parentAlias
	 * @param direction
	 * @param ignoreCase
	 */
	public SortColumn(String propertyName, String parentAlias, SortDir direction, Boolean ignoreCase) {
		this();
		setPropertyName(propertyName);
		setParentAlias(parentAlias);
		setDirection(direction);
		setIgnoreCase(ignoreCase);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getParentAlias() {
		return parentAlias;
	}

	public void setParentAlias(String parentAlias) {
		this.parentAlias = parentAlias;
	}

	public SortDir getDirection() {
		return direction;
	}

	public void setDirection(SortDir direction) {
		this.direction = direction;
	}

	public boolean isAscending() {
		return SortDir.ASC == direction;
	}

	public boolean isDescending() {
		return SortDir.DESC == direction;
	}

	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final SortColumn other = (SortColumn) obj;
		if(direction == null) {
			if(other.direction != null) return false;
		}
		else if(!direction.equals(other.direction)) return false;
		if(ignoreCase == null) {
			if(other.ignoreCase != null) return false;
		}
		else if(!ignoreCase.equals(other.ignoreCase)) return false;
		if(parentAlias == null) {
			if(other.parentAlias != null) return false;
		}
		else if(!parentAlias.equals(other.parentAlias)) return false;
		if(propertyName == null) {
			if(other.propertyName != null) return false;
		}
		else if(!propertyName.equals(other.propertyName)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((ignoreCase == null) ? 0 : ignoreCase.hashCode());
		result = prime * result + ((parentAlias == null) ? 0 : parentAlias.hashCode());
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		return result;
	}

	/**
	 * @return SQL valid order by sub-clause.
	 *         <p>
	 *         NOTE: Does NOT account for the ignoreCase flag.
	 */
	@Override
	public String toString() {
		String s = parentAlias == null ? "" : parentAlias + ".";
		return s + propertyName + " " + getDirection().getSqlclause();
	}
}
