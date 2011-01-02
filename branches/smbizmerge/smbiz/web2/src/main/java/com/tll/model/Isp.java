package com.tll.model;


/**
 * The ISP entity
 * @author jpk
 */
@Extended
public class Isp extends Account {

	private static final long serialVersionUID = -1666954465162270432L;

	@Override
	public Class<? extends IEntity> entityClass() {
		return Isp.class;
	}
}