package com.tll.dao;

import java.util.Collection;
import java.util.List;

import com.tll.dao.IDao;

/**
 * Strate JDBC access.
 * @author jpk
 */
public interface IPCHDao extends IDao {

	/**
	 * Retrieves all top-level bindings.
	 * @param accouuntId
	 * @return list
	 */
	List<Integer> findTopLevelCategoryIds(int accouuntId);

	/**
	 * Retrieves all child bindings.
	 * @param categoryId
	 * @return list
	 */
	List<Integer> findChildCategoryIds(int categoryId);

	/**
	 * Retrieves all parent bindings.
	 * @param categoryId
	 * @return list
	 */
	List<Integer> findParentCategoryIds(int categoryId);

	/**
	 * Retrieves the "tree path" for a "tree node"
	 * @param categoryId represents an arbitrary node
	 * @return the ordinal dependant node path in order of top to bottom
	 */
	List<Integer> getPath(int categoryId);

	/**
	 * Assign a single parent/child binding.
	 * @param parentId the parent category id
	 * @param childId the child category id
	 */
	void assign(int parentId, int childId);

	/**
	 * Binds a category id to one or more category ids as parents.
	 * @param categoryId the subject category id
	 * @param parents the parent category ids
	 */
	void assignParents(int categoryId, Collection<Integer> parents);

	/**
	 * Binds a category id to one or more category ids as children.
	 * @param categoryId the subject category id
	 * @param children the child category ids
	 */
	void assignChildren(int categoryId, Collection<Integer> children);

	/**
	 * Removes the given parents from a category.
	 * @param categoryId the subject category id
	 * @param parents the parent category ids
	 */
	void removeParents(int categoryId, Collection<Integer> parents);

	/**
	 * Removes the given children from the category.
	 * @param categoryId the subject category id
	 * @param children the child category ids
	 */
	void removeChildren(int categoryId, Collection<Integer> children);

	/**
	 * Clears all parents from a given category.
	 * @param categoryId
	 */
	void clearParents(int categoryId);

	/**
	 * Clears all children from a given category.
	 * @param categoryId
	 */
	void clearChildren(int categoryId);
}