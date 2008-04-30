/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.server.rpc.entity;

import com.tll.client.data.EntityLoadRequest;
import com.tll.client.data.EntityPayload;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.model.EntityUtil;
import com.tll.model.INamedEntity;
import com.tll.model.impl.EntityType;
import com.tll.model.key.KeyFactory;
import com.tll.server.RequestContext;
import com.tll.service.entity.INamedEntityService;

/**
 * MNamedEntityServiceImpl
 * @author jpk
 */
public abstract class MNamedEntityServiceImpl<N extends INamedEntity> extends MEntityServiceImpl<N> {

	@Override
	protected N coreLoad(final RequestContext requestContext, final EntityLoadRequest request,
			final EntityType entityType, final EntityPayload payload) {

		if(request.isLoadByName()) {
			// load by name
			final String name = request.getEntityRef() == null ? null : request.getEntityRef().getName();
			if(name == null) {
				payload.getStatus().addMsg("A name must be specified.", MsgLevel.ERROR);
				return null;
			}
			final Class<N> entityClass = EntityUtil.entityClassFromType(entityType);
			final INamedEntityService<N> namedEntityService =
					(INamedEntityService<N>) requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);
			return namedEntityService.load(KeyFactory.getNameKey(entityClass, name));
		}

		return super.coreLoad(requestContext, request, entityType, payload);
	}
}
