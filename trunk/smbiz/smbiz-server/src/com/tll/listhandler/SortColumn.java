package com.tll.listhandler;

import com.tll.client.IMarshalable;

/**
 * Represents a sort directive for a single "column".
 * @author jpk
 */
public class SortColumn implements IMarshalable {

	private static final long serialVersionUID = -4966927388892147102L;

	public static final Boolean DEFAULT_IGNORE_CASE = Boolean.TRUE;
	public static final SortDir DEFAULT_SORT_DIR = SortDir.ASC;

	/**
	 * The column name
	 */
	private String column;
	/**
	 * The sort direction: ascending or descending
	 */
	private SortDir direction = DEFAULT_SORT_DIR;
	/**
	 * Ignore upper/lower case when column is text-based?
	 */
	private Boolean ignoreCase = DEFAULT_IGNORE_CASE;

	public SortColumn() {
		super();
	}

	public SortColumn(String column) {
		this();
		setColumn(column);
	}

	public SortColumn(String column, SortDir direction) {
		this();
		setColumn(column);
		setDirection(direction);
	}

	public SortColumn(String column, Boolean ignoreCase) {
		this();
		setColumn(column);
		setIgnoreCase(ignoreCase);
	}

	public SortColumn(String column, SortDir direction, Boolean ignoreCase) {
		this();
		setColumn(column);
		setDirection(direction);
		setIgnoreCase(ignoreCase);
	}

	public String descriptor() {
		return toString();
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
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
	public final boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj instanceof SortColumn == false) {
			return false;
		}

		final SortColumn that = (SortColumn) obj;

		boolean rval = (column != null && column.equals(that.column) && direction == that.direction);

		if(rval) {
			rval = (ignoreCase == null) ? that.ignoreCase == null : ignoreCase.equals(that.ignoreCase);
		}

		return rval;
	}

	@Override
	public final int hashCode() {
		int hash = 1;
		if(column != null) {
			hash += 31 * column.hashCode();
		}
		hash += direction.hashCode();
		hash += ignoreCase == null ? 0 : ignoreCase.booleanValue() ? 1 : 0;
		return hash;
	}

	public SortColumn copy() {
		return new SortColumn(column, direction, (this.ignoreCase == null ? null : new Boolean(this.ignoreCase
				.booleanValue())));
	}

	/**
	 * @return SQL valid order by sub-clause.
	 *         <p>
	 *         NOTE: Does NOT account for the ignoreCase flag.
	 */
	@Override
	public String toString() {
		return getColumn() + " " + (getDirection() == SortDir.ASC ? "asc" : "desc");
	}
}
