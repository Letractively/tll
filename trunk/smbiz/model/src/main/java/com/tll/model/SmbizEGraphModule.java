/**
 * The Logic Lab
 * @author jpk
 * @since Aug 30, 2009
 */
package com.tll.model;



/**
 * SmbizEGraphModule
 * @author jpk
 */
public class SmbizEGraphModule extends EGraphModule {

	public SmbizEGraphModule() {
		super(SmbizEntityGraphBuilder.class, "mock-entities.xml");
	}
}