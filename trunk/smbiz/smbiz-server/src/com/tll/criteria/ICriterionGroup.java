/*
 * The Logic Lab 
 */
package com.tll.criteria;

import java.util.Collection;

/**
 * ICriterionGroup
 * @author jpk
 */
public interface ICriterionGroup extends ICriterion, Iterable<ICriterion> {

	/**
	 * Is this entityGroup of criterion a junction (ANDed together) or a
	 * dis-junction (ORed together)
	 * @return true/false
	 */
	boolean isConjunction();

	/**
	 * Sets the junction property (AND/OR)
	 * @param isConjunction
	 */
	void setJunction(boolean isConjunction);

	/**
	 * Adds an {@link ICriterion} to the entityGroup.
	 * @param ctn the criterion
	 * @return {@link ICriterionGroup} this instance for easy method chaining
	 */
	ICriterionGroup addCriterion(ICriterion ctn);

	/**
	 * Adds a {@link Collection} of {@link ICriterion}
	 * @param clc the {@link ICriterion} collection
	 * @return {@link ICriterionGroup} this instance for easy method chaining
	 */
	ICriterionGroup addCriterion(Collection<ICriterion> clc);

	/**
	 * Removes a {@link ICriterion} to the entityGroup.
	 * @param ctn the property name of the desired {@link ICriterion}.
	 */
	ICriterionGroup removeCriterion(ICriterion ctn);

	/**
	 * @return the number of referenced {@link ICriterion} in this entityGroup
	 */
	int size();
}
