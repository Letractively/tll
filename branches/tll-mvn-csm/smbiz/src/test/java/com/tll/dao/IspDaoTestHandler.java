/*
 * The Logic Lab 
 */
package com.tll.dao;

import com.tll.model.Isp;

/**
 * IspDaoTestHandler
 * @author jpk
 */
public class IspDaoTestHandler extends AbstractAccountDaoTestHandler<Isp> {

	@Override
	public Class<Isp> entityClass() {
		return Isp.class;
	}
}
