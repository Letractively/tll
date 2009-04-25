/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.server.rpc.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.tll.SystemError;
import com.tll.common.data.EntityLoadRequest;
import com.tll.common.data.EntityOptions;
import com.tll.common.data.EntityPayload;
import com.tll.common.data.EntityPersistRequest;
import com.tll.common.data.EntityPurgeRequest;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.ISearch;
import com.tll.criteria.Criteria;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.ICriteria;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.model.IEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.PrimaryKey;
import com.tll.service.entity.IEntityService;

/**
 * MEntityServiceImpl - Provides base methods for CRUD ops on entities.
 * @author jpk
 * @param <E>
 */
public abstract class MEntityServiceImpl<E extends IEntity> implements IMEntityServiceImpl<E> {

	/**
	 * Loads additional entity properties.
	 * @param e
	 * @param options
	 * @param refs The map to place related entity references ({@link ModelKey})
	 *        if they are reuested in <code>options</code>.
	 * @param context The request context.
	 * @throws SystemError When any error occurrs.
	 */
	protected abstract void handleLoadOptions(MEntityContext context, E e, EntityOptions options,
			Map<String, ModelKey> refs) throws SystemError;

	/**
	 * Handles persist options specified in options.
	 * @param e
	 * @param options
	 * @param context The request context.
	 * @throws SystemError When any error occurrs.
	 */
	protected abstract void handlePersistOptions(MEntityContext context, E e, EntityOptions options)
	throws SystemError;

	/**
	 * Does the core entity loading.
	 * @param context
	 * @param request
	 * @param payload
	 * @return The loaded {@link IEntity}
	 */
	@SuppressWarnings("unchecked")
	protected E coreLoad(final MEntityContext context, final EntityLoadRequest request,
			final EntityPayload payload) {

		// core entity loading
		final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(request.getEntityType());
		final IEntityService<E> svc =
			context.getEntityServiceFactory().instanceByEntityType(entityClass);

		if(request.isLoadByBusinessKey()) {
			// load by business key
			final ISearch search = request.getSearch();
			if(search == null) {
				payload.getStatus().addMsg("A business key wise search must be specified.", MsgLevel.ERROR);
				return null;
			}

			final IBusinessKey key = handleBusinessKeyTranslation(search);

			return svc.load(key);
		}

		// load by primary key
		final Integer id = request.getEntityRef().getId();
		return svc.load(new PrimaryKey(entityClass, id));
	}

	public final void load(final MEntityContext context, final EntityLoadRequest request,
			final EntityPayload payload) {
		try {
			final E e = coreLoad(context, request, payload);
			if(e == null) {
				return;
			}

			// optional loading
			final Map<String, ModelKey> refs = new HashMap<String, ModelKey>();
			if(request.entityOptions != null) {
				handleLoadOptions(context, e, request.entityOptions, refs);
			}

			// marshal the loaded entity
			final Model group =
				context.getMarshaler().marshalEntity(e, getMarshalOptions(context));
			payload.setEntity(group);

			// set any entity refs
			for(final String propName : refs.keySet()) {
				payload.setRelatedOneRef(propName, refs.get(propName));
			}
		}
		catch(final EntityNotFoundException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), false);
		}
		catch(final SystemError e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
		}
		catch(final RuntimeException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public final void persist(final MEntityContext context, final EntityPersistRequest request,
			final EntityPayload payload) {
		try {
			// core persist
			final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(request.getEntityType());
			final Model entity = request.getEntity();
			E e = context.getMarshaler().unmarshalEntity(entityClass, entity);
			final IEntityService<E> svc =
				context.getEntityServiceFactory().instanceByEntityType(entityClass);
			e = svc.persist(e);

			// handle persist options
			handlePersistOptions(context, e, request.entityOptions);

			// marshall
			final Model group =
				context.getMarshaler().marshalEntity(e, getMarshalOptions(context));
			payload.setEntity(group);

			payload.getStatus().addMsg(e.descriptor() + " persisted.", MsgLevel.INFO);
		}
		catch(final EntityExistsException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), false);
		}
		catch(final ConstraintViolationException ise) {
			for(final ConstraintViolation iv : ise.getConstraintViolations()) {
				payload.getStatus().addMsg(iv.getMessage(), MsgLevel.ERROR, MsgAttr.FIELD.flag, iv.getPropertyPath());

			}
		}
		catch(final SystemError e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
		}
		catch(final RuntimeException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public final void purge(final MEntityContext context, final EntityPurgeRequest request,
			final EntityPayload payload) {
		try {
			final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(request.getEntityType());
			final IEntityService<E> svc =
				context.getEntityServiceFactory().instanceByEntityType(entityClass);
			final ModelKey entityRef = request.getEntityRef();
			if(entityRef == null || !entityRef.isSet()) {
				throw new EntityNotFoundException("A valid entity reference must be specified to purge an entity.");
			}
			final PrimaryKey pk = new PrimaryKey(entityClass, entityRef.getId());
			final E e = svc.load(pk);
			svc.purge(e);

			payload.setEntityRef(entityRef);
			payload.getStatus().addMsg(e.descriptor() + " purged.", MsgLevel.INFO);
		}
		catch(final EntityNotFoundException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), false);
		}
		catch(final SystemError e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
		}
		catch(final RuntimeException e) {
			context.getExceptionHandler().handleException(payload.getStatus(), e, e.getMessage(), true);
			throw e;
		}
	}

	/**
	 * Translates {@link ISearch} to {@link IBusinessKey}s.
	 * @param search The search to translate
	 * @return Translated {@link IBusinessKey}
	 * @throws IllegalArgumentException When the <code>search</code> parameter is
	 *         unsupported.
	 */
	protected abstract IBusinessKey<E> handleBusinessKeyTranslation(ISearch search) throws IllegalArgumentException;

	/**
	 * Handles the entity specific search to criteria translation.
	 * @param context
	 * @param search
	 * @param criteria
	 * @throws IllegalArgumentException When the <code>search</code> parameter is
	 *         unsupported.
	 */
	protected abstract void handleSearchTranslation(MEntityContext context, ISearch search,
			ICriteria<E> criteria)
	throws IllegalArgumentException;

	@SuppressWarnings("unchecked")
	public final ICriteria<E> translate(final MEntityContext context, final ISearch search)
	throws IllegalArgumentException {
		final CriteriaType criteriaType = search.getCriteriaType();
		final Class<E> entityClass = (Class<E>) EntityTypeUtil.getEntityClass(search.getEntityType());
		Criteria<E> criteria;
		final List<IQueryParam> queryParams = search.getQueryParams();

		if(criteriaType.isQuery()) {
			final ISelectNamedQueryDef queryDef = context.getQueryResolver().resolveNamedQuery(search.getNamedQuery());
			if(queryDef == null) {
				throw new IllegalArgumentException("Unable to resolve named query: " + search.getNamedQuery());
			}
			criteria = new Criteria<E>(queryDef, queryParams);
		}
		else {
			// entity
			criteria = new Criteria<E>(entityClass);
			handleSearchTranslation(context, search, criteria);
		}

		return criteria;
	}
}
