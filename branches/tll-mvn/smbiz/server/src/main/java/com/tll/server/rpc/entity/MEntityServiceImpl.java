/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;

import com.tll.SystemError;
import com.tll.client.data.EntityFetchPrototypeRequest;
import com.tll.client.data.EntityLoadRequest;
import com.tll.client.data.EntityOptions;
import com.tll.client.data.EntityPayload;
import com.tll.client.data.EntityPersistRequest;
import com.tll.client.data.EntityPurgeRequest;
import com.tll.client.data.RemoteListingDefinition;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.msg.Msg.MsgAttr;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.search.ISearch;
import com.tll.criteria.Criteria;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.model.EntityType;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.PrimaryKey;
import com.tll.server.RequestContext;
import com.tll.server.ServletUtil;
import com.tll.server.rpc.AuxDataHandler;
import com.tll.server.rpc.listing.IMarshalingListHandler;
import com.tll.server.rpc.listing.MarshalingListHandler;
import com.tll.server.rpc.listing.PropKeyListHandler;
import com.tll.service.entity.IEntityService;

/**
 * MEntityServiceImpl - Provides base methods for CRUD ops on entities.
 * @author jpk
 * @param <E>
 * @param <S>
 */
public abstract class MEntityServiceImpl<E extends IEntity, S extends ISearch> implements IMEntityServiceImpl<E, S> {

	/**
	 * Loads additional entity properties.
	 * @param e
	 * @param options
	 * @param refs The map to place related entity references ({@link RefKey}) if
	 *        they are reuested in <code>options</code>.
	 * @param requestContext The request context.
	 * @throws SystemError When any error occurrs.
	 */
	protected abstract void handleLoadOptions(RequestContext requestContext, E e, EntityOptions options,
			Map<String, RefKey> refs) throws SystemError;

	/**
	 * Handles persist options specified in options.
	 * @param e
	 * @param options
	 * @param requestContext The request context.
	 * @throws SystemError When any error occurrs.
	 */
	protected abstract void handlePersistOptions(RequestContext requestContext, E e, EntityOptions options)
			throws SystemError;

	public final void getEmptyEntity(final RequestContext requestContext, final EntityFetchPrototypeRequest request,
			final EntityType entityType, final EntityPayload payload) {
		try {
			final IEntity e =
					requestContext.getEntityFactory().createEntity(EntityUtil.entityClassFromType(entityType),
							request.isGenerate());
			final Model group = requestContext.getMarshaler().marshalEntity(e, getMarshalOptions(requestContext));
			payload.setEntity(group);
		}
		catch(final SystemError se) {
			ServletUtil.handleException(requestContext, payload.getStatus(), se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			ServletUtil.handleException(requestContext, payload.getStatus(), re, re.getMessage(), true);
			throw re;
		}
	}

	/**
	 * Does the core entity loading.
	 * @param requestContext
	 * @param request
	 * @param entityType
	 * @param payload
	 * @return The loaded {@link IEntity}
	 */
	@SuppressWarnings("unchecked")
	protected E coreLoad(final RequestContext requestContext, final EntityLoadRequest request,
			final EntityType entityType, final EntityPayload payload) {
		// core entity loading
		final Class<E> entityClass = EntityUtil.entityClassFromType(entityType);
		final IEntityService<E> svc = requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);

		if(request.isLoadByBusinessKey()) {
			// load by business key
			S search = (S) request.getSearch();
			if(search == null) {
				payload.getStatus().addMsg("A business key wise search must be specified.", MsgLevel.ERROR);
				return null;
			}

			BusinessKey key = handleBusinessKeyTranslation(search);

			return svc.load(key);
		}

		// load by primary key
		final Integer id = request.getEntityRef().getId();
		return svc.load(new PrimaryKey(entityClass, id));
	}

	public final void load(final RequestContext requestContext, final EntityLoadRequest request,
			final EntityType entityType, final EntityPayload payload) {
		try {
			final E e = coreLoad(requestContext, request, entityType, payload);
			if(e == null) {
				return;
			}

			// optional loading
			final Map<String, RefKey> refs = new HashMap<String, RefKey>();
			if(request.entityOptions != null) {
				handleLoadOptions(requestContext, e, request.entityOptions, refs);
			}

			// marshal the loaded entity
			final Model group = requestContext.getMarshaler().marshalEntity(e, getMarshalOptions(requestContext));
			payload.setEntity(group);

			// set any entity refs
			for(final String propName : refs.keySet()) {
				payload.setRelatedOneRef(propName, refs.get(propName));
			}

			// load any requested auxiliary
			if(request.auxDataRequest != null) {
				AuxDataHandler.getAuxData(requestContext, request.auxDataRequest, payload);
			}
		}
		catch(final EntityNotFoundException enfe) {
			ServletUtil.handleException(requestContext, payload.getStatus(), enfe, enfe.getMessage(), false);
		}
		catch(final SystemError se) {
			ServletUtil.handleException(requestContext, payload.getStatus(), se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			ServletUtil.handleException(requestContext, payload.getStatus(), re, re.getMessage(), true);
			throw re;
		}
	}

	public final void persist(final RequestContext requestContext, final EntityPersistRequest request,
			final EntityType entityType, final EntityPayload payload) {
		try {
			// core persist
			final Class<E> entityClass = EntityUtil.entityClassFromType(entityType);
			final Model entity = request.getEntity();
			E e = requestContext.getMarshaler().unmarshalEntity(entityClass, entity);
			final IEntityService<E> svc = requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);
			e = svc.persist(e);

			// handle persist options
			handlePersistOptions(requestContext, e, request.entityOptions);

			// marshall
			final Model group = requestContext.getMarshaler().marshalEntity(e, getMarshalOptions(requestContext));
			payload.setEntity(group);

			payload.getStatus().addMsg(e.descriptor() + " persisted.", MsgLevel.INFO);
		}
		catch(final EntityExistsException e1) {
			ServletUtil.handleException(requestContext, payload.getStatus(), e1, e1.getMessage(), false);
		}
		catch(final InvalidStateException ise) {
			for(final InvalidValue iv : ise.getInvalidValues()) {
				payload.getStatus().addMsg(iv.getMessage(), MsgLevel.ERROR, MsgAttr.FIELD.flag, iv.getPropertyPath());

			}
		}
		catch(final SystemError se) {
			ServletUtil.handleException(requestContext, payload.getStatus(), se, null, true);
		}
		catch(final RuntimeException re) {
			ServletUtil.handleException(requestContext, payload.getStatus(), re, null, true);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public final void purge(final RequestContext requestContext, final EntityPurgeRequest entityRequest,
			final EntityType entityType, final EntityPayload p) {
		try {
			final Class<IEntity> entityClass = EntityUtil.entityClassFromType(entityType);
			final IEntityService<IEntity> svc = requestContext.getEntityServiceFactory().instanceByEntityType(entityClass);
			final RefKey entityRef = entityRequest.getEntityRef();
			if(entityRef == null || !entityRef.isSet()) {
				throw new EntityNotFoundException("A valid entity reference must be specified to purge an entity.");
			}
			final PrimaryKey pk = new PrimaryKey(entityClass, entityRef.getId());
			final IEntity e = svc.load(pk);
			svc.purge(e);

			p.setEntityRef(entityRef);
			p.getStatus().addMsg(e.descriptor() + " purged.", MsgLevel.INFO);
		}
		catch(final EntityNotFoundException e) {
			ServletUtil.handleException(requestContext, p.getStatus(), e, e.getMessage(), false);
		}
		catch(final SystemError se) {
			ServletUtil.handleException(requestContext, p.getStatus(), se, se.getMessage(), true);
		}
		catch(final RuntimeException re) {
			ServletUtil.handleException(requestContext, p.getStatus(), re, re.getMessage(), true);
			throw re;
		}
	}

	/**
	 * Translates {@link ISearch} to {@link BusinessKey}s.
	 * @param search The search to translate
	 * @return Translated {@link BusinessKey}
	 */
	protected abstract BusinessKey<E> handleBusinessKeyTranslation(S search);

	/**
	 * Handles the entity specific search to criteria translation.
	 * @param search
	 * @param criteria
	 * @throws IllegalArgumentException When the <code>search</code> parameter is
	 *         unsupported.
	 */
	protected abstract void handleSearchTranslation(RequestContext requestContext, S search,
			ICriteria<? extends E> criteria) throws IllegalArgumentException;

	public final ICriteria<? extends E> translate(final RequestContext requestContext, final EntityType entityType,
			final S search) throws IllegalArgumentException {
		final CriteriaType criteriaType = search.getCriteriaType();
		final Class<E> entityClass = EntityUtil.entityClassFromType(entityType);
		Criteria<? extends E> criteria;
		final Set<IQueryParam> queryParams = search.getQueryParams();

		if(criteriaType.isQuery()) {
			ISelectNamedQueryDef nq = search.getNamedQuery();
			if(nq == null) {
				throw new IllegalArgumentException("No named query specified");
			}
			criteria = new Criteria<E>(nq, queryParams);
		}
		else {
			// entity
			criteria = new Criteria<E>(entityClass);
			handleSearchTranslation(requestContext, search, criteria);
		}

		return criteria;
	}

	/*
	 * Sub-classes should override this method for specific table requirements
	 * based on the listing command particulars.
	 */
	public IMarshalingListHandler<E> getMarshalingListHandler(final RequestContext requestContext,
			final RemoteListingDefinition<S> listingDefinition) {
		if(listingDefinition.getPropKeys() != null) {
			return new PropKeyListHandler<E>(requestContext.getMarshaler(), getMarshalOptions(requestContext),
					listingDefinition.getPropKeys());
		}
		return new MarshalingListHandler<E>(requestContext.getMarshaler(), getMarshalOptions(requestContext));
	}
}
