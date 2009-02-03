/*
 * The Logic Lab 
 */
package com.tll.dao;

import java.util.Iterator;
import java.util.Set;

import org.testng.Assert;

import com.tll.model.Interface;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.mock.MockEntityFactory;

/**
 * AbstractInterfaceDaoTestHandler
 * @param <I> interface type
 * @author jpk
 */
public abstract class AbstractInterfaceDaoTestHandler<I extends Interface> extends AbstractEntityDaoTestHandler<I> {

	protected InterfaceOption removedOption;
	protected InterfaceOptionParameterDefinition removedParam;

	/*
	 * Interfaces don't support paging.
	 */
	@Override
	public boolean supportsPaging() {
		return false;
	}

	@Override
	public void assembleTestEntity(I e) throws Exception {

		final InterfaceOption o1 = create(InterfaceOption.class, false);
		Set<InterfaceOptionParameterDefinition> params =
				getAll(InterfaceOptionParameterDefinition.class);
		o1.addParameters(params);
		e.addOption(o1);

		final InterfaceOption o2 = create(InterfaceOption.class, false);
		params = getAll(InterfaceOptionParameterDefinition.class);
		o2.addParameters(params);
		e.addOption(o2);
	}

	@Override
	public void makeUnique(I e) {
		super.makeUnique(e);
		for(final InterfaceOption o : e.getOptions()) {
			MockEntityFactory.makeBusinessKeyUnique(o);
			for(final InterfaceOptionParameterDefinition param : o.getParameters()) {
				MockEntityFactory.makeBusinessKeyUnique(param);
			}
		}
	}

	@Override
	public void verifyLoadedEntityState(I e) throws Exception {
		super.verifyLoadedEntityState(e);

		Assert.assertNotNull(e.getCode(), "Interface code is null");
		Assert.assertNotNull(e.getOptions(), "Interface options is null");
		Assert.assertTrue(e.getOptions().size() > 0, "Interface options is empty");
		for(final InterfaceOption o : e.getOptions()) {
			if(o.getParameters() != null && o.getParameters().size() > 0) {
				for(final InterfaceOptionParameterDefinition param : o.getParameters()) {
					Assert.assertNotNull("param name is empty", param.getName());
				}
			}
		}
	}

	@Override
	public void alterTestEntity(I e) {
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
	public void verifyEntityAlteration(I e) throws Exception {
		Assert.assertNull(e.getOption(removedOption.getId()), "Orphaned option still persists");
		final InterfaceOption o = e.getOptions().iterator().next();
		for(final InterfaceOptionParameterDefinition p : o.getParameters()) {
			if(p.equals(removedParam)) {
				Assert.fail("The removed [orphan check] parameter still exists");
			}
		}
	}

}
