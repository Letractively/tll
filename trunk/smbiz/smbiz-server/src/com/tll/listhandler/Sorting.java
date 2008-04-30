package com.tll.listhandler;

import com.tll.client.IMarshalable;

/**
 * Construct representing a hierarchical sorting directive.
 * @author jpk
 */
public class Sorting implements IMarshalable {

	static final long serialVersionUID = 4943672060530931835L;

	private SortColumn[] columns;

	public Sorting() {
		super();
	}

	public String descriptor() {
		return "Sorting";
	}

	public Sorting(SortColumn[] columns) {
		this();
		setColumns(columns);
	}

	public Sorting(String primarySortColumnName, SortDir direction, Boolean ignoreCase) {
		super();
		getPrimarySortColumn().setColumn(primarySortColumnName);
		getPrimarySortColumn().setDirection(direction);
		getPrimarySortColumn().setIgnoreCase(ignoreCase);
	}

	public Sorting(String primarySortColumnName, SortDir direction) {
		super();
		getPrimarySortColumn().setColumn(primarySortColumnName);
		getPrimarySortColumn().setDirection(direction);
	}

	public Sorting(String primarySortColumnName, Boolean ignoreCase) {
		this(primarySortColumnName, SortDir.ASC, ignoreCase);
	}

	public Sorting(String primarySortColumnName) {
		super();
		getPrimarySortColumn().setColumn(primarySortColumnName);
		getPrimarySortColumn().setDirection(SortDir.ASC);
	}

	public int size() {
		return columns == null ? 0 : columns.length;
	}

	public void setColumns(SortColumn[] columns) {
		if(columns == null || columns.length < 1)
			throw new IllegalArgumentException("Unable to set sorting columns: null or empty columns array specified");
		this.columns = columns;
	}

	public SortColumn[] getColumns() {
		return columns;
	}

	private SortColumn[] safeGetColumns() {
		if(columns == null) {
			columns = new SortColumn[] { new SortColumn() };
		}
		return columns;
	}

	public SortColumn getPrimarySortColumn() {
		return safeGetColumns()[0];
	}

	public void setPrimarySortColumn(SortColumn column) {
		safeGetColumns()[0] = column;
	}

	@Override
	public final boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || obj instanceof Sorting == false) return false;
		final Sorting that = (Sorting) obj;
		if(this.size() != that.size() || this.size() < 1) return false;
		for(int i = 0; i < this.size(); i++) {
			if(!this.columns[i].equals(that.columns[i])) return false;
		}
		return true;
	}

	@Override
	public final int hashCode() {
		int hash = 1;
		if(columns != null && columns.length > 0) {
			for(final SortColumn element : columns) {
				hash += element.hashCode();
			}
		}
		return hash;
	}

	/**
	 * @return SQL valid order by clause.
	 * @see SortColumn#toString()
	 */
	@Override
	public String toString() {
		if(size() < 1) return "";
		final StringBuffer sb = new StringBuffer();
		for(int i = 0; i < columns.length; i++) {
			sb.append(columns[i].toString());
			if(i < columns.length - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public Sorting copy() {
		final Sorting cln = new Sorting();

		if(this.columns != null) {
			cln.columns = new SortColumn[this.columns.length];
			for(int i = 0; i < this.columns.length; i++) {
				cln.columns[i] = this.columns[i].copy();
			}
		}

		return cln;
	}
}
