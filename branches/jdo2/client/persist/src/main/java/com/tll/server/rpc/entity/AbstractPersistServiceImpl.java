/**
 * The Logic Lab
 * @author jpk
 * @since Jun 24, 2009
 */
package com.tll.server.rpc.entity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.tll.common.data.ModelPayload;
import com.tll.common.data.Status;
import com.tll.common.model.IEntityType;
import com.tll.common.model.IPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.search.BusinessKeySearch;
import com.tll.common.search.EntityNameSearch;
import com.tll.common.search.ISearch;
import com.tll.common.search.PrimaryKeySearch;
import com.tll.dao.EntityExistsException;
import com.tll.dao.EntityNotFoundException;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKeyFactory;
import com.tll.model.key.BusinessKeyNotDefinedException;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.ISchemaProperty;
import com.tll.server.marshal.MarshalOptions;
import com.tll.server.rpc.RpcServlet;
import com.tll.service.entity.IEntityService;
import com.tll.service.entity.INamedEntityService;
import com.tll.util.ObjectUtil;
import com.tll.util.PropertyPath;

/**
 * AbstractPersistServiceImpl
 * @author jpk
 */
public abstract class AbstractPersistServiceImpl implements IPersistServiceImpl {

	/**
	 * Client-izes the given property path (need to account for possible nested).
	 * <p>
	 * It is assumed nested entities are only 1-level deep
	 * @param <T> the entity type
	 * @param schemaInfo
	 * @param entityClass
	 * @param path
	 * @return the clientized path
	 */
	protected static final <T> String clientizePropertyPath(ISchemaInfo schemaInfo, Class<T> entityClass, String path) {
		final PropertyPath p = new PropertyPath(path);
		if(p.depth() > 2) {
			final String ppp = p.trim(1);
			final ISchemaProperty sp = schemaInfo.getSchemaProperty(entityClass, ppp);
			if(sp.getPropertyType().isNested()) {
				path = ppp + '_' + p.last();
			}
		}
		return path;
	}

	protected final PersistContext context;

	/**
	 * Constructor
	 * @param context the required persist context
	 */
	public AbstractPersistServiceImpl(PersistContext context) {
		super();
		if(context == null) throw new IllegalArgumentException("Null persist context.");
		this.context = context;
	}

	@Override
	public final void load(ISearch search, ModelPayload payload) {
		if(search == null || !search.isSet()) {
			payload.getStatus().addMsg(search == null ? "No search criteria specified" : "Unset search criteria",
					MsgLevel.ERROR, MsgAttr.STATUS.flag);
		}
		else {
			IEntity e = null;
			IEntityType et = null;
			if(search instanceof PrimaryKeySearch) {
				final PrimaryKeySearch pks = (PrimaryKeySearch) search;
				e = loadEntityByPrimaryKey(pks, payload.getStatus());
				et = pks.getKey().getEntityType();
			}
			else if(search instanceof BusinessKeySearch) {
				final BusinessKeySearch bks = (BusinessKeySearch) search;
				e = loadEntityByBusinesKey(bks, payload.getStatus());
				et = bks.getEntityType();
			}
			else if(search instanceof EntityNameSearch) {
				final EntityNameSearch ens = (EntityNameSearch) search;
				e = loadEntityByName(ens, payload.getStatus());
				et = ens.getEntityType();
			}
			else {
				// override
				loadImpl(search, payload);
				return;
			}

			if(e != null) {
				assert et != null;
				try {
					final Model m = entityToModel(et, e);
					payload.setModel(m);
					payload.getStatus().addMsg(e.descriptor() + " loaded.", MsgLevel.INFO, MsgAttr.STATUS.flag);
				}
				catch(final Exception ex) {
					RpcServlet.exceptionToStatus(ex, payload.getStatus());
				}
			}
		}
	}

	/**
	 * Translates a target entity instance to a {@link Model} instance.
	 * @param entityType the entity type
	 * @param e the entity to translate
	 * @return the translated model instance
	 * @throws Exception upon error
	 */
	protected Model entityToModel(IEntityType entityType, IEntity e) throws Exception, Exception {
		// default simply marshals the entity
		try {
			return marshal(entityType, e);
		}
		catch(final Throwable t) {
			throw new Exception(t);
		}
	}

	/**
	 * The default add routine which may be overridden.
	 * @param model
	 * @param payload
	 * @throws EntityExistsException When the entity is non-unique by way of a defined business key
	 * @throws ConstraintViolationException When the marshaled entity is found invalid
	 */
	@SuppressWarnings("unchecked")
	protected void doAdd(Model model, ModelPayload payload) throws EntityExistsException, ConstraintViolationException {
		final Class<? extends IEntity> entityClass =
			(Class<? extends IEntity>) context.getEntityTypeResolver().resolveEntityClass(model.getEntityType());
		IEntity e = context.getMarshaler().marshalModel(model, entityClass);
		final IEntityService<IEntity> svc =
			(IEntityService<IEntity>) context.getEntityServiceFactory().instanceByEntityType(entityClass);
		e = svc.persist(e);

		// marshall
		model = marshal(model.getEntityType(), e);
		payload.setModel(model);

		payload.getStatus().addMsg(e.descriptor() + " added.", MsgLevel.INFO, MsgAttr.STATUS.flag);
	}

	/**
	 * The default update routine which may be overridden.
	 * @param modelChanges presumed to only contain model properties that were
	 *        altered
	 * @param payload
	 * @throws VersionMismatchException When the model data version doesn't match
	 *         the loaded target entity version
	 * @throws ConstraintViolationException When the marshaled entity is found invalid
	 */
	@SuppressWarnings("unchecked")
	protected void doUpdate(Model modelChanges, ModelPayload payload) throws VersionMismatchException, ConstraintViolationException {
		final Class<? extends IEntity> entityClass =
			(Class<? extends IEntity>) context.getEntityTypeResolver().resolveEntityClass(modelChanges.getEntityType());
		final IEntityService<IEntity> svc =
			(IEntityService<IEntity>) context.getEntityServiceFactory().instanceByEntityType(entityClass);
		final Class<IEntity> eclass = resolveEntityClass(modelChanges.getEntityType());

		// load current state of this entity
		final Long id = Long.valueOf(modelChanges.getId());
		IEntity e = svc.load(new PrimaryKey<IEntity>(eclass, id));

		// ensure versions match!
		if(!ObjectUtil.equals(Long.valueOf(modelChanges.getVersion()), Long.valueOf(e.getVersion()))) {
			throw new VersionMismatchException(eclass, Long.valueOf(e.getVersion()), Long.valueOf(modelChanges.getVersion()));
		}

		// marshal the changes only
		context.getMarshaler().marshalModel(modelChanges, e);
		final boolean isNew = e.isNew();
		e = svc.persist(e);

		// marshal
		final Model refreshedModel = marshal(modelChanges.getEntityType(), e);
		payload.setModel(refreshedModel);

		payload.getStatus().addMsg(e.descriptor() + (isNew ? " added." : " updated."), MsgLevel.INFO, MsgAttr.STATUS.flag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void persist(Model model, ModelPayload payload) {
		try {
			if(model.isNew())
				doAdd(model, payload);
			else
				doUpdate(model, payload);
		}
		catch(final VersionMismatchException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
		}
		catch(final EntityExistsException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
		}
		catch(final ConstraintViolationException ise) {
			final Class<? extends IEntity> entityClass =
				(Class<? extends IEntity>) context.getEntityTypeResolver().resolveEntityClass(model.getEntityType());
			for(final ConstraintViolation iv : ise.getConstraintViolations()) {
				// resolve index if we have a violation on under an indexed entity property
				// since the validation api doesn't provide the index rather only empty brackets ([])
				// in the ConstraintViolation's propertyPath property
				payload.getStatus().addMsg(iv.getMessage(), MsgLevel.ERROR, MsgAttr.FIELD.flag,
						clientizePropertyPath(context.getSchemaInfo(), entityClass, iv.getPropertyPath().toString()));
			}
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
			context.getExceptionHandler().handleException(e);
			throw e;
		}
	}

	@Override
	public final void purge(Model model, ModelPayload payload) {
		purgeImpl(model, payload);
	}

	@Override
	public void purge(ModelKey ref, ModelPayload payload) {
		purgeEntity(ref, payload);
	}

	@SuppressWarnings("unchecked")
	protected final Class<IEntity> resolveEntityClass(IEntityType entityType) {
		return (Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(entityType);
	}

	/**
	 * Resolves the approprate entity service for a particular entity type.
	 * @param entityType
	 * @return the resolved entity service
	 * @throws IllegalArgumentException when the service can't be resolved
	 */
	protected final IEntityService<IEntity> getEntityService(IEntityType entityType) throws IllegalArgumentException {
		return context.getEntityServiceFactory().instanceByEntityType(resolveEntityClass(entityType));
	}

	/**
	 * Convenience method for entity to model marshaling.
	 * @param entityType
	 * @param entity
	 * @return new {@link Model} instance
	 * @throws RuntimeException upon marshaling related error
	 */
	protected Model marshal(IEntityType entityType, IEntity entity) throws RuntimeException {
		MarshalOptions mo;
		try {
			mo = context.getMarshalOptionsResolver().resolve(entityType);
		}
		catch(final IllegalArgumentException e) {
			// default fallback
			mo = MarshalOptions.NO_REFERENCES;
		}
		final Model m = context.getMarshaler().marshalEntity(entity, mo);
		assert m != null;
		return m;
	}

	/**
	 * @return A descriptive ui-ready name for the type of model data this
	 *         implmentation supports.
	 */
	protected abstract String getModelTypeName();

	/**
	 * Loads an entity by primary key marshaling it into a {@link Model} instance.
	 * @param search guaranteed non-<code>null</code> search criteria
	 * @param status the status to which messages are posted
	 */
	@SuppressWarnings("unchecked")
	protected IEntity loadEntityByPrimaryKey(PrimaryKeySearch search, Status status) {
		try {
			final ModelKey mkey = search.getKey();
			final IEntityType et = mkey.getEntityType();
			final Class<IEntity> ec = (Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(et);
			final IEntityService<IEntity> svc = getEntityService(et);
			final Long id = Long.valueOf(mkey.getId());
			final IEntity e = svc.load(new PrimaryKey(ec, id));
			return e;
		}
		catch(final EntityNotFoundException e) {
			RpcServlet.exceptionToStatus(e, status);
			return null;
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, status);
			throw e;
		}
	}

	/**
	 * Loads an entity by business key marshaling it into a {@link Model}
	 * instance.
	 * @param search guaranteed non-<code>null</code> search criteria
	 * @param status the status to which messages are posted
	 */
	@SuppressWarnings("unchecked")
	protected IEntity loadEntityByBusinesKey(BusinessKeySearch search, Status status) {
		try {
			final IEntityType et = search.getEntityType();
			final Class<IEntity> ec = (Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(et);
			final String bkName = search.getBusinessKeyName();
			final IPropertyValue[] pvs = search.getProperties();
			IBusinessKey<IEntity> bk;
			bk = BusinessKeyFactory.create(ec, bkName);
			for(final IPropertyValue pv : pvs) {
				bk.setPropertyValue(pv.getPropertyName(), pv.getValue());
			}
			final IEntity e = getEntityService(et).load(bk);
			// final Model m = marshal(et, e);
			// payload.setModel(m);
			status.addMsg(e.descriptor() + " loaded.", MsgLevel.INFO, MsgAttr.STATUS.flag);
			return e;
		}
		catch(final BusinessKeyNotDefinedException e) {
			RpcServlet.exceptionToStatus(e, status);
			return null;
		}
		catch(final EntityNotFoundException e) {
			RpcServlet.exceptionToStatus(e, status);
			return null;
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, status);
			throw e;
		}
	}

	/**
	 * Loads an entity by name marshaling it into a {@link Model} instance.
	 * @param search guaranteed non-<code>null</code> search criteria
	 * @param status the status to which messages are posted
	 */
	@SuppressWarnings("unchecked")
	protected IEntity loadEntityByName(EntityNameSearch search, Status status) {
		try {
			final IEntityType et = search.getEntityType();
			final Class<IEntity> ec = (Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(et);
			final String name = search.getName();
			final IEntityService<IEntity> svc = getEntityService(et);
			if(svc instanceof INamedEntityService == false) {
				throw new RuntimeException("Entity type: " + et + "doesn't support loading by name.");
			}
			final IEntity e = ((INamedEntityService) svc).load(new NameKey(ec, name));
			// final Model m = marshal(et, e);
			// payload.setModel(m);
			status.addMsg(e.descriptor() + " loaded.", MsgLevel.INFO, MsgAttr.STATUS.flag);
			return e;
		}
		catch(final EntityNotFoundException e) {
			RpcServlet.exceptionToStatus(e, status);
			return null;
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, status);
			throw e;
		}
	}

	/**
	 * Impl specific load routine. Usually employed when we are not adhering to
	 * entity boundaries.
	 * @param search
	 * @param payload
	 */
	protected void loadImpl(ISearch search, ModelPayload payload) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Purges an entity given a {@link ModelKey} ref.
	 * @param ref identifies the entity to be purged
	 * @param payload The {@link ModelPayload} that is filled
	 */
	@SuppressWarnings("unchecked")
	protected void purgeEntity(ModelKey ref, ModelPayload payload) {
		try {
			final Class<IEntity> entityClass =
				(Class<IEntity>) context.getEntityTypeResolver().resolveEntityClass(ref.getEntityType());
			final IEntityService<IEntity> svc = context.getEntityServiceFactory().instanceByEntityType(entityClass);
			final Long id = Long.valueOf(ref.getId());
			final PrimaryKey pk = new PrimaryKey(entityClass, id);
			final IEntity e = svc.load(pk);
			svc.purge(e);
			payload.setRef(ref);
			payload.getStatus().addMsg(e.descriptor() + " purged.", MsgLevel.INFO, MsgAttr.STATUS.flag);
		}
		catch(final EntityNotFoundException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
		}
		catch(final RuntimeException e) {
			RpcServlet.exceptionToStatus(e, payload.getStatus());
			context.getExceptionHandler().handleException(e);
			throw e;
		}
	}

	protected void purgeImpl(Model model, ModelPayload payload) {
		throw new UnsupportedOperationException();
	}
}
