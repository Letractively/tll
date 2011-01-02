/**
 * The Logic Lab
 * @author jpk
 * Nov 19, 2007
 */
package com.tll.dao.db4o;

import java.util.Collection;
import java.util.List;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Inject;
import com.tll.dao.IPCHDao;
import com.tll.model.bk.BusinessKeyFactory;


/**
 * PCHDao
 * TODO implement
 * @author jpk
 */
public class PCHDao extends Db4oEntityDao implements IPCHDao {

	/**
	 * Constructor
	 * @param oc
	 * @param namedQueryTranslator
	 * @param businessKeyFactory
	 */
	@Inject
	public PCHDao(EmbeddedObjectContainer oc, IDb4oNamedQueryTranslator namedQueryTranslator, BusinessKeyFactory businessKeyFactory) {
		super(oc, namedQueryTranslator, businessKeyFactory);
	}

	@Override
	public void assign(int parentId, int childId) {
	}

	@Override
	public void assignChildren(int categoryId, Collection<Integer> children) {
	}

	@Override
	public void assignParents(int categoryId, Collection<Integer> parents) {
	}

	@Override
	public void clearChildren(int categoryId) {
	}

	@Override
	public void clearParents(int categoryId) {
	}

	@Override
	public List<Integer> findChildCategoryIds(int categoryId) {
		return null;
	}

	@Override
	public List<Integer> findParentCategoryIds(int categoryId) {
		return null;
	}

	@Override
	public List<Integer> findTopLevelCategoryIds(int accouuntId) {
		return null;
	}

	@Override
	public List<Integer> getPath(int categoryId) {
		return null;
	}

	@Override
	public void removeChildren(int categoryId, Collection<Integer> children) {
	}

	@Override
	public void removeParents(int categoryId, Collection<Integer> parents) {
	}

}
