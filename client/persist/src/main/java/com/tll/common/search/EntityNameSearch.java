/**
 * The Logic Lab
 * @author jpk
 * @since May 18, 2009
 */
package com.tll.common.search;

import com.tll.common.model.IEntityTypeProvider;
import com.tll.util.StringUtil;

/**
 * EntityNameSearch
 * @author jpk
 */
public class EntityNameSearch extends SearchBase implements IEntityTypeProvider {

	private String entityType;
	private String name;

	/**
	 * Constructor
	 */
	public EntityNameSearch() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType
	 * @param name
	 */
	public EntityNameSearch(String entityType, String name) {
		super();
		this.entityType = entityType;
		this.name = name;
	}

	@Override
	public String getEntityType() {
		return entityType;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isSet() {
		return entityType != null && !StringUtil.isEmpty(name);
	}

}
