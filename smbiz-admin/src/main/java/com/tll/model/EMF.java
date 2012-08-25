/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.model;

import com.google.inject.Inject;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * Needed to satisfy GWT's RequestFactory feature.
 * @author jpk
 */
public class EMF {

	@Inject
	private static IEntityServiceFactory esf;
	
	public static IEntityServiceFactory get() {
		return esf;
	}
}
