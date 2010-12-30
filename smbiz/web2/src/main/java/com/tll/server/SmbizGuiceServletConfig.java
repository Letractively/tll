/**
 * The Logic Lab
 * @author jpk
 * @since Dec 30, 2010
 */
package com.tll.server;

import java.util.HashMap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

/**
 * @author jpk
 */
public class SmbizGuiceServletConfig extends GuiceServletContextListener {
	
	static class SmbizServletModule extends ServletModule {

		@Override
		protected void configureServlets() {
			
			// NoSecuritySessionContextFilter
			filter("/*").through(NoSecuritySessionContextFilter.class);
			
			// WebClientCacheFilter
			HashMap<String, String> cparams = new HashMap<String, String>(1);
			cparams.put("oneDayCacheFileExts", ".js .css .gif .jpg .png");
			filter("/*").through(WebClientCacheFilter.class, cparams);
		}
		
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new SmbizServletModule());
	}

}
