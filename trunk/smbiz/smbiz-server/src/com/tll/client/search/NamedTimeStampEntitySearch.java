/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.client.search;

/**
 * TimeStampEntitySearch
 * @author jpk
 */
public abstract class NamedTimeStampEntitySearch extends TimeStampEntitySearch {

	protected String name;

	/**
	 * Constructor
	 */
	public NamedTimeStampEntitySearch() {
		super();
	}

	/**
	 * Constructor
	 * @param searchType
	 * @param entityType
	 */
	public NamedTimeStampEntitySearch(int searchType, String entityType) {
		super(searchType, entityType);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void clear() {
		super.clear();
		name = null;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj) || obj instanceof NamedTimeStampEntitySearch == false) return false;
		final NamedTimeStampEntitySearch that = (NamedTimeStampEntitySearch) obj;
		if(that.name == null) {
			if(name != null) return false;
		}
		else {
			if(name == null || !name.equals(that.name)) return false;

		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		if(name != null) {
			hash = hash * 25 + name.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += " name:" + name;
		return s;
	}
}
