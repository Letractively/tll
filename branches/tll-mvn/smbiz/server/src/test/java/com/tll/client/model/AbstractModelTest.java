/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.client.model;

import java.util.List;

import org.testng.annotations.BeforeClass;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.di.DaoModule;

/**
 * AbstractModelTest - Base class for testing client model stuff.
 * @author jpk
 */
public abstract class AbstractModelTest extends DbTest {

	/**
	 * Constructor
	 */
	public AbstractModelTest() {
		super(JpaMode.NONE);
	}

	@Override
	protected final void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new DaoModule(DaoMode.MOCK));
	}

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}
}
