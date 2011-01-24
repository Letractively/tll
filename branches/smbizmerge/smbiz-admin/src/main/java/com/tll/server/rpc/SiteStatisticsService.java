package com.tll.server.rpc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tll.client.rpc.ISiteStatisticsService;
import com.tll.common.dto.SiteStatisticsDto;
import com.tll.server.PersistContext;

/**
 * @author jon.kirton
 */
@Singleton
public class SiteStatisticsService extends RpcServlet implements ISiteStatisticsService {
	
	private static final long serialVersionUID = 1L;
	
	private final PersistContext persistContext;

	/**
	 * Constructor
	 */
	@Inject
	public SiteStatisticsService(PersistContext persistContext) {
		super();
		this.persistContext = persistContext;
	}

	@Override
	public SiteStatisticsPayload getSiteStatitics() {
		com.tll.service.entity.SiteStatisticsService sss = persistContext.getEntityServiceFactory().instance(com.tll.service.entity.SiteStatisticsService.class);
		SiteStatisticsDto dto = new SiteStatisticsDto();
		dto.numAddresses = sss.numAddresses();
		dto.numIsps = sss.numIsps();
		dto.numMerchants = sss.numMerchants();
		dto.numCustomers = sss.numCustomers();
		dto.numUsers = sss.numUsers();
		dto.numAddresses = sss.numAddresses();
		return new SiteStatisticsPayload(dto);
	}

}
