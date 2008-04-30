/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.client.search;

/**
 * EntitySearch
 * @author jpk
 */
public abstract class EntitySearch extends SearchBase {

	/**
	 * Corres. to the string-wise representation of the EntityType enum.
	 */
	protected String entityType;

	/**
	 * Constructor
	 */
	public EntitySearch() {
		super();
	}

	/**
	 * Constructor
	 * @param searchType
	 * @param entityType
	 */
	public EntitySearch(int searchType, String entityType) {
		super(searchType);
		this.entityType = entityType;
	}

	public final String getEntityType() {
		return entityType;
	}

	public final void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	@Override
	public void clear() {
		super.clear();
		// no-op
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj) || obj instanceof EntitySearch == false) return false;
		final EntitySearch that = (EntitySearch) obj;
		return that.entityType != null && that.entityType.equals(this.entityType);
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		if(entityType != null) {
			hash = hash * 31 + entityType.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += " entityType:" + entityType;
		return s;
	}
}
