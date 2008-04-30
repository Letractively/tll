/*
 * The Logic Lab 
 */
package com.tll.criteria;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.SystemError;

/**
 * CriterionGroup
 * @author jpk
 */
public class CriterionGroup implements ICriterionGroup {

	private static final long serialVersionUID = 502701212641369513L;

	boolean junction = true; // default to AND junction

	Set<ICriterion> group;

	/**
	 * Constructor
	 */
	public CriterionGroup() {
		super();
	}

	/**
	 * Constructor
	 * @param isConjunction
	 */
	public CriterionGroup(boolean isConjunction) {
		this();
		this.junction = isConjunction;
	}

	public boolean isGroup() {
		return true;
	}

	public Set<ICriterion> getGroup() {
		return group;
	}

	public boolean isConjunction() {
		return junction;
	}

	public void setJunction(boolean junction) {
		this.junction = junction;
	}

	private Set<ICriterion> getGroupInternal() {
		if(group == null) {
			group = new HashSet<ICriterion>(3);
		}
		return group;
	}

	public ICriterionGroup addCriterion(ICriterion ctn) {
		getGroupInternal().add(ctn);
		return this;
	}

	public ICriterionGroup addCriterion(Collection<ICriterion> clc) {
		getGroupInternal().addAll(clc);
		return this;
	}

	public ICriterionGroup removeCriterion(ICriterion ctn) {
		group.remove(ctn);
		return this;
	}

	public Iterator<ICriterion> iterator() {
		return group == null ? null : group.iterator();
	}

	public void clear() {
		if(group != null) {
			group.clear();
		}
	}

	public boolean isSet() {
		if(group == null || group.size() < 1) {
			return false;
		}
		for(final ICriterion c : group) {
			if(!c.isSet()) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected CriterionGroup clone() {
		try {
			final CriterionGroup cln = (CriterionGroup) super.clone();
			if(group != null && group.size() > 0) {
				cln.group = new HashSet<ICriterion>();
				for(final ICriterion c : group) {
					cln.group.add(c.copy());
				}
			}
			return cln;
		}
		catch(final CloneNotSupportedException e) {
			throw new SystemError("Can't clone a CriterionGroup! (Sould not happen");
		}
	}

	public ICriterion copy() {
		return clone();
	}

	public Comparator getComparator() {
		throw new UnsupportedOperationException("ICriterionGroup does not support comparators.");
	}

	public String getField() {
		throw new UnsupportedOperationException("ICriterionGroup does not provide field names.");
	}

	public String getPropertyName() {
		throw new UnsupportedOperationException("ICriterionGroup does not provide property names.");
	}

	public Object getValue() {
		throw new UnsupportedOperationException("ICriterionGroup does not provide field values.");
	}

	public boolean isCaseSensitive() {
		throw new UnsupportedOperationException("ICriterionGroup does not support case sensitive property.");
	}

	public int size() {
		return group == null ? 0 : group.size();
	}

}
