/*
 * The Logic Lab 
 */
package com.tll.dao.impl;

import java.util.Iterator;

import org.testng.Assert;

import com.tll.dao.NamedEntityDaoTest;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.MockEntityProvider;
import com.tll.model.impl.Interface;
import com.tll.model.impl.InterfaceOption;
import com.tll.model.impl.InterfaceOptionParameterDefinition;

/**
 * AbstractInterfaceDaoTest
 * @author jpk
 */
public abstract class AbstractInterfaceDaoTest<I extends Interface> extends NamedEntityDaoTest<I> {

	protected InterfaceOption removedOption;
	protected InterfaceOptionParameterDefinition removedParam;

	/**
	 * Constructor
	 * <p>
	 * <strong>IMPT: </stong>we do NOT test paging related methods for interfaces
	 * as a left outer join is involved and we currently aren't overriding the
	 * stock dao paging methods for interfaces.
	 */
	public AbstractInterfaceDaoTest(Class<I> intfClass) {
		super(intfClass, IInterfaceDao.class, false);
	}

	@Override
	protected void assembleTestEntity(I e) throws Exception {

		final InterfaceOption o1 = getMockEntityProvider().getEntityCopy(InterfaceOption.class, 1);
		InterfaceOptionParameterDefinition param =
				getMockEntityProvider().getEntityCopy(InterfaceOptionParameterDefinition.class, 1);
		InterfaceOptionParameterDefinition param2 =
				getMockEntityProvider().getEntityCopy(InterfaceOptionParameterDefinition.class, 2);
		o1.addParameter(param);
		o1.addParameter(param2);
		e.addOption(o1);

		final InterfaceOption o2 = getMockEntityProvider().getEntityCopy(InterfaceOption.class, 2);
		param = getMockEntityProvider().getEntityCopy(InterfaceOptionParameterDefinition.class, 1);
		param2 = getMockEntityProvider().getEntityCopy(InterfaceOptionParameterDefinition.class, 2);
		o2.addParameter(param);
		o2.addParameter(param2);
		e.addOption(o2);
	}

	@Override
	protected void uniquify(I e) {
		super.uniquify(e);

		try {
			for(final InterfaceOption o : e.getOptions()) {
				MockEntityProvider.makeBusinessKeyUnique(o);
				for(final InterfaceOptionParameterDefinition param : o.getParameters()) {
					MockEntityProvider.makeBusinessKeyUnique(param);
				}
			}
		}
		catch(final BusinessKeyNotDefinedException ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Override
	protected void verifyLoadedEntityState(I e) throws Exception {
		super.verifyLoadedEntityState(e);

		Assert.assertNotNull(e.getCode(), "Interface code is null");
		Assert.assertNotNull(e.getOptions(), "Interface options is null");
		Assert.assertTrue(e.getOptions().size() > 0, "Interface options is empty");
		for(final InterfaceOption o : e.getOptions()) {
			Assert
					.assertTrue(o.getParameters() != null && o.getParameters().size() > 0, "Interface option has no parameters");
			for(final InterfaceOptionParameterDefinition param : o.getParameters()) {
				Assert.assertNotNull("param name is empty", param.getName());
			}
		}
	}

	@Override
	protected void alterEntity(I e) {
		final Iterator<InterfaceOption> itr = e.getOptions().iterator();

		final InterfaceOption o = itr.next();
		final InterfaceOption o2 = itr.next();

		e.removeOption(o);
		removedOption = o;

		final Iterator<InterfaceOptionParameterDefinition> itrp = o2.getParameters().iterator();
		final InterfaceOptionParameterDefinition p = itrp.next();
		final InterfaceOptionParameterDefinition p2 = itrp.next();
		o2.removeParameter(p);
		removedParam = p;
		p2.setDescription("updated");
	}

	@Override
	protected void verifyEntityAlteration(I e) throws Exception {
		Assert.assertNull(e.getOption(removedOption.getId()), "Orphaned option still persists");
		final InterfaceOption o = e.getOptions().iterator().next();
		for(final InterfaceOptionParameterDefinition p : o.getParameters()) {
			if(p.equals(removedParam)) {
				Assert.fail("The removed [orphan check] parameter still exists");
			}
		}
	}

}
