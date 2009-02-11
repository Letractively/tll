/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.model.EntityType;
import com.tll.model.EntityTypeUtil;
import com.tll.model.INamedEntity;
import com.tll.model.key.NameKey;
import com.tll.server.RequestContext;
import com.tll.service.entity.INamedEntityService;

/**
 * MNamedEntityServiceImpl
 * @author jpk
 * @param <N>
 * @param <S>
 */
public abstract class MNamedEntityServiceImpl<N extends INamedEntity, S extends ISearch> extends MEntityServiceImpl<N, S> {

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
			final Class<N> entityClass = EntityTypeUtil.entityClassFromType(entityType);
			final INamedEntityService<N> namedEntityService =
					(INamedEntityService<N>) requestContext.getAppContext().getEntityServiceFactory().instanceByEntityType(
							entityClass);
			return namedEntityService.load(new NameKey<N>(entityClass, name));
		}

		return super.coreLoad(requestContext, request, entityType, payload);
	}
}