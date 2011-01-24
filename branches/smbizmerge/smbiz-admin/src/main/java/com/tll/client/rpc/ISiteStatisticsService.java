package com.tll.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tll.common.data.Payload;
import com.tll.common.dto.SiteStatisticsDto;

/**
 * @author jon.kirton
 */
@RemoteServiceRelativePath(value = "ss")
public interface ISiteStatisticsService extends RemoteService {

	public class SiteStatisticsPayload extends Payload {

		private SiteStatisticsDto dto;

		public SiteStatisticsPayload(SiteStatisticsDto dto) {
			super();
			this.dto = dto;
		}

		public SiteStatisticsDto getDto() {
			return dto;
		}

		public void setDto(SiteStatisticsDto dto) {
			this.dto = dto;
		}

	}

	SiteStatisticsPayload getSiteStatitics();
}
