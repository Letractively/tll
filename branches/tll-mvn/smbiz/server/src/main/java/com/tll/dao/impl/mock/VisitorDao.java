/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IVisitorDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.Visitor;

public class VisitorDao extends EntityDao<Visitor> implements IVisitorDao, IMockDao<Visitor> {

	@Inject
	public VisitorDao(Set<Visitor> set) {
		super(Visitor.class, set);
	}
}