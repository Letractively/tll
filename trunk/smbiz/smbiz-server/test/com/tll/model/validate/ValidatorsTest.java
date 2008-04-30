/**
 * The Logic Lab
 */
package com.tll.model.validate;

import java.util.List;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.SystemError;
import com.tll.TestBase;
import com.tll.dao.DaoMode;
import com.tll.dao.IDaoFactory;
import com.tll.dao.IEntityDao;
import com.tll.guice.DaoModule;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.Address;
import com.tll.util.CommonUtil;

/**
 * AbstractValidatorTest
 * @author jpk
 */
@Test(groups = "model.validate")
public class ValidatorsTest extends TestBase {

	/**
	 * Constructor
	 */
	public ValidatorsTest() {
		super();
	}

	@Override
	protected void beforeClass() {
		super.beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new DaoModule(DaoMode.MOCK));
	}

	protected void assembleTestEntity(IEntity e) throws Exception {
		if(AccountAddress.class.equals(e.entityClass())) {
			// invalidate a nested property to verify property path
			final AccountAddress aa = (AccountAddress) e;
			final Address a = aa.getAddress();
			assert a != null;
			a.setPostalCode("invalid");
			a.setPhone("invalidp");
		}
	}

	private <E extends IEntity> E getTestEntity(Class<E> entityType) throws Exception {
		E e = null;
		final IDaoFactory df = injector.getInstance(IDaoFactory.class);
		try {
			final IEntityDao<E> dao = df.instanceByEntityType(entityType);
			final List<E> list = dao.loadAll();
			if(list != null && list.size() > 0) {
				int i = 0;
				do {
					e = list.get(i++);
				} while(!e.entityClass().equals(entityType) && i < list.size());
			}
		}
		catch(final SystemError se) {
			// assume we have an entity type that does not directly tie into a defined
			// dao (e.g. InterfaceOption).
		}
		if(e == null) {
			e = getMockEntityProvider().getEntityCopy(entityType);
		}
		assert e != null;
		assembleTestEntity(e);
		return e;
	}

	/**
	 * Test to make sure we have a validator for declared entities and validates a
	 * single mock entity for all entity types.
	 * @throws Exception
	 */
	@Test
	@SuppressWarnings("unchecked")
	public final void testValidators() throws Exception {
		final Class<? extends IEntity>[] entityClasses = CommonUtil.getClasses("com.tll.model", IEntity.class, true, null);
		for(final Class<? extends IEntity> entityClass : entityClasses) {
			logger.debug("*** Validating entity type: " + EntityUtil.typeName(entityClass) + " ***");
			try {
				final IEntity e = getTestEntity(entityClass);
				final IEntityValidator<IEntity> validator =
						(IEntityValidator<IEntity>) EntityValidatorFactory.instance(entityClass);
				assert validator != null;
				validator.validate(e);
			}
			catch(final InvalidStateException ise) {
				for(final InvalidValue em : ise.getInvalidValues()) {
					logger.debug("prop: " + em.getPropertyPath());
					logger.debug("msg: " + em.getMessage());
				}
			}
			logger.debug("****************");
			logger.debug("");
		}
	}

}
