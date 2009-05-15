/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.server.rpc.entity;

import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.ModelPayload;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.model.INamedEntity;
import com.tll.model.key.NameKey;
import com.tll.service.entity.INamedEntityService;

/**
 * MNamedEntityServiceImpl
 * @author jpk
 * @param <N>
 */
public abstract class MNamedEntityServiceImpl<N extends INamedEntity> extends MEntityServiceImpl<N> {

	@SuppressWarnings("unchecked")
	@Override
	protected N coreLoad(final MEntityContext context, final EntityLoadRequest request,
			final ModelPayload payload) {

		if(request.isLoadByName()) {
			// load by name
			final String name = request.getRef() == null ? null : request.getRef().getName();
			if(name == null) {
				payload.getStatus().addMsg("A name must be specified.", MsgLevel.ERROR, MsgAttr.STATUS.flag);
				return null;
			}
			final Class<N> entityClass = (Class<N>) EntityTypeUtil.getEntityClass(request.getEntityType());
			final INamedEntityService<N> namedEntityService =
				(INamedEntityService<N>) context.getEntityServiceFactory().instanceByEntityType(
						entityClass);
			return namedEntityService.load(new NameKey<N>(entityClass, name));
		}

		return super.coreLoad(context, request, payload);
	}
}
