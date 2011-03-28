package com.tll.server;

import java.util.HashMap;

import com.google.inject.servlet.ServletModule;
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

		// NoSecuritySessionContextFilter
		filter("/*").through(NoSecuritySessionContextFilter.class);

		// WebClientCacheFilter
		HashMap<String, String> cparams = new HashMap<String, String>(1);
		cparams.put("oneDayCacheFileExts", "prod".equals(stage)? ".js .css .gif .jpg .png" : "");
		filter("/*").through(WebClientCacheFilter.class, cparams);

		// site statistics rpc service
		serve("/SmbizAdmin/ss").with(SiteStatisticsService.class);
	}

}