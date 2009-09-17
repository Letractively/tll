/**
 * The Logic Lab
 * @author jpk
 * Nov 19, 2007
 */
package com.tll.dao.db4o;

import java.util.Collection;
import java.util.List;

import com.db4o.ObjectContainer;
import com.google.inject.Inject;
import com.tll.dao.IPCHDao;


/**
 * PCHDao
 * TODO implement
 * @author jpk
 */
public class PCHDao extends Db4oEntityDao implements IPCHDao {

	/**
	 * Constructor
	 * @param oc
	 */
	@Inject
	public PCHDao(ObjectContainer oc) {
		super(oc);
	}

	public void assign(int parentId, int childId) {
	}

	public void assignChildren(int categoryId, Collection<Integer> children) {
	}

	public void assignParents(int categoryId, Collection<Integer> parents) {
	}

	public void clearChildren(int categoryId) {
	}

	public void clearParents(int categoryId) {
	}

	public List<Integer> findChildCategoryIds(int categoryId) {
		return null;
	}

	public List<Integer> findParentCategoryIds(int categoryId) {
		return null;
	}

	public List<Integer> findTopLevelCategoryIds(int accouuntId) {
		return null;
	}

	public List<Integer> getPath(int categoryId) {
		return null;
	}

	public void removeChildren(int categoryId, Collection<Integer> children) {
	}

	public void removeParents(int categoryId, Collection<Integer> parents) {
	}

}
