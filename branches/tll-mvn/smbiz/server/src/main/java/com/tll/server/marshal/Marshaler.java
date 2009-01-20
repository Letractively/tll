/**
 * The Logic Lab
 */
package com.tll.server.marshal;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.persistence.Transient;

import org.springframework.beans.BeanWrapperImpl;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.client.model.BooleanPropertyValue;
import com.tll.client.model.CharacterPropertyValue;
import com.tll.client.model.DatePropertyValue;
import com.tll.client.model.EnumPropertyValue;
import com.tll.client.model.FloatPropertyValue;
import com.tll.client.model.IModelProperty;
import com.tll.client.model.IntPropertyValue;
import com.tll.client.model.LongPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.model.RelatedOneProperty;
import com.tll.client.model.StringMapPropertyValue;
import com.tll.client.model.StringPropertyValue;
import com.tll.dao.SearchResult;
import com.tll.model.EntityType;
import com.tll.model.EntityUtil;
import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.IEntityFactory;
import com.tll.model.IScalar;
import com.tll.model.PaymentData;
import com.tll.model.PaymentInfo;
import com.tll.model.schema.ISchemaInfo;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.RelationInfo;
import com.tll.model.schema.SchemaInfoException;

/**
 * Marshaler - Converts server-side entities to client-bound Data transfer
 * objects and vice-versa.
 * @author jpk
 */
public final class Marshaler {

	private final IEntityFactory entityFactory;
	private final ISchemaInfo schemaInfo;

	/**
	 * Constructor
	 * @param entityFactory
	 * @param schemaInfo
	 */
	@Inject
	public Marshaler(final IEntityFactory entityFactory, final ISchemaInfo schemaInfo) {
		this.entityFactory = entityFactory;
		this.schemaInfo = schemaInfo;
	}

	/**
	 * Binding - Used for walking an {@link Object}'s hiererchy to avoid infinite
	 * looping as the hierarchy may contain circular references.
	 * @author jpk
	 */
	protected static final class Binding {

		final Object source;
		final Model group;

		/**
		 * Constructor
		 * @param source
		 * @param group
		 */
		Binding(final Object source, final Model group) {
			this.source = source;
			this.group = group;
		}
	}

	/**
	 * BindingStack
	 * @author jpk
	 */
	protected static final class BindingStack extends Stack<Binding> {

		private static final long serialVersionUID = -7748594505590343080L;

		/**
		 * Searches for a {@link Binding} given an {@link Object} ref.
		 * @param source
		 * @return The found {@link Binding} or <code>null</code> if not present in
		 *         this stack.
		 */
		public Binding find(final Object source) {
			for(final Binding b : this) {
				if(b.source == source) {
					return b;
				}
			}
			return null;
		}

		/**
		 * Searches for a {@link Binding} given a {@link Model} ref.
		 * @param group
		 * @return The found {@link Binding} or <code>null</code> if not present in
		 *         this stack.
		 */
		public Binding find(final Model group) {
			for(final Binding b : this) {
				if(b.group == group) {
					return b;
				}
			}
			return null;
		}
	}

	/**
	 * Simple check to filter out certain properties based on their name.
	 * @param propName
	 * @return
	 */
	private boolean isMarshalableProperty(final PropertyDescriptor pd) {
		// check bound method annotations and honor @Transient
		final Method m = pd.getReadMethod();
		if(m != null) {
			final Transient t = m.getAnnotation(Transient.class);
			return (t == null);
		}
		return true;
	}

	/**
	 * Determines if recursion should drill down another level.
	 * @param depth The current hierarchical depth
	 * @param maxDepth The max allowed hierarchical depth
	 * @return <code>true</code> if depth check passes.
	 */
	private boolean depthCheck(final int depth, final int maxDepth) {
		return (maxDepth < 0 || (maxDepth >= 0 && ((depth + 1) <= maxDepth)));
	}

	/**
	 * Returns a property value if the given property type supports it.
	 * @param ptype The property type
	 * @param pname The property name
	 * @param obj The value. May be <code>null</code>.
	 * @param metadata The property meta data. May be <code>null</code>.
	 * @return new IModelProperty instance or <code>null</code> if the given
	 *         property type does NOT corres. to a rudimentary property value.
	 */
	private IModelProperty createModelProperty(final Class<?> ptype, final String pname, final Object obj,
			final PropertyMetadata pdata) {
		IModelProperty prop = null;

		if(String.class.isAssignableFrom(ptype)) {
			prop = new StringPropertyValue(pname, pdata, (String) obj);
		}

		else if(Date.class.isAssignableFrom(ptype)) {
			final Date d = obj == null ? null : new Date(((Date) obj).getTime());
			prop = new DatePropertyValue(pname, pdata, d);
		}

		else if(Enum.class.isAssignableFrom(ptype)) {
			prop = new EnumPropertyValue(pname, pdata, obj == null ? null : (Enum<?>) obj);
		}

		else if(Long.class.isAssignableFrom(ptype) || long.class.isAssignableFrom(ptype)) {
			prop = new LongPropertyValue(pname, pdata, (Long) obj);
		}

		else if(Integer.class.isAssignableFrom(ptype) || int.class.isAssignableFrom(ptype)) {
			prop = new IntPropertyValue(pname, pdata, (Integer) obj);
		}

		else if(Character.class.isAssignableFrom(ptype) || char.class.isAssignableFrom(ptype)) {
			prop = new CharacterPropertyValue(pname, pdata, (Character) obj);
		}

		else if(Boolean.class.isAssignableFrom(ptype) || boolean.class.isAssignableFrom(ptype)) {
			prop = new BooleanPropertyValue(pname, pdata, (Boolean) obj);
		}

		else if(Float.class.isAssignableFrom(ptype) || float.class.isAssignableFrom(ptype)) {
			prop = new FloatPropertyValue(pname, pdata, (Float) obj);
		}

		return prop;
	}

	/**
	 * Converts a {@link SearchResult} instance to a new {@link Model} instance
	 * containing all marshalable properties contained within the search result.
	 * @param searchResult May NOT be <code>null</code>.
	 * @param options May NOT be <code>null</code>.
	 * @return client ready model
	 * @throws IllegalArgumentException When neither an IEntity nor an IScalar is
	 *         resolved from the given SearchResult.
	 */
	public Model marshalSearchResult(final SearchResult<? extends IEntity> searchResult, final MarshalOptions options) {
		assert searchResult != null;
		assert options != null;
		Model model;
		final IEntity e = searchResult.getEntity();
		if(e == null) {
			final IScalar scalar = searchResult.getScalar();
			if(scalar == null) {
				throw new IllegalArgumentException(
						"Neither an entity nor scalar was resolvable from the search result argument");
			}
			model = marshalScalar(scalar, options);
		}
		else {
			BindingStack visited = new BindingStack();
			model = marshalEntity(e, options, 0, visited);
			visited = null;
		}
		return model;
	}

	/**
	 * Marshals the given entity.
	 * @param <E>
	 * @param entity May not be <code>null</code>.
	 * @param options May not be <code>null</code>.
	 * @return New {@link Model} instance containing the marshaled entity
	 *         properties.
	 * @throws SystemError upon any error encountered.
	 */
	public <E extends IEntity> Model marshalEntity(final E entity, final MarshalOptions options) {
		assert entity != null;
		assert options != null;
		return marshalEntity(entity, options, 0, new BindingStack());
	}

	/**
	 * Converts a source IEntity instance to a marshalable {@link Model}.
	 * @param <E> The entity type
	 * @param source The source IEntity to marshal. May not be <code>null</code>.
	 * @param options The marshaling options. May NOT be <code>null</code>.
	 * @return Marshaled {@link Model}.
	 * @throws SystemError upon any error encountered.
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> Model marshalEntity(final E source, final MarshalOptions options, final int depth,
			final BindingStack visited) {

		// check visited
		Binding b = visited.find(source);
		if(b != null) {
			return b.group;
		}

		final Class<? extends IEntity> entityClass = source.entityClass();
		final Model model = new Model(EntityUtil.entityTypeFromClass(entityClass));

		b = new Binding(source, model);
		visited.push(b);

		final BeanWrapperImpl bw = new BeanWrapperImpl(source);

		for(final PropertyDescriptor pd : bw.getPropertyDescriptors()) {
			final String pname = pd.getName();

			// skip certain properties...
			if(!bw.isWritableProperty(pname) || !isMarshalableProperty(pd)) {
				continue;
			}

			final Class<?> ptype = pd.getPropertyType();
			Object obj = null;
			try {
				obj = bw.getPropertyValue(pname);
			}
			catch(final RuntimeException re) {
				continue;
			}

			final PropertyMetadata pdata = generatePropertyData(entityClass, pname);

			IModelProperty prop = createModelProperty(ptype, pname, obj, pdata);

			if(prop == null) {
				// related one
				if(IEntity.class.isAssignableFrom(ptype)) {
					final RelationInfo ri = getRelationInfo(entityClass, pname);
					boolean reference = ri.isReference();
					if(shouldMarshalRelation(reference, depth, options)) {
						final IEntity e = (IEntity) obj;
						final Model ngrp = e == null ? null : marshalEntity(e, options, depth + 1, visited);
						prop = new RelatedOneProperty(ri.getRelatedType(), pname, reference, ngrp);
					}
				}

				// related many collection
				else if(Collection.class.isAssignableFrom(ptype)) {
					final RelationInfo ri = getRelationInfo(entityClass, pname);
					boolean reference = ri.isReference();
					if(shouldMarshalRelation(reference, depth, options)) {
						List<Model> list = null;
						if(obj != null) {
							list = new ArrayList<Model>();
							final Set<IEntity> set = (Set<IEntity>) obj;
							for(final IEntity e : set) {
								final Model nested = marshalEntity(e, options, depth + 1, visited);
								list.add(nested);
							}
						}
						prop = new RelatedManyProperty(ri.getRelatedType(), pname, reference, list);
					}
				}

				// map (assume <String, String> type)
				else if(Map.class.isAssignableFrom(ptype)) {
					prop = new StringMapPropertyValue(pname, generatePropertyData(entityClass, pname), (Map) obj);
				}

				// special case: PaymentData object
				else if("paymentData".equals(pname)) {
					final BeanWrapperImpl bw2 = new BeanWrapperImpl(obj);
					for(final PropertyDescriptor pd2 : bw2.getPropertyDescriptors()) {
						if(bw2.isWritableProperty(pd2.getName()) && isMarshalableProperty(pd2)) {
							try {
								final Object oval = bw2.getPropertyValue(pd2.getName());
								if(oval != null) {
									model.set(createModelProperty(pd2.getPropertyType(), "paymentData_" + pd2.getName(), oval,
											generatePropertyData(PaymentInfo.class, "paymentData." + pd2.getName())));
								}
							}
							catch(final RuntimeException e) {
							}
						}
					}
				}
				else {
					throw new SystemError("Unhandled property type: " + ptype);
				}
			}

			if(prop != null) {
				model.set(prop);
			}

		}

		return model;
	}

	/**
	 * Converts a source Object instance to a marshalable {@link Model}.
	 * @param <S> The scalar type
	 * @param source The IScalar to marshal. May not be <code>null</code>.
	 * @param options The marshaling options. May NOT be <code>null</code>.
	 * @return Marshaled object as a {@link Model}.
	 * @throws SystemError upon any error encountered.
	 */
	public <S extends IScalar> Model marshalScalar(final S source, final MarshalOptions options) {

		final Model model = new Model(EntityUtil.entityTypeFromClass(source.getRefType()));

		final Map<String, Object> tupleMap = source.getTupleMap();
		for(final String pname : tupleMap.keySet()) {
			final Object obj = tupleMap.get(pname);
			// NOTE: if we have a null tuple value, then default the type to String!
			final Class<?> ptype = obj == null ? String.class : obj.getClass();
			final IModelProperty prop = createModelProperty(ptype, pname, obj, null);
			if(prop != null) {
				model.set(prop);
			}
		}

		return model;
	}

	/**
	 * Un-marshals a {@link Model} representing an entity to an {@link IEntity}.
	 * @param <E>
	 * @param entityClass
	 * @param entityGroup
	 * @return Un-marshaled server-side {@link IEntity} instance.
	 */
	public <E extends IEntity> E unmarshalEntity(final Class<E> entityClass, final Model entityGroup) {
		BindingStack visited = new BindingStack();
		final E e = unmarshalEntity(entityClass, entityGroup, visited);
		visited = null;
		return e;
	}

	/**
	 * Unmarshals a UI entity to a new {@link IEntity} instance.
	 * @param <E>
	 * @param entityClass {@link IEntity} class.
	 * @param entityGroup The {@link Model} holding the entity properties
	 * @param visited BindingStack holding visited entities to handle circular
	 *        entity relationships
	 * @return New <E> instance
	 * @throws SystemError upon any error encountered.
	 */
	@SuppressWarnings("unchecked")
	private <E extends IEntity> E unmarshalEntity(final Class<E> entityClass, final Model entityGroup,
			final BindingStack visited) {

		// only recurse if not already visited
		Binding b = visited.find(entityGroup);
		if(b != null) {
			return (E) b.source;
		}

		E e;
		try {
			e = entityClass.newInstance();
		}
		catch(final InstantiationException ie) {
			throw new SystemError("Unable to instantiate entity of class: " + entityClass.getSimpleName());
		}
		catch(final IllegalAccessException iae) {
			throw new SystemError("Unable to instantiate entity of class: " + entityClass.getSimpleName());
		}

		// special case: payment info
		if(PaymentInfo.class.equals(entityClass)) {
			((PaymentInfo) e).setPaymentData(new PaymentData());
		}

		b = new Binding(e, entityGroup);
		visited.push(b);

		final BeanWrapperImpl bw = new BeanWrapperImpl(e);

		for(final Iterator itr = entityGroup.iterator(); itr.hasNext();) {
			final IModelProperty prop = (IModelProperty) itr.next();
			String propName = prop.getPropertyName();
			final Object pval = prop.getValue();
			Object val = null;

			switch(prop.getType()) {

				case STRING:
				case LONG:
				case INT:
				case BOOL:
				case FLOAT:
				case DATE:
				case CHAR:
				case ENUM:
					val = pval;
					break;

				case RELATED_ONE: {
					final Model rltdOne = (Model) pval;
					final EntityType entityType = rltdOne == null ? null : rltdOne.getEntityType();
					final IEntity toOne =
							(rltdOne == null || rltdOne.isMarkedDeleted() ? null : unmarshalEntity(EntityUtil
									.entityClassFromType(entityType), rltdOne, visited));
					val = toOne;
				}
					break;

				case RELATED_MANY: {
					final List<Model> el = (List<Model>) pval;
					Collection<IEntity> clc;
					final Class<?> c = bw.getPropertyType(propName);
					final int size = el == null ? 0 : el.size();
					if(Set.class.isAssignableFrom(c)) {
						clc = new LinkedHashSet<IEntity>(size);
					}
					else if(List.class.isAssignableFrom(c)) {
						clc = new ArrayList<IEntity>(size);
					}
					else {
						throw new SystemError("Unhandled collection type: " + c.getSimpleName());
					}
					for(final Object obj : el) {
						final Model model = (Model) obj;
						final EntityType entityType = model.getEntityType();
						final IEntity clcEntity =
								model.isMarkedDeleted() ? null : unmarshalEntity(EntityUtil.entityClassFromType(entityType), model,
										visited);
						if(clcEntity instanceof IChildEntity) {
							((IChildEntity) clcEntity).setParent(e);
						}
						if(clcEntity != null) {
							clc.add(clcEntity);
						}
					}
					val = clc;
				}
					break;

				case STRING_MAP:
					val = pval;
					break;

				default:
					throw new SystemError("Unhandled Property Value type: " + prop.getType());

			}// switch

			// special case: paymentData_
			if(propName.startsWith("paymentData_")) {
				propName = "paymentData." + propName.substring(12);
			}

			try {
				bw.setPropertyValue(propName, val);
			}
			catch(final RuntimeException re) {
				// ok
			}

		}// for loop

		if(e.getId() == null) {
			// assume new and set generated id
			assert e.getVersion() == null : "Encountered an entity w/o an id having and non-null version!";
			entityFactory.setGenerated(e);
		}

		return e;
	}

	/**
	 * Provides {@link PropertyMetadata} for a given entities' property.
	 * @param entityClass
	 * @param propName
	 * @return New {@link PropertyMetadata} instance or <code>null</code> if the
	 *         given property name does not resolve to a property value.
	 */
	private PropertyMetadata generatePropertyData(final Class<? extends IEntity> entityClass, final String propName) {
		try {
			return schemaInfo.getPropertyMetadata(entityClass, propName);
		}
		catch(SchemaInfoException e) {
			return null;
		}
	}

	/**
	 * Gets relational info for a given entity property that is expected to be
	 * relational
	 * @param entityClass
	 * @param propName
	 * @return true/false
	 * @throws SchemaInfoException When the prop name is not resolvable to a model
	 *         relation.
	 */
	private RelationInfo getRelationInfo(final Class<? extends IEntity> entityClass, final String propName) {
		return schemaInfo.getRelationInfo(entityClass, propName);
	}

	/**
	 * Ascertains whether or not a given relational type property for a given
	 * entity type should be marshaled.
	 * @param isReferenceRelation
	 * @param currentDepth
	 * @param marshalOptions
	 * @return true/false
	 */
	private boolean shouldMarshalRelation(boolean isReferenceRelation, int currentDepth, MarshalOptions marshalOptions) {
		if(!depthCheck(currentDepth, marshalOptions.getMaxDepth())) {
			return false;
		}
		if(isReferenceRelation) {
			return marshalOptions.isProcessReferenceRelations();
		}
		return true;
	}
}
