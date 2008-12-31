/*
 * The Logic Lab 
 */
package com.tll.dao;

import javax.persistence.PersistenceException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.TestUtils;
import com.tll.model.INamedEntity;

/**
 * NamedEntityDaoTest
 * @author jpk
 */
public abstract class NamedEntityDaoTest<N extends INamedEntity> extends AbstractDaoTest<N> {

	/**
	 * Constructor
	 * @param entityClass
	 * @param daoClass
	 */
	public NamedEntityDaoTest(Class<N> entityClass, Class<? extends IEntityDao<? super N>> daoClass) {
		super(entityClass, daoClass);
	}

	/**
	 * Constructor
	 * @param entityClass
	 * @param daoClass
	 * @param testPagingRelated
	 */
	public NamedEntityDaoTest(Class<N> entityClass, Class<? extends IEntityDao<? super N>> daoClass,
			boolean testPagingRelated) {
		super(entityClass, daoClass, testPagingRelated);
	}

	/**
	 * Tests for expected exception throw for field too long case
	 * @throws Exception
	 */
	@Test(groups = "dao")
	public final void testFieldMaxLenghtExceeded() throws Exception {
		// as it stands now we don't have a schema check for mock daos
		if(daoMode == DaoMode.MOCK) {
			logger.warn("Not checking field max length for mock daos");
			return;
		}
		final N e = getTestEntity();
		e.setName(TestUtils.LENGTH_257_STRING);
		try {
			dao.persist(e);
			Assert.fail("Expected runtime exception didn't occur");
		}
		catch(final PersistenceException pe) {
			// expected
		}
	}

	@Override
	protected void verifyLoadedEntityState(N e) throws Exception {
		Assert.assertNotNull(e.getName(), "Named entities' name is null");
	}

	@Override
	protected void alterEntity(N e) {
		e.setName("altered");
	}

	@Override
	protected void verifyEntityAlteration(N e) throws Exception {
		Assert.assertTrue("altered".equals(e.getName()), "Named entity alteration does not match");
	}

}
