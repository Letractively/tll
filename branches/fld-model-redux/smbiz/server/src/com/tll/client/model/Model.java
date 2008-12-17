/**
 * The Logic Lab
 * @author jpk Aug 31, 2007
 */
package com.tll.client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tll.client.util.StringUtil;
import com.tll.model.EntityType;
import com.tll.model.schema.PropertyType;

/**
 * Model - Encapsulates a set of {@link IModelProperty}s. This construct serves
 * to represent an entity instance object graph on the client.
 * @author jpk
 */
public final class Model implements IData, Iterable<IModelProperty> {

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
	 * Set of {@link IPropertyValue}s holding the actual model data.
	 */
	private Set<IModelProperty> props = new HashSet<IModelProperty>();

	/**
	 * The entity type.
	 */
	private EntityType entityType;

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
	public Model(EntityType entityType) {
		super();
		this.entityType = entityType;
	}

	/**
	 * @return the ref type
	 */
	public EntityType getEntityType() {
		return entityType;
	}

	/**
	 * Provides the <em>unique</em> {@link RefKey} for this model/entity instance.
	 */
	public RefKey getRefKey() {
		Integer id = null;
		IModelProperty pvId = get(ID_PROPERTY);
		if(pvId != null && pvId.getType() == PropertyType.INT) {
			id = ((IntPropertyValue) pvId).getInteger();
		}

		String name = null;
		IModelProperty pvName = get(NAME_PROPERTY);
		if(pvName != null && pvName.getType() == PropertyType.STRING) {
			name = ((StringPropertyValue) pvName).getString();
		}

		// NOTE: we don't enforce/check id validity here as we may be a copy or a
		// cleared model!
		assert /*(id != null) && */(entityType != null);
		return new RefKey(entityType, id, name);
	}

	/**
	 * @return Reference to <em>this</em> Model expressed as an
	 *         {@link IModelProperty}.
	 */
	public RelatedOneProperty getSelfRef() {
		if(selfRef == null) {
			selfRef = new RelatedOneProperty(getEntityType(), null, true, this);
		}
		return selfRef;
	}

	/**
	 * Finds a property value in this model's collection of property values given
	 * a non-path property name. (No property path resolution is performed.)
	 * @param name The non-path property name (i.e. no dots)
	 * @return The found {@link IPropertyValue} or <code>null</code> if not
	 *         present.
	 */
	public IModelProperty get(String name) {
		if(name == null) return null;
		for(IModelProperty prop : props) {
			if(name.equals(prop.getPropertyName())) return prop;
		}
		return null;
	}

	/**
	 * Sets the given {@link IPropertyValue} as a child to this model with no
	 * property path resolution preformed. If an existing prop val is currently
	 * mapped to the ascribed property name, it is replaced by the one given.
	 * @param propValue The replacing {@link IPropertyValue}
	 */
	public void set(IModelProperty propValue) {
		if(propValue == null) return;
		IModelProperty prop = get(propValue.getPropertyName());
		if(prop != null) {
			props.remove(prop);
		}
		props.add(propValue);
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
		IModelProperty prop = getProperty(propPath);
		if(prop == null) return null;
		if(prop instanceof ISelfFormattingPropertyValue == false) {
			throw new IllegalArgumentException("Non self-formatting property: " + propPath);
		}
		return ((ISelfFormattingPropertyValue) prop).asString();
	}

	/**
	 * Resolves a property path to the nested {@link IModelProperty}. This is a
	 * generic way to obtain a defined model property.
	 * <p>
	 * <strong>NOTE: </strong>When a property path element having no associated
	 * property is encoutered before reaching the end of the given property path
	 * or when a given index is found out of range for an indexable property in
	 * the given property path, <code>null</code> is returned.
	 * @param propPath The property path. When <code>null</code>, a
	 *        {@link RelatedOneProperty} that references <em>this</em> model is
	 *        returned.
	 * @return The nested property or <code>null</code> if not present
	 * @throws IllegalArgumentException When the given property path is
	 *         mal-formed.
	 */
	public IModelProperty getProperty(String propPath) throws IllegalArgumentException {
		if(propPath == null || propPath.length() < 1) {
			return getSelfRef();
		}
		try {
			return resolvePropertyPath(propPath);
		}
		catch(NullNodeInPropPathException e) {
			return null;
		}
		catch(IndexOutOfRangeInPropPathException ie) {
			return null;
		}
		catch(UnsetPropertyException e) {
			return null;
		}
		catch(PropertyPathException ppe) {
			throw new IllegalArgumentException(ppe.getMessage());
		}
	}

	/**
	 * Retrieves an IPropertyValue from the model given a property path. This
	 * method is {@link #getProperty(String)} with a filter that targets on
	 * {@link IPropertyValue} type {@link IModelProperty}s.
	 * @param propPath A parsed property path
	 * @return The resolved {@link IPropertyValue} or <code>null</code> if not
	 *         set.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to an {@link IPropertyValue}.
	 */
	public IPropertyValue getPropertyValue(String propPath) throws IllegalArgumentException {
		IModelProperty prop = getProperty(propPath);
		if(prop == null) return null;
		if(!prop.getType().isValue()) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to a value property");
		}
		return (IPropertyValue) prop;
	}

	/**
	 * Extracts a nested Model from a targeted {@link IModelRefProperty}.
	 * @param propPath The property path that points to the desired model ref
	 *        property. If <code>null</code> or empty, this {@link Model} is
	 *        returned.
	 * @return The nested {@link Model}
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to an {@link IModelRefProperty}.
	 */
	public Model getNestedModel(String propPath) throws IllegalArgumentException {
		IModelProperty prop = getProperty(propPath);
		if(prop == null) return null;
		if(!prop.getType().isModelRef()) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to a model ref property");
		}
		return ((IModelRefProperty) prop).getModel();
	}

	/**
	 * Retrieves a related one property value from the model given a property
	 * path.
	 * @param propPath The property path (E.g.: "root.relatedModelPropName")
	 * @return Related one property value or <code>null</code> if not set.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to a related one property.
	 */
	public RelatedOneProperty relatedOne(String propPath) throws IllegalArgumentException {
		IModelProperty prop = getProperty(propPath);
		if(prop == null) return null;
		if(prop.getType() != PropertyType.RELATED_ONE) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to a related one property");
		}
		return (RelatedOneProperty) prop;
	}

	/**
	 * Retrieves a related many property value from the model given a property
	 * path.
	 * @param propPath The property path (E.g.: "root.listProperty")
	 * @return Related many property value or <code>null</code> if not set.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to a related many property.
	 */
	public RelatedManyProperty relatedMany(String propPath) throws IllegalArgumentException {
		IModelProperty prop = getProperty(propPath);
		if(prop == null) return null;
		if(prop.getType() != PropertyType.RELATED_MANY) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to a related many property");
		}
		return (RelatedManyProperty) prop;
	}

	/**
	 * Retrieves an indexed property value from the model given a property path.
	 * This is a property value that wraps a nested Model that is a child of a
	 * related many property.
	 * @param propPath The property path. (E.g.: "root.listProperty[1]")
	 * @return Indexed property value or <code>null</code> if not set.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to an indexed property.
	 */
	public IndexedProperty indexed(String propPath) throws IllegalArgumentException {
		IModelProperty prop = getProperty(propPath);
		if(prop == null) return null;
		if(prop.getType() != PropertyType.INDEXED) {
			throw new IllegalArgumentException("Property '" + propPath + "' does not map to an indexed property");
		}
		return (IndexedProperty) prop;
	}

	/**
	 * Is this model entity new?
	 * @return true/false
	 */
	public boolean isNew() {
		IntPropertyValue prop = (IntPropertyValue) get(VERSION_PROPERTY);
		return prop == null ? true : (prop.getInteger() == null);
	}

	/**
	 * Retrieves the id property
	 * @return The entity id
	 */
	public Integer getId() {
		IntPropertyValue prop = (IntPropertyValue) get(ID_PROPERTY);
		return prop == null ? null : prop.getInteger();
	}

	/**
	 * Retrieves the entities' name property value
	 * @return The entities' name
	 */
	public String getName() {
		StringPropertyValue prop = (StringPropertyValue) get(NAME_PROPERTY);
		return prop == null ? null : prop.getString();
	}

	/**
	 * @return The date created. May be <code>null</code> for entity types that
	 *         don't support this property.
	 */
	public Date getDateCreated() {
		DatePropertyValue prop = (DatePropertyValue) get(DATE_CREATED_PROPERTY);
		return prop == null ? null : prop.getDate();
	}

	/**
	 * @return The date last modified. May be <code>null</code> for entity types
	 *         that don't support this property.
	 */
	public Date getDateModified() {
		DatePropertyValue prop = (DatePropertyValue) get(DATE_MODIFIED_PROPERTY);
		return prop == null ? null : prop.getDate();
	}

	/**
	 * Resolves a given property path against the hierarchy of this model.
	 * @param propPath The property path
	 * @return Property path binding
	 * @throws PropertyPathException When an error occurrs whilst resolving the
	 *         property path
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
			final int index = pp.indexAt(i);
			boolean indexed = (index >= 0);
			boolean atEnd = (i == len - 1);

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
				ModelRefProperty mrp = (ModelRefProperty) prop;
				if(atEnd) {
					return new RelatedOneProperty(mrp.getRelatedType(), mrp.getPropertyName(), mrp.isReference(), mrp.getModel());
				}
				// get the nested group...
				Model ng = mrp.getModel();
				if(ng == null) {
					throw new NullNodeInPropPathException(pp.toString(), pname);
				}
				// reset for next path
				model = ng;
			}

			// related many prop val
			else if(pvType == PropertyType.RELATED_MANY) {
				RelatedManyProperty rmp = (RelatedManyProperty) prop;
				if(!indexed) {
					if(atEnd) {
						return rmp;
					}
					// and index is expected if were not at the end
					throw new MalformedPropPathException(pp.toString());
				}
				else if(indexed) {
					// get the nested group prop val list...
					List<Model> nlist = rmp.getList();
					if(nlist == null) {
						// ensure we have a list to attach indexed groups to later
						nlist = new ArrayList<Model>(0);
						rmp.setList(nlist);
					}
					if(atEnd) {
						if(index >= nlist.size()) {
							throw new IndexOutOfRangeInPropPathException(pp.toString(), pname, index);
						}
						Model ng = nlist.get(index);
						return new IndexedProperty(rmp.getRelatedType(), pp.nameAt(pp.depth() - 1), rmp.isReference(), ng, index);
					}
					// reset for next path
					model = nlist.get(index);
				}
			}
		}
		throw new MalformedPropPathException(pp.toString());
	}

	/**
	 * PropBinding
	 * @author jpk
	 */
	static class PropBinding {

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
	static final class CopyBinding extends PropBinding {

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
	 */
	@SuppressWarnings("serial")
	static final class BindingStack<B extends PropBinding> extends ArrayList<B> {

		/**
		 * Locates an {@link Binding} given a model ref.
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
	 * Deep copies this instance.
	 * @return Clone of this instance
	 */
	public Model copy() {
		return copy(this, new BindingStack<CopyBinding>());
	}

	/**
	 * Recursive copy routine to guard against re-copying entities
	 * @param source The model to be copied
	 * @param visited
	 * @return A deep copy of the model
	 */
	private static Model copy(Model source, BindingStack<CopyBinding> visited) {

		if(source == null) return null;

		// check visited
		CopyBinding binding = (CopyBinding) visited.find(source);
		if(binding != null) return binding.target;

		Model copy = new Model(source.entityType);

		visited.add(new CopyBinding(source, copy));

		copy.markedDeleted = source.markedDeleted;

		for(IModelProperty prop : source.props) {
			assert prop != null;

			// related one or indexed prop...
			if(prop instanceof ModelRefProperty) {
				ModelRefProperty mrp = (ModelRefProperty) prop;
				Model model = mrp.isReference() ? mrp.getModel() : copy(mrp.getModel(), visited);
				copy.props.add(new RelatedOneProperty(mrp.getRelatedType(), mrp.getPropertyName(), mrp.isReference(), model));
			}

			// model list relation...
			else if(prop instanceof RelatedManyProperty) {
				RelatedManyProperty rmp = (RelatedManyProperty) prop;
				List<Model> list = rmp.getList();
				List<Model> nlist = null;
				if(list != null) {
					nlist = new ArrayList<Model>(list.size());
					for(Model model : list) {
						nlist.add(rmp.isReference() ? model : copy(model, visited));
					}
				}
				copy.props.add(new RelatedManyProperty(rmp.getRelatedType(), rmp.getPropertyName(), rmp.isReference(), nlist));
			}

			// prop val..
			else {
				copy.props.add(((AbstractPropertyValue) prop).copy());
			}
		}

		return copy;
	}

	/**
	 * Walks the held collection of {@link IPropertyValue}s clearing all values
	 * recursing as necessary to ensure all have been visited.
	 */
	public void clearPropertyValues() {
		clearProps(this, new BindingStack<PropBinding>());
	}

	/**
	 * Clears all nested property values of the given model.
	 * @param model The group to be cleared
	 * @param visited
	 */
	private static void clearProps(Model model, BindingStack<PropBinding> visited) {

		if(model == null) return;

		// check visited
		PropBinding binding = visited.find(model);
		if(binding != null) return;

		visited.add(new PropBinding(model));

		// TODO do we want to reset markedDeleted?
		model.markedDeleted = false;

		for(IModelProperty prop : model.props) {
			assert prop != null;

			// model prop (relational) val...
			if(prop instanceof ModelRefProperty) {
				ModelRefProperty gpv = (ModelRefProperty) prop;
				if(!gpv.isReference()) {
					clearProps(gpv.getModel(), visited);
				}
			}

			// model list (relational) prop val...
			else if(prop instanceof RelatedManyProperty) {
				RelatedManyProperty rmp = (RelatedManyProperty) prop;
				if(!rmp.isReference()) {
					List<Model> list = rmp.getList();
					if(list != null) {
						for(Model m : list) {
							clearProps(m, visited);
						}
					}
				}
			}

			// property values...
			else {
				((AbstractPropertyValue) prop).clear();
			}
		}
	}

	/**
	 * @return IPropertyValue Iterator for the referenced property values for this
	 *         model.
	 */
	public Iterator<IModelProperty> iterator() {
		assert props != null;
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

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof Model)) return false;
		return getRefKey().equals(((Model) obj).getRefKey());
	}

	@Override
	public int hashCode() {
		return getRefKey().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("hash:");
		sb.append(hashCode());
		if(markedDeleted) {
			sb.append(" MRKD DELETED! ");
		}

		sb.append(" props[");
		for(Iterator<IModelProperty> itr = props.iterator(); itr.hasNext();) {
			IModelProperty val = itr.next();
			sb.append(val.toString());
			if(itr.hasNext()) {
				sb.append(" ");
			}
		}
		sb.append("]");

		return sb.toString();
	}

}
