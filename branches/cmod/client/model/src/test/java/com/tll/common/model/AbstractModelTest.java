/**
 * The Logic Lab
 * @author jpk Dec 18, 2007
 */
package com.tll.common.model;

import org.testng.annotations.BeforeClass;

import com.tll.AbstractInjectedTest;

/**
 * AbstractModelTest - Base class for testing client model stuff.
 * @author jpk
 */
public abstract class AbstractModelTest extends AbstractInjectedTest {

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}
}
