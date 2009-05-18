/**
 * 
 */
package com.tll.dao.orm;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.IPCHDao;

/**
 * PCHDao
 * @author jpk
 */
public class PCHDao extends EntityDao implements IPCHDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 */
	@Inject
	public PCHDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler) {
		super(emPrvdr, dbDialectHandler);
	}

	public List<Integer> getPath(int categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void assign(int parentId, int childId) {
		// TODO Auto-generated method stub

	}

	public void assignChildren(int categoryId, Collection<Integer> children) {
		// TODO Auto-generated method stub

	}

	public void assignParents(int categoryId, Collection<Integer> parents) {
		// TODO Auto-generated method stub

	}

	public void clearChildren(int categoryId) {
		// TODO Auto-generated method stub

	}

	public void clearParents(int categoryId) {
		// TODO Auto-generated method stub

	}

	public List<Integer> findChildCategoryIds(int categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> findParentCategoryIds(int categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> findTopLevelCategoryIds(int accouuntId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeChildren(int categoryId, Collection<Integer> children) {
		// TODO Auto-generated method stub

	}

	public void removeParents(int categoryId, Collection<Integer> parents) {
		// TODO Auto-generated method stub

	}
}
