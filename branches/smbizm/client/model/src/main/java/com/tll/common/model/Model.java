/**
 * The Logic Lab
 * @author jpk Aug 31, 2007
 */
package com.tll.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tll.IDescriptorProvider;
import com.tll.IMarshalable;
import com.tll.common.bind.IBindable;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.model.schema.IPropertyMetadataProvider;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;
import com.tll.util.PropertyPath;
import com.tll.util.StringUtil;

/**
 * Model - Encapsulates a set of {@link IModelProperty}s. This construct serves
 * to represent an entity instance object graph on the client.
 * @author jpk
 */
public final class Model implements IMarshalable, IBindable, IPropertyMetadataProvider,
 IEntityTypeProvider, IDescriptorProvider, Iterable<IModelProperty> {

	/**
	 * Entity id property name
	 */
	public static final String ID_PROPERTY = "id";

	/**
	 * Entity version property name
	 */
	public static final String VERSION_PROPERTY = "version";

	/**
	 * Entity name property name. May not be set for entity definitions without a
	 * name property.
	 */
	public static final String NAME_PROPERTY = "name";

	/**
	 * Entity data created property name. May not be set for entity definitions
	 * without a date created property.
	 */
	public static final String DATE_CREATED_PROPERTY = "dateCreated";

	/**
	 * Entity data modified property name. May not be set for entity definitions
	 * without a date modified property.
	 */
	public static final String DATE_MODIFIED_PROPERTY = "dateModified";

	/**
	 * The set of model properties. <br>
	 * NOTE: can't mark as final for GWT RPC compatibility
	 */
	private final /*final*/Set<IModelProperty> props = new HashSet<IModelProperty>();

	/**
	 * The entity type.
	 */
	private IEntityType entityType;

	/**
	 * The marked deleted flag. When <code>true</code>, this indicates this model
	 * data is scheduled for deletion.
	 */
	private boolean markedDeleted;

	/**
	 * Ref to self (this {@link Model}) that is lazily instantiated and is exists
	 * to be able to pass a {@link Model} ref as an {@link IModelProperty}.
	 */
	private RelatedOneProperty selfRef;

	/**
	 * Constructor
	 */
	public Model() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType
	 */
	public Model(IEntityType entityType) {
		super();
		this.entityType = entityType;
	}

	/**
	 * @return the ref type
	 */
	public IEntityType getEntityType() {
		return entityType;
	}

	/**
	 * Is this model entity new?
	 * @return true/false
	 */
	public boolean isNew() {
		final IntPropertyValue prop = (IntPropertyValue) get(VERSION_PROPERTY);
		return prop == null ? true : (prop.getInteger() == null);
	}

	/**
	 * Retrieves the id property
	 * @return The entity id
	 */
	public Integer getId() {
		final IntPropertyValue prop = (IntPropertyValue) get(ID_PROPERTY);
		return prop == null ? null : prop.getInteger();
	}

	/**
	 * Retrieves the entities' name property value
	 * @return The entities' name
	 */
	public String getName() {
		final StringPropertyValue prop = (StringPropertyValue) get(NAME_PROPERTY);
		return prop == null ? null : prop.getString();
	}

	/**
	 * @return The date created. May be <code>null</code> for entity types that
	 *         don't support this property.
	 */
	public Date getDateCreated() {
		final DatePropertyValue prop = (DatePropertyValue) get(DATE_CREATED_PROPERTY);
		return prop == null ? null : prop.getDate();
	}

	/**
	 * @return The date last modified. May be <code>null</code> for entity types
	 *         that don't support this property.
	 */
	public Date getDateModified() {
		final DatePropertyValue prop = (DatePropertyValue) get(DATE_MODIFIED_PROPERTY);
		return prop == null ? null : prop.getDate();
	}

	/**
	 * Provides the <em>unique</em> {@link ModelKey} for this instance.
	 * @return the unique key
	 */
	public ModelKey getKey() {
		return new ModelKey(entityType, getId(), getName());
	}

	/**
	 * @return Reference to <em>this</em> Model expressed as an
	 *         {@link IModelProperty}.
	 */
	private RelatedOneProperty getSelfRef() {
		if(selfRef == null) {
			selfRef = new RelatedOneProperty(getEntityType(), null, true, this);
		}
		return selfRef;
	}

	/**
	 * Finds a property value in this model's collection of property values given
	 * a non-path property name. (No property path resolution is performed.)
	 * <p>
	 * @param name The non-path property name (i.e. no dots)
	 * @return The found {@link IPropertyValue} or <code>null</code> if not
	 *         present.
	 */
	public IModelProperty get(String name) {
		if(name == null) return null;
		for(final IModelProperty prop : props) {
			if(name.equals(prop.getPropertyName())) return prop;
		}
		return null;
	}

	/**
	 * Sets the given {@link IPropertyValue} as a child to this model with no
	 * property path resolution preformed. If an existing prop val is currently
	 * mapped to the ascribed property name, it is replaced by the one given.
	 * <p>
	 * <em><b>IMPT:</b> This method does not fire property change events.</em>
	 * @param mprop The replacing {@link IPropertyValue}
	 */
	public void set(IModelProperty mprop) {
		if(mprop == null) return;
		final IModelProperty prop = get(mprop.getPropertyName());
		if(prop != mprop) {
			if(prop != null) {
				props.remove(prop);
			}
			props.add(mprop);
		}
	}

	/**
	 * Physically removes the given model property from this model if it exists.
	 * @param mprop the property ref to remove
	 * @param drill drill down into nested models?
	 * @return <code>true</code> if the property was removed.
	 */
	public boolean remove(final IModelProperty mprop, final boolean drill) {
		Object rmv = null;
		for(final IModelProperty prop : props) {
			if(mprop == prop) {
				rmv = prop;
				break;
			}
			else if(drill && prop.getType().isModelRef()) {
				// drill down
				return ((IModelRefProperty) prop).getModel().remove(prop, true);
			}
			else if(drill && prop.getType() == PropertyType.RELATED_MANY) {
				for(final Model m : ((RelatedManyProperty) prop).getList()) {
					if(m.remove(mprop, true)) {
						return true;
					}
				}
			}
		}
		if(rmv != null) {
			return props.remove(rmv);
		}
		return false;
	}

	/**
	 * Provides the property value in String form
	 * <em>for self-formatting types only</em>.
	 * @param propPath The property path
	 * @return The property value as a String or <code>null</code> when the
	 *         property does not exist.
	 * @throws IllegalArgumentException When the targeted property is relational
	 *         or is not self-formatting.
	 */
	public String asString(String propPath) throws IllegalArgumentException {
		IModelProperty prop = null;
		try {
			prop = getModelProperty(propPath);
		}
		catch(final PropertyPathException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		assert prop != null;
		if(prop instanceof ISelfFormattingPropertyValue == false) {
			throw new IllegalArgumentException("Non self-formatting property: " + propPath);
		}
		return ((ISelfFormattingPropertyValue) prop).asString();
	}

	public Object getProperty(String propPath) throws PropertyPathException {
		return getModelProperty(propPath).getValue();
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException, IllegalArgumentException {
		if(PropertyPath.isIndexed(propPath)) {
			// divert to the "physical" related many property as indexed properties
			// are "virtual"
			relatedMany(PropertyPath.deIndex(propPath)).setProperty(propPath, value);
		}
		else {
			getModelProperty(propPath).setProperty(propPath, value);
		}
	}

	/**
	 * Does a property exist?
	 * @param propPath The property path to test
	 * @return true/false
	 */
	public boolean propertyExists(String propPath) {
		try {
			return getModelProperty(propPath) != null;
		}
		catch(final PropertyPathException e) {
			return false;
		}
	}

	/**
	 * Resolves a property path to the nested {@link IModelProperty}. This is a
	 * generic way to obtain a defined model property.
	 * <p>
	 * <strong>NOTE: </strong>When a property path element having no associated
	 * property is encoutered before reaching the end of the given property path
	 * or when a given index is found out of range for an indexable property in
	 * the given property path, <code>null</code> is returned.
	 * @param propPath The property path. When <code>null</code> or empty, a
	 *        {@link RelatedOneProperty} that references <em>this</em> model is
	 *        returned.
	 * @return The resolved non-<code>null</code> model property
	 * @throws PropertyPathException When the model property can't be resolved.
	 */
	public IModelProperty getModelProperty(String propPath) throws PropertyPathException {
		return StringUtil.isEmpty(propPath) ? getSelfRef() : resolvePropertyPath(propPath);
	}

	/**
	 * Retrieves a non-relational property value from this {@link Model} given a
	 * property path. This method is behaves like
	 * {@link #getModelProperty(String)} with a filter that targets
	 * {@link IPropertyValue}s only.
	 * @param propPath Points to the desired model property
	 * @return The resolved non-<code>null</code> {@link IPropertyValue}
	 * @throws PropPathNodeMismatchException When the given property path does not
	 *         resolve to a property value.
	 * @throws PropertyPathException When the property path is mal-formed or
	 *         doesn't point to an existing model property.
	 */
	public IPropertyValue getPropertyValue(String propPath) throws PropPathNodeMismatchException, PropertyPathException {
		final IModelProperty prop = getModelProperty(propPath);
		if(prop == null) return null;
		if(!prop.getType().isValue()) {
			throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(), "value");
		}
		return (IPropertyValue) prop;
	}

	/**
	 * Extracts a nested Model from a targeted {@link IModelRefProperty}.
	 * @param propPath The property path that points to the desired model ref
	 *        property. If <code>null</code> or empty, this {@link Model} is
	 *        returned.
	 * @return The resolved non-<code>null</code> {@link Model}
	 * @throws PropertyPathException When the given property path can't be
	 *         resolved or does not map to an {@link IModelRefProperty}.
	 */
	public Model getNestedModel(String propPath) throws PropertyPathException {
		final IModelProperty prop = getModelProperty(propPath);
		assert prop != null;
		if(!prop.getType().isModelRef()) {
			throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(),
			"model reference");
		}
		return ((IModelRefProperty) prop).getModel();
	}

	/**
	 * Retrieves a related one property value from the model given a property
	 * path.
	 * @param propPath The property path (E.g.: "root.relatedModelPropName")
	 * @return The non-<code>null</code> resolved related one property.
	 * @throws PropertyPathException When the given property path can't be
	 *         resolved or does not map to related one property.
	 */
	public RelatedOneProperty relatedOne(String propPath) throws PropertyPathException {
		final IModelProperty prop = getModelProperty(propPath);
		assert prop != null;
		if(prop.getType() != PropertyType.RELATED_ONE) {
			throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(),
			"related one");
		}
		return (RelatedOneProperty) prop;
	}

	/**
	 * Retrieves a related many property value from the model given a property
	 * path.
	 * @param propPath The property path (E.g.: "root.listProperty")
	 * @return The resolved non-<code>null</code> related many property.
	 * @throws PropertyPathException When the given property path can't be
	 *         resolved or does not map to a related many property.
	 */
	public RelatedManyProperty relatedMany(String propPath) throws PropertyPathException {
		final IModelProperty prop = getModelProperty(propPath);
		assert prop != null;
		if(prop.getType() != PropertyType.RELATED_MANY) {
			throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(),
			"related many");
		}
		return (RelatedManyProperty) prop;
	}

	/**
	 * Retrieves an indexed property value from the model given a property path.
	 * This is a property value that wraps a nested Model that is a child of a
	 * related many property.
	 * @param propPath The property path. (E.g.: "root.listProperty[1]")
	 * @return The resolved non-<code>null</code> indexed property.
	 * @throws PropertyPathException When the given property path can't be
	 *         resolved or does not map to an indexed property.
	 */
	public IndexedProperty indexed(String propPath) throws PropertyPathException {
		final IModelProperty prop = getModelProperty(propPath);
		assert prop != null;
		if(prop.getType() != PropertyType.INDEXED) {
			throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(), "indexed");
		}
		return (IndexedProperty) prop;
	}

	public PropertyMetadata getPropertyMetadata(String propPath) {
		try {
			return getPropertyValue(propPath).getMetadata();
		}
		catch(final PropertyPathException e) {
			return null;
		}
	}

	/**
	 * Resolves a given property path against the hierarchy of this model throwing
	 * a specific {@link PropertyPathException} when an error occurs.
	 * @param propPath The property path
	 * @return The non-<code>null</code> resolved model property
	 * @throws PropertyPathException When an error occurrs whilst resolving the
	 *         property path or when the given property path does not resolve to
	 *         an existant property
	 */
	private IModelProperty resolvePropertyPath(final String propPath) throws PropertyPathException {
		if(StringUtil.isEmpty(propPath)) {
			throw new MalformedPropPathException("No property path specified.");
		}

		final PropertyPath pp = new PropertyPath(propPath);
		IModelProperty prop = null;
		Model model = this;
		final int len = pp.depth();
		for(int i = 0; i < len; i++) {
			final String pname = pp.nameAt(i);
			final int index;
			try {
				index = pp.indexAt(i);
			}
			catch(final IllegalArgumentException e) {
				throw new MalformedPropPathException(e.getMessage());
			}
			final boolean indexed = (index >= 0);
			final boolean atEnd = (i == len - 1);

			// find the prop val under current group
			prop = model.get(pname);
			if(prop == null) {
				if(atEnd) {
					throw new UnsetPropertyException(pp.toString());
				}
				throw new NullNodeInPropPathException(pp.toString(), pname);
			}

			// get the bound prop val type for this prop path element
			final PropertyType pvType = prop.getType();

			// non-relational prop val
			if(!pvType.isRelational()) {
				if(!atEnd) {
					throw new PropPathNodeMismatchException(pp.toString(), pname, pvType.toString(), "Relational");
				}
				return prop;
			}

			// related one prop val
			else if(pvType == PropertyType.RELATED_ONE) {
				if(indexed) {
					throw new PropPathNodeMismatchException(pp.toString(), pname, pvType.toString(), PropertyType.RELATED_MANY
							.toString());
				}
				final IModelRefProperty mrp = (IModelRefProperty) prop;
				if(atEnd) {
					// return new RelatedOneProperty(mrp.getRelatedType(),
					// mrp.getPropertyName(), mrp.isReference(), mrp.getModel());
					// TODO figure out why we were creating a *new* instance !!!!
					return mrp;
				}
				// get the nested group...
				final Model ng = mrp.getModel();
				if(ng == null) {
					throw new NullNodeInPropPathException(pp.toString(), pname);
				}
				// reset for next path
				model = ng;
			}

			// related many prop val
			else if(pvType == PropertyType.RELATED_MANY) {
				final RelatedManyProperty rmp = (RelatedManyProperty) prop;
				if(!indexed) {
					if(atEnd) {
						return rmp;
					}
					// and index is expected if were not at the end
					throw new MalformedPropPathException(pp.toString());
				}
				else if(indexed) {
					// get the nested group prop val list...
					if(atEnd) {
						if(index >= rmp.size()) {
							throw new IndexOutOfRangeInPropPathException(pp.toString(), pname, index);
						}
						return rmp.getIndexedProperty(index);
					}
					// reset for next path
					model = rmp.getIndexedProperty(index).getModel();
				}
			}
		}
		throw new MalformedPropPathException(propPath);
	}

	/**
	 * PropBinding
	 * @author jpk
	 */
	private static class PropBinding {

		Model model;

		/**
		 * Constructor
		 */
		PropBinding() {
			super();
		}

		/**
		 * Constructor
		 * @param model
		 */
		PropBinding(Model model) {
			super();
			this.model = model;
		}
	}

	/**
	 * CopyBinding - Used for deep copying {@link Model} instances.
	 * @author jpk
	 */
	private static final class CopyBinding extends PropBinding {

		Model target;

		/**
		 * Constructor
		 * @param source
		 * @param target
		 */
		CopyBinding(Model source, Model target) {
			super(source);
			this.target = target;
		}
	}

	/**
	 * BindingStack
	 * @author jpk
	 * @param <B>
	 */
	@SuppressWarnings("serial")
	static final class BindingStack<B extends PropBinding> extends ArrayList<B> {

		/**
		 * Locates an {@link PropBinding} given a model ref.
		 * @param model The sought model in this list of bindings
		 * @return The containing binding or <code>null</code> if not present.
		 */
		PropBinding find(final Model model) {
			for(final PropBinding b : this) {
				if(b.model == model) return b;
			}
			return null;
		}
	}

	/**
	 * Deep copies this instance ignoring those nested models marked as deleted.
	 * @param copyReferences Copy relational properties that are marked as
	 *        reference?
	 * @return Clone of this instance or <code>null</code> if this model is marked
	 *         as deleted and the given <code>copyMarkedDeleted</code> param is
	 *         <code>true</code>.
	 */
	public Model copy(final boolean copyReferences) {
		return copy(this, copyReferences, true, new BindingStack<CopyBinding>());
	}

	/**
	 * Deep copies this instance.
	 * @param copyReferences Copy relational properties that are marked as
	 *        reference?
	 * @param copyMarkedDeleted Copy nested models marked as deleted?
	 * @return Clone of this instance or <code>null</code> if this model is marked
	 *         as deleted and the given <code>copyMarkedDeleted</code> param is
	 *         <code>true</code>.
	 */
	public Model copy(final boolean copyReferences, boolean copyMarkedDeleted) {
		return copy(this, copyReferences, copyMarkedDeleted, new BindingStack<CopyBinding>());
	}

	/**
	 * Recursive copy routine to guard against re-copying entities
	 * @param source The model to be copied
	 * @param copyReferences Copy relational properties that are marked as
	 *        reference?
	 * @param copyMarkedDeleted Copy models marked as deleted?
	 * @param visited
	 * @return A deep copy of the model
	 */
	private static Model copy(Model source, final boolean copyReferences, final boolean copyMarkedDeleted,
			BindingStack<CopyBinding> visited) {

		if(source == null) return null;

		if(!copyMarkedDeleted && source.isMarkedDeleted()) return null;

		// check visited
		final CopyBinding binding = (CopyBinding) visited.find(source);
		if(binding != null) return binding.target;

		final Model copy = new Model(source.entityType);

		visited.add(new CopyBinding(source, copy));

		copy.markedDeleted = source.markedDeleted;

		for(final IModelProperty prop : source.props) {
			assert prop != null;

			// related one or indexed prop...
			if(prop instanceof IModelRefProperty) {
				final IModelRefProperty mrp = (IModelRefProperty) prop;
				final Model model =
					(copyMarkedDeleted || (!copyMarkedDeleted && mrp.getModel() != null && !mrp.getModel().isMarkedDeleted())) ?
							(copyReferences || !mrp.isReference()) ? copy(mrp.getModel(), copyReferences, copyMarkedDeleted, visited)
									: mrp.getModel()
									: null;
									copy.props.add(new RelatedOneProperty(mrp.getRelatedType(), mrp.getPropertyName(), mrp.isReference(), model));
			}

			// related many...
			else if(prop instanceof RelatedManyProperty) {
				final RelatedManyProperty rmp = (RelatedManyProperty) prop;
				final List<Model> list = rmp.getList();
				List<Model> nlist = null;
				if(list != null) {
					nlist = new ArrayList<Model>(list.size());
					for(final Model model : list) {
						if(copyMarkedDeleted || (!copyMarkedDeleted && !model.isMarkedDeleted())) {
							nlist
							.add((copyReferences || !rmp.isReference()) ? copy(model, copyReferences, copyMarkedDeleted, visited)
									: model);
						}
					}
				}
				copy.props.add(new RelatedManyProperty(rmp.getRelatedType(), rmp.getPropertyName(), rmp.isReference(), nlist));
			}

			// prop val..
			else {
				copy.props.add(((IPropertyValue) prop).copy());
			}
		}

		return copy;
	}

	/**
	 * Clears a single property value.
	 * @param propPath Identifies the property value to clear
	 * @throws PropertyPathException
	 */
	public void clearPropertyValue(String propPath) throws PropertyPathException {
		getPropertyValue(propPath).clear();
	}

	/**
	 * Walks the held collection of {@link IPropertyValue}s clearing then
	 * recursing as necessary to ensure all have been visited.
	 * @param clearReferences Clear valus held in related models marked as a
	 *        reference?
	 */
	public void clearPropertyValues(boolean clearReferences) {
		clearProps(this, clearReferences, new BindingStack<PropBinding>());
	}

	/**
	 * Clears all nested property values of the given model.
	 * @param model The group to be cleared
	 * @param clearReferences Clear valus held in related models marked as a
	 *        reference?
	 * @param visited
	 */
	private static void clearProps(Model model, final boolean clearReferences, BindingStack<PropBinding> visited) {

		if(model == null) return;

		// check visited
		final PropBinding binding = visited.find(model);
		if(binding != null) return;

		visited.add(new PropBinding(model));

		model.markedDeleted = false;

		for(final IModelProperty prop : model.props) {
			assert prop != null;

			// model prop (relational) val...
			if(prop instanceof IModelRefProperty) {
				final IModelRefProperty gpv = (IModelRefProperty) prop;
				if(clearReferences || !gpv.isReference()) {
					clearProps(gpv.getModel(), clearReferences, visited);
				}
			}

			// model list (relational) prop val...
			else if(prop instanceof RelatedManyProperty) {
				final RelatedManyProperty rmp = (RelatedManyProperty) prop;
				if(clearReferences || !rmp.isReference()) {
					final List<Model> list = rmp.getList();
					if(list != null) {
						for(final Model m : list) {
							clearProps(m, clearReferences, visited);
						}
					}
				}
			}

			// property values...
			else {
				((IPropertyValue) prop).clear();
			}
		}
	}

	/**
	 * @return IPropertyValue Iterator for the referenced property values for this
	 *         model.
	 */
	public Iterator<IModelProperty> iterator() {
		return props.iterator();
	}

	/**
	 * @return <code>true</code> if this group is marked for deletion.
	 */
	public boolean isMarkedDeleted() {
		return markedDeleted;
	}

	/**
	 * Sets the makred for deletion flag.
	 * @param markedDeleted
	 */
	public void setMarkedDeleted(boolean markedDeleted) {
		this.markedDeleted = markedDeleted;
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void addPropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		try {
			resolvePropertyPath(propertyName).addPropertyChangeListener(listener);
		}
		catch(final PropertyPathException e) {
			throw new IllegalArgumentException("Unable to add property change listener", e);
		}
	}

	public IPropertyChangeListener[] getPropertyChangeListeners() {
		throw new UnsupportedOperationException();
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		throw new UnsupportedOperationException();
	}

	public void removePropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		try {
			resolvePropertyPath(propertyName).removePropertyChangeListener(listener);
		}
		catch(final PropertyPathException e) {
			throw new IllegalArgumentException("Unable to remove property change listener", e);
		}
	}

	@Override
	public String descriptor() {
		return getKey().descriptor();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof Model)) return false;
		return getKey().equals(((Model) obj).getKey());
	}

	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	@Override
	public String toString() {
		return getKey().toString()/* + " [" + ((Object) this).hashCode() + ']'*/;
	}

}
