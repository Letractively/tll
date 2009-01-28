/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao;

import java.util.Collection;

import com.tll.model.IEntity;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * SimpleEntityDaoTest
 * @author jpk
 */
public class SimpleEntityDaoTest extends AbstractEntityDaoTest {

	@SuppressWarnings("unchecked")
	@Override
	protected Collection<IEntityDaoTestHandler<IEntity>> getDaoTestHandlers() {
		return Arrays.asList(new IEntityDaoTestHandler[] { new TestEntityDaoTestHandler() });
	}
}
