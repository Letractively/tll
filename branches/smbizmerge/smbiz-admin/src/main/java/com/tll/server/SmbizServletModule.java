package com.tll.server;

import java.util.HashMap;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.tll.config.Config;
import com.tll.server.rpc.SiteStatisticsService;

/**
 * Encapsulates web.xml bindings.
 * @author jpk
 */
class SmbizServletModule extends ServletModule {

	private final Config config;

	/**
	 * Constructor
	 * @param config
	 */
	public SmbizServletModule(Config config) {
		super();
		if(config == null) throw new IllegalArgumentException();
		this.config = config;
	}

	@Override
	protected void configureServlets() {
		
		String stage = config.getString("stage");
		
		HashMap<String, String> cparams = new HashMap<String, String>();

		// NoSecuritySessionContextFilter
		filter("/*").through(NoSecuritySessionContextFilter.class);

		// WebClientCacheFilter
		cparams.clear();
		cparams.put("oneDayCacheFileExts", "prod".equals(stage)? ".js .css .gif .jpg .png" : "");
		filter("/*").through(WebClientCacheFilter.class, cparams);
		
		// gwt request factory
		bind(RequestFactoryServlet.class).in(Scopes.SINGLETON);
		cparams.clear();
		// You'll need to compile with -extras and move the symbolMaps directory to this location if you want stack trace deobfuscation to work
		cparams.put("symbolMapsDirectory", "WEB-INF/classes/symbolMaps/");
		serve("/SmbizAdmin/gwtRequest").with(RequestFactoryServlet.class, cparams);

		// site statistics rpc service
		serve("/SmbizAdmin/ss").with(SiteStatisticsService.class);
	}

}