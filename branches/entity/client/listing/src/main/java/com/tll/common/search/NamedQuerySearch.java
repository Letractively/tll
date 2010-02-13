/**
 * The Logic Lab
 * @author jpk
 * @since May 18, 2009
 */
package com.tll.common.search;

import java.util.ArrayList;
import java.util.List;

import com.tll.common.model.IEntityType;
import com.tll.criteria.CriteriaType;
import com.tll.model.schema.IQueryParam;
import com.tll.util.StringUtil;

/**
 * NamedQuerySearch
 * @author jpk
 */
public class NamedQuerySearch implements INamedQuerySearch {

	private IEntityType entityType;
	private String queryName;
	private boolean scalar;
	private ArrayList<IQueryParam> params;

	/**
	 * Constructor
	 */
	public NamedQuerySearch() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType
	 * @param queryName
	 * @param scalar
	 */
	public NamedQuerySearch(IEntityType entityType, String queryName, boolean scalar) {
		super();
		this.entityType = entityType;
		this.queryName = queryName;
		this.scalar = scalar;
	}

	public void clear() {
		queryName = null;
	}

	public void addParam(IQueryParam param) {
		if(params == null) params = new ArrayList<IQueryParam>();
		params.add(param);
	}

	@Override
	public String getQueryName() {
		return queryName;
	}

	@Override
	public List<IQueryParam> getQueryParams() {
		return params;
	}

	@Override
	public CriteriaType getCriteriaType() {
		return scalar ? CriteriaType.SCALAR_NAMED_QUERY : CriteriaType.ENTITY_NAMED_QUERY;
	}

	@Override
	public boolean isSet() {
		return entityType != null && !StringUtil.isEmpty(queryName) && params.size() > 0;
	}

	@Override
	public IEntityType getEntityType() {
		return entityType;
	}

}
