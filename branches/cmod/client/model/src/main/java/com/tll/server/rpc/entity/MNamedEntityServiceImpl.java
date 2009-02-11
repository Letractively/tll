/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityPayload;
import com.tll.common.model.IEntityType;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.model.INamedEntity;
import com.tll.model.key.NameKey;
import com.tll.service.entity.INamedEntityService;

/**
 * MNamedEntityServiceImpl
 * @author jpk
 * @param <N>
 * @param <S>
 */
public abstract class MNamedEntityServiceImpl<N extends INamedEntity, S extends ISearch> extends MEntityServiceImpl<N, S> {

	@SuppressWarnings("unchecked")
	@Override
	protected N coreLoad(final IMEntityServiceContext context, final EntityLoadRequest request,
			final IEntityType entityType, final EntityPayload payload) {

		if(request.isLoadByName()) {
			// load by name
			final String name = request.getEntityRef() == null ? null : request.getEntityRef().getName();
			if(name == null) {
				payload.getStatus().addMsg("A name must be specified.", MsgLevel.ERROR);
				return null;
			}
			final Class<N> entityClass = (Class<N>) EntityTypeUtil.getEntityClass(entityType);
			final INamedEntityService<N> namedEntityService =
					(INamedEntityService<N>) context.getEntityServiceFactory().instanceByEntityType(
							entityClass);
			return namedEntityService.load(new NameKey<N>(entityClass, name));
		}

		return super.coreLoad(context, request, entityType, payload);
	}
}
