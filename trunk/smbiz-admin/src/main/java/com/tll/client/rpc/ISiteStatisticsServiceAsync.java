package com.tll.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ISiteStatisticsServiceAsync {

	void getSiteStatitics(AsyncCallback<com.tll.client.rpc.ISiteStatisticsService.SiteStatisticsPayload> callback);

	public static final class Util {

		private static ISiteStatisticsServiceAsync instance;

		public static final ISiteStatisticsServiceAsync instance() {
			if(instance == null) {
				instance = (ISiteStatisticsServiceAsync) GWT.create(ISiteStatisticsService.class);
			}
			return instance;
		}

		private Util() {}
	}
}
