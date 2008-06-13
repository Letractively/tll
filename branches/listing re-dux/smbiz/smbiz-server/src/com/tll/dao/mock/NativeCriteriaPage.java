/**
 * The Logic Lab
 * @author jpk Nov 20, 2007
 */
package com.tll.dao.mock;

import java.util.List;

import com.tll.criteria.ICriteria;
import com.tll.listhandler.Page;
import com.tll.listhandler.SearchResult;
import com.tll.model.IEntity;

/**
 * NativeCriteriaPage
 * @author jpk
 */
public final class NativeCriteriaPage<E extends IEntity> extends Page<SearchResult<E>> {

	private static final long serialVersionUID = 794902539980152610L;

	private ICriteria<? extends E> criteria;

	/**
	 * Constructor
	 * @param pageSize
	 * @param totalSize
	 * @param pageElements
	 * @param pageNumber
	 */
	public NativeCriteriaPage(int pageSize, int totalSize, List<SearchResult<E>> pageElements, int pageNumber) {
		super(pageSize, totalSize, pageElements, pageNumber);
	}

	public ICriteria<? extends E> getCriteria() {
		return criteria;
	}

	public void setCriteria(ICriteria<? extends E> criteria) {
		this.criteria = criteria;
	}

}
