/**
 * The Logic Lab
 * @author jpk Aug 31, 2007
 */
package com.tll.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.tll.IDescriptorProvider;
import com.tll.IMarshalable;
import com.tll.IPropertyValueProvider;
import com.tll.common.bind.IBindable;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.model.CopyCriteria.CopyMode;
import com.tll.model.PropertyMetadata;
import com.tll.model.PropertyType;
import com.tll.util.Binding;
import com.tll.util.BindingRefSet;
import com.tll.util.PropertyPath;
import com.tll.util.RefSet;
import com.tll.util.StringUtil;

/**
 * Model - Encapsulates a set of {@link IModelProperty}s. This construct serves
 * to represent an entity instance object graph on the client.
 * @author jpk
 */
public final class Model implements IMarshalable, IBindable, IEntityTypeProvider, IDescriptorProvider, Iterable<IModelProperty>, IPropertyValueProvider {

	/**
	 * ModelPropSet
	 * @author jpk
	 */
	static class ModelPropSet implements Iterable<IModelProperty>, IMarshalable {

		private /*final*/ LinkedHashSet<IModelProperty> set = new LinkedHashSet<IModelProperty>();

		public ModelPropSet() {
			super();
		}

		/**
		 * Ensures the given model prop is non-null and unique by name against the
		 * existing child props (non-hierarchically).
		 * @param mp the model property to add
		 * @return true if the property was added
		 */
		public boolean add(IModelProperty mp) {
			if(mp == null) return false;
			if(mp.getPropertyName() == null) throw new IllegalArgumentException();
			// we need to ensure the name is unique among the other model props (but
			// not hierarchically)
			for(final IModelProperty emp : set) {
				if(emp.getPropertyName().equals(mp.getPropertyName())) {
					return false;
				}
			}
			return set.add(mp);
		}

		/**
		 * Sets the given {@link IPropertyValue}. If an existing model prop has the
		 * same property name, it is replaced by the one given.
		 * @param mp The {@link IModelProperty} to set
		 */
		public void set(IModelProperty mp) {
			if(mp == null) return;
			if(mp.getPropertyName() == null) throw new IllegalArgumentException();
			final IModelProperty prop = get(mp.getPropertyName());
			if(prop != mp) {
				if(prop != null) {
					set.remove(prop);
				}
				add(mp);
			}
		}

		/**
		 * Retrieves a model prop by property name
		 * @param propName
		 * @return The found model property or <code>null<code> if not found
		 */
		public IModelProperty get(String propName) {
			for(final IModelProperty m : set) {
				if(m.getPropertyName().equals(propName)) return m;
			}
			return null;
		}

		@Override
		public Iterator<IModelProperty> iterator() {
			return set.iterator();
		}

		/**
		 * @return The number of properties in the set.
		 */
		public int size() {
			return set.size();
		}

	} // ModelPropSet

	/**
	 * CopyPredicate - One impl is defined for each declared {@link CopyMode}.
	 * @author jpk
	 */
	static interface ICopyPredicate {

		/**
		 * Should the copy clone references or maintain the original model ref?
		 * <p>
		 * Used when model ref type properties are encountered
		 * @return true/false
		 */
		boolean maintainReference();

		/**
		 * Evaluates at the {@link Model} level the source and corresponding copy.
		 * The copy is available for modification as well.
		 * @param source the source model
		 * @param copy the being copied to
		 * @return <code>true</code> if the given source model's properties should
		 *         be iterated for copy.
		 */
		boolean evaluateSourceAndCopy(Model source, Model copy);

		/**
		 * Evaluatea a source model and its corresponding root model relative path
		 * for copying.
		 * @param srcProp the source model prop ref
		 * @param rootRelPath the root relative path of the given source model
		 *        property
		 * @return <code>true</code> if the given model property should be copied.
		 */
		boolean evaluateProperty(IModelProperty srcProp, String rootRelPath);
	} // ICopyPredicate

	/**
	 * WhitelistElement
	 * @author jpk
	 */
	static class WhitelistElement {

		IModelProperty srcProp;
		String rootRelPath, nearestParentRefPath;

		/**
		 * Constructor
		 * @param srcProp
		 * @param rootRelPath
		 * @param nearestParentRefPath
		 */
		WhitelistElement(IModelProperty srcProp, String rootRelPath, String nearestParentRefPath) {
			super();
			this.srcProp = srcProp;
			this.rootRelPath = rootRelPath;
			this.nearestParentRefPath = nearestParentRefPath;
			// Log.debug("ChangesPredicate elm: " + this);
		}

		@Override
		public String toString() {
			return "srcProp: " + srcProp + ", rootRelPath: " + rootRelPath + ", nearestParentRefPath: "
					+ nearestParentRefPath;
		}
	} // WhitelistElement

	/**
	 * AllPropsPredicate
	 * @author jpk
	 */
	static class AllPropsPredicate implements ICopyPredicate {

		@Override
		public boolean evaluateSourceAndCopy(Model source, Model copy) {
			copy.markedDeleted = source.markedDeleted;
			return true; // no-op
		}

		@Override
		public boolean maintainReference() {
			return false; // default is to clone even references
		}

		@Override
		public boolean evaluateProperty(IModelProperty srcProp, String rootRelPath) {
			return true; // default
		}

	} // AllPropsPredicate

	/**
	 * NoReferencesPredicate
	 * @author jpk
	 */
	static class NoReferencesPredicate extends AllPropsPredicate {

		@Override
		public boolean evaluateProperty(IModelProperty srcProp, String rootRelPath) {
			if(srcProp instanceof IModelRefProperty) {
				return !((IModelRefProperty) srcProp).isReference();
			}
			return true; // default
		}

	} // NoReferencesPredicate

	/**
	 * KeepReferencesPredicate
	 * @author jpk
	 */
	static class KeepReferencesPredicate extends AllPropsPredicate {

		@Override
		public boolean maintainReference() {
			return true;
		}

	} // NoReferencesPredicate

	/**
	 * SubsetPredicate
	 * @author jpk
	 */
	@SuppressWarnings("serial")
	class SubsetPredicate extends HashSet<WhitelistElement> implements ICopyPredicate {

		/**
		 * Constructor
		 * @param whitelistModelProps
		 */
		public SubsetPredicate(Set<IModelProperty> whitelistModelProps) {
			super();
			// create whitelist elements needed for the main copy routine
			if(whitelistModelProps != null) {
				for(final IModelProperty mp : whitelistModelProps) {
					final String rootRelPath = getRelPath(mp);
					String nearestParentRefPath;
					if(mp instanceof IModelRefProperty || mp instanceof IndexedProperty) {
						nearestParentRefPath = rootRelPath;
					}
					else {
						// resolve the nearest parent (relational or indexed prop)
						final PropertyPath pp = new PropertyPath(rootRelPath);
						if(pp.depth() > 1) {
							nearestParentRefPath = pp.trim(1);
						}
						else {
							nearestParentRefPath = "";
						}
					}
					add(new WhitelistElement(mp, rootRelPath, nearestParentRefPath));
				}
			}
		}

		@Override
		public boolean maintainReference() {
			return false;
		}

		@Override
		public boolean evaluateSourceAndCopy(Model source, Model copy) {
			// core model props to always have
			copy.markedDeleted = source.markedDeleted;
			copy.setId(source.getId());
			copy.setVersion(source.getVersion());
			return true;
		}

		/**
		 * We copy relational props if the are parent to targeted white list props
		 * @param rootRelPath the current root relative path to check for copying
		 * @return true/false
		 */
		public boolean evaluateProperty(IModelProperty srcProp, String rootRelPath) {
			if(size() > 0) {
				final boolean relational = srcProp.getType().isRelational();
				final boolean indexed = srcProp.getType() == PropertyType.INDEXED;
				for(final WhitelistElement wle : this) {
					if(relational || indexed) {
						if(wle.nearestParentRefPath.indexOf(rootRelPath) == 0) {
							//Log.debug("Allowing relational/indexed [" + rootRelPath + "]");
							return true;
						}
					}
					else {
						if(wle.rootRelPath.equals(rootRelPath) || rootRelPath.endsWith(ID_PROPERTY)
								|| rootRelPath.endsWith(VERSION_PROPERTY)) {
							//Log.debug("Allowing value/nested [" + rootRelPath + "]");
							return true;
						}
					}
				}
			}
			return false;
		}

	} // SubsetPredicate

	@SuppressWarnings("serial")
	class ChangesPredicate extends SubsetPredicate {

		/**
		 * Constructor
		 * @param whitelistModelProps
		 */
		public ChangesPredicate(Set<IModelProperty> whitelistModelProps) {
			super(whitelistModelProps);
		}

		@Override
		public boolean evaluateSourceAndCopy(Model source, Model copy) {
			super.evaluateSourceAndCopy(source, copy);
			// when in change mode, only copy id and version when a model is marked as
			// deleted
			if(source.markedDeleted) {
				return false;
			}
			return true;
		}

		/**
		 * We copy relational props if the are parent to targeted white list props
		 * @param rootRelPath the current root relative path to check for copying
		 * @return true/false
		 */
		@Override
		public boolean evaluateProperty(IModelProperty srcProp, String rootRelPath) {
			if(srcProp instanceof IModelRefProperty) {
				final Model m = ((IModelRefProperty) srcProp).getModel();
				if(m != null && m.isMarkedDeleted()) {
					// Log.debug("Allowing model ref [" + rootRelPath + "]");
					return true;
				}
			}
			if(srcProp instanceof IPropertyValue) {
				final PropertyMetadata metadata = ((IPropertyValue) srcProp).getMetadata();
				if(metadata != null && metadata.isManaged()) return false;
			}
			return super.evaluateProperty(srcProp, rootRelPath);
		}

	} // ChangesPredicate

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
	 * Utility method to find a {@link Model} instance in a collection given its
	 * key. This is handy since we only do <em>physical</em> equality on
	 * {@link Model} instances.
	 * @param mclc the model collection to search
	 * @param key identifies the model being sought in the collection
	 * @return the found instance or <code>null</code> if not found in the
	 *         collection.
	 */
	public static Model findInCollection(Collection<Model> mclc, ModelKey key) {
		if(mclc == null) return null;
		for(Model m : mclc) {
			if(m.getKey().equals(key)) return m;
		}
		return null;
	}

	/**
	 * Resolves the root relative property path of a given model property
	 * descendant.
	 * @param descendant
	 * @param current
	 * @param parentPath
	 * @param visited
	 * @return the root relative path or <code>null</code> if not a descendant.
	 */
	private static String resolveModelProperty(final IModelProperty descendant, IModelProperty current,
			String parentPath, RefSet<Model> visited) {

		if(current.getType().isModelRef()) {
			final Model m = ((IModelRefProperty) current).getModel();
			if(visited.exists(m)) return null;
			if(!visited.add(m)) throw new IllegalStateException();
		}

		final String cpp = PropertyPath.getPropertyPath(parentPath, current.getPropertyName());
		// Log.debug("resolveModelProperty() current prop path: " + cpp);

		if(descendant == current) return cpp;

		if(current instanceof RelatedOneProperty) {
			final RelatedOneProperty rop = (RelatedOneProperty) current;
			final Model m = rop.getModel();
			if(m != null) {
				for(final IModelProperty mp : m) {
					final String path = resolveModelProperty(descendant, mp, cpp, visited);
					if(path != null) return path;
				}
			}
		}
		else if(current instanceof RelatedManyProperty) {
			final RelatedManyProperty rmp = (RelatedManyProperty) current;
			for(final IndexedProperty ip : rmp) {
				final String ipath = PropertyPath.index(cpp, ip.getIndex());
				if(ip == descendant) return ipath;
				for(final IModelProperty mp : ip.getModel()) {
					final String path = resolveModelProperty(descendant, mp, ipath, visited);
					if(path != null) return path;
				}
			}
		}

		// not a descendant!
		return null;
	}

	/**
	 * Recursive copy routine to guard against re-copying entities.
	 * <p>
	 * <b>NOTE: </b>A model prop whitelist trumps the references flag!
	 * @param parentPropPath the parent property path relative to the "root" model
	 * @param source The model to be copied
	 * @param cp the copy predicate that filters what is copied
	 * @param visited list of {@link Binding} where the binding source is the
	 *        model source and the target is the model copy
	 * @return the copied model
	 */
	private static Model copy(String parentPropPath, final Model source, ICopyPredicate cp,
			final BindingRefSet<Model, Model> visited) {

		if(source == null) return null;

		Model copy = null;

		// check visited
		Binding<Model, Model> b = visited.findBindingBySource(source);
		if(b != null) {
			// Log.debug("Already visited target: " + b.tgt);
			return b.tgt;
		}
		copy = new Model(source.entityType);
		b = new Binding<Model, Model>(source, copy);
		if(!visited.add(b)) throw new IllegalStateException();

		if(!cp.evaluateSourceAndCopy(source, copy)) {
			return copy;
		}

		for(final IModelProperty srcprop : source.props) {
			assert srcprop != null;

			final String crntPropPath = PropertyPath.getPropertyPath(parentPropPath, srcprop.getPropertyName());
			// Log.debug("copy() Checking prop: " + crntPropPath);

			if(!cp.evaluateProperty(srcprop, crntPropPath)) {
				continue;
			}

			// related one
			if(srcprop instanceof RelatedOneProperty) {
				final IModelRefProperty mrp = (IModelRefProperty) srcprop;
				final Model srcModel = mrp.getModel();
				boolean keepRef = mrp.isReference() ? cp.maintainReference() : false;
				final Model cpyModel = srcModel == null ? null : keepRef ? srcModel : copy(crntPropPath, srcModel, cp, visited);
				copy.set(new RelatedOneProperty(mrp.getRelatedType(), cpyModel, mrp.getPropertyName(), mrp.isReference()));
			}

			// related many
			else if(srcprop instanceof RelatedManyProperty) {
				final RelatedManyProperty rmp = (RelatedManyProperty) srcprop;
				final ArrayList<Model> clist = new ArrayList<Model>(rmp.size());
				for(final IndexedProperty ip : rmp) {
					final String ipath = PropertyPath.index(crntPropPath, ip.getIndex());
					if(cp.evaluateProperty(ip, ipath)) {
						final Model im = ip.getModel();
						boolean keepRef = ip.isReference() ? cp.maintainReference() : false;
						final Model cim = keepRef ? im : copy(ipath, im, cp, visited);
						if(cim != null) clist.add(cim);
					}
				}
				copy.set(new RelatedManyProperty(rmp.getRelatedType(), rmp.getPropertyName(), rmp.isReference(), clist));
			}

			// value or nested..
			else {
				assert srcprop.getType().isValue() || srcprop.getType().isNested();
				copy.set(((IPropertyValue) srcprop).copy());
			}
		} // loop

		return copy;
	}

	/**
	 * Clears all nested property values of the given model.
	 * @param model The group to be cleared
	 * @param clearReferences Clear valus held in related models marked as a
	 *        reference?
	 * @param visited
	 */
	private static void clearProps(Model model, final boolean clearReferences, final boolean retainIdAndVersion,
			RefSet<Model> visited) {

		if(model == null) return;

		// check visited
		if(visited.exists(model)) return;
		if(!visited.add(model)) throw new IllegalStateException();

		model.markedDeleted = false;

		for(final IModelProperty prop : model.props) {
			assert prop != null;

			// model prop (relational) val...
			if(prop instanceof IModelRefProperty) {
				final IModelRefProperty gpv = (IModelRefProperty) prop;
				if(clearReferences || !gpv.isReference()) {
					clearProps(gpv.getModel(), clearReferences, retainIdAndVersion, visited);
				}
			}

			// model list (relational) prop val...
			else if(prop instanceof RelatedManyProperty) {
				final RelatedManyProperty rmp = (RelatedManyProperty) prop;
				if(clearReferences || !rmp.isReference()) {
					final List<Model> list = rmp.getModelList();
					if(list != null) {
						for(final Model m : list) {
							clearProps(m, clearReferences, retainIdAndVersion, visited);
						}
					}
				}
			}

			// property values...
			else {
				if(!retainIdAndVersion
						|| (retainIdAndVersion && (!ID_PROPERTY.equals(prop.getPropertyName()) && !VERSION_PROPERTY.equals(prop
								.getPropertyName())))) ((IPropertyValue) prop).clear();
			}
		}
	}

	/**
	 * The set of model properties. <br>
	 * NOTE: can't mark as final for GWT RPC compatibility
	 */
	private /*final*/ModelPropSet props = new ModelPropSet();

	/**
	 * The bound entity type.
	 */
	private IEntityType entityType;

	/**
	 * The marked deleted flag. When <code>true</code>, this indicates this model
	 * data is scheduled for deletion.
	 */
	boolean markedDeleted;

	/**
	 * Self-reference expressed as an {@link IModelRefProperty} which is
	 * instantiated and cached upon demand.
	 */
	RelatedOneProperty selfRef;

	/**
	 * Constructor
	 */
	public Model() {
		super();
	}

	/**
	 * Constructor
	 * @param entityType required since we depend on the model key for equality!
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
		return getVersion() == null;
	}

	/**
	 * @return the {@link #ID_PROPERTY} value which may be <code>null</code> if
	 *         this is scalar data.
	 */
	public String getId() {
		final StringPropertyValue prop = (StringPropertyValue) get(ID_PROPERTY);
		return prop == null ? null : prop.getString();
	}

	/**
	 * Sets the id property creating it if not present.
	 * @param id
	 */
	public void setId(String id) {
		setProperty(ID_PROPERTY, id, PropertyType.STRING);
	}

	/**
	 * @return the {@link #NAME_PROPERTY} value which may be <code>null</code>.
	 */
	public String getName() {
		final StringPropertyValue prop = (StringPropertyValue) get(NAME_PROPERTY);
		return prop == null ? null : prop.getString();
	}

	/**
	 * Sets the name property creating it if not present.
	 * @param name
	 */
	public void setName(String name) {
		setProperty(NAME_PROPERTY, name, PropertyType.STRING);
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
	 * @return the {@link #VERSION_PROPERTY} value which may be <code>null</code>
	 *         if this is scalar data.
	 */
	public String getVersion() {
		final StringPropertyValue prop = (StringPropertyValue) get(VERSION_PROPERTY);
		return prop == null ? null : prop.getString();
	}

	public void setVersion(String version) {
		setProperty(VERSION_PROPERTY, version, PropertyType.STRING);
	}

	/**
	 * @return the {@link ModelKey} for this instance.
	 */
	public ModelKey getKey() {
		return new ModelKey(entityType, getId(), getName());
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
		return props.get(name);
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
		props.set(mprop);
	}

	public Object getProperty(String propPath) throws PropertyPathException {
		return getModelProperty(propPath).getValue();
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException, IllegalArgumentException {
		setPropertyInternal(propPath, value, null);
	}

	/*
	 * IPropertyValueProvider implementation
	 */
	@Override
	public Object getPropertyValue(String propertyPath) {
		try {
			return getProperty(propertyPath);
		}
		catch(PropertyPathException e) {
			throw new IllegalArgumentException("Unable to get property value: " + e.getMessage(), e);
		}
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

	/**
	 * Same as {@link #setProperty(String, Object)} but no
	 * {@link PropertyPathException} is raised on error rather an un-checked
	 * exception.
	 * @param propPath property identifying path
	 * @param value property value to set
	 * @param ptype the expected property type
	 * @throws IllegalArgumentException When setting the property fails
	 */
	public void setProperty(String propPath, Object value, PropertyType ptype) throws IllegalArgumentException {
		try {
			setPropertyInternal(propPath, value, ptype);
		}
		catch(final PropertyPathException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Same as {@link #setPropertyInternal(String, Object, PropertyType)} but when
	 * a
	 * @param propPath
	 * @param value
	 * @throws IllegalArgumentException When setting the string property fails
	 */
	public void setString(String propPath, String value) throws IllegalArgumentException {
		setProperty(propPath, value, PropertyType.STRING);
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
	public IPropertyValue getValueProperty(String propPath) throws PropPathNodeMismatchException, PropertyPathException {
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
	 * @return The resolved {@link Model} or <code>null</code> if the model
	 *         property doesn't exist or it does and the held model ref is
	 *         <code>null</code>
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to an {@link IModelRefProperty}.
	 */
	public Model nestedModel(String propPath) throws IllegalArgumentException {
		try {
			final IModelProperty prop = getModelProperty(propPath);
			assert prop != null;
			if(!prop.getType().isModelRef()) {
				throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(),
						"model reference");
			}
			return ((IModelRefProperty) prop).getModel();
		}
		catch(final UnsetPropertyException e) {
			return null;
		}
		catch(final NullNodeInPropPathException e) {
			return null;
		}
		catch(final PropertyPathException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Retrieves a related one property value from the model given a property
	 * path.
	 * @param propPath The property path (E.g.: "root.relatedModelPropName")
	 * @return The resolved related one property or <code>null</code> if it
	 *         doesn't exist
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to related one property.
	 */
	public RelatedOneProperty relatedOne(String propPath) throws IllegalArgumentException {
		try {
			final IModelProperty prop = getModelProperty(propPath);
			assert prop != null;
			if(prop.getType() != PropertyType.RELATED_ONE) {
				throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(),
						"related one");
			}
			return (RelatedOneProperty) prop;
		}
		catch(final UnsetPropertyException e) {
			return null;
		}
		catch(final NullNodeInPropPathException e) {
			return null;
		}
		catch(final PropertyPathException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Retrieves a related many property value from the model given a property
	 * path.
	 * @param propPath The property path (E.g.: "root.listProperty")
	 * @return The resolved related many property or <code>null</code> if it
	 *         doesn't exist
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to a related many property.
	 */
	public RelatedManyProperty relatedMany(String propPath) throws IllegalArgumentException {
		try {
			final IModelProperty prop = getModelProperty(propPath);
			assert prop != null;
			if(prop.getType() != PropertyType.RELATED_MANY) {
				throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(),
						"related many");
			}
			return (RelatedManyProperty) prop;
		}
		catch(final UnsetPropertyException e) {
			return null;
		}
		catch(final NullNodeInPropPathException e) {
			return null;
		}
		catch(final PropertyPathException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Retrieves an indexed property value from the model given a property path.
	 * This is a property value that wraps a nested Model that is a child of a
	 * related many property.
	 * @param propPath The property path. (E.g.: "root.listProperty[1]")
	 * @return The resolved indexed property or <code>null</code> if it doesn't
	 *         exist.
	 * @throws IllegalArgumentException When the given property path can't be
	 *         resolved or does not map to an indexed property.
	 */
	public IndexedProperty indexed(String propPath) throws IllegalArgumentException {
		try {
			final IModelProperty prop = getModelProperty(propPath);
			assert prop != null;
			if(prop.getType() != PropertyType.INDEXED) {
				throw new PropPathNodeMismatchException(propPath, prop.getPropertyName(), prop.getType().toString(), "indexed");
			}
			return (IndexedProperty) prop;
		}
		catch(final UnsetPropertyException e) {
			return null;
		}
		catch(final NullNodeInPropPathException e) {
			return null;
		}
		catch(final PropertyPathException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public PropertyMetadata getPropertyMetadata(String propPath) {
		try {
			return getValueProperty(propPath).getMetadata();
		}
		catch(final PropertyPathException e) {
			return null;
		}
	}

	/**
	 * Calculates the property path of the given descendant model property.
	 * @param descendant required model prop ref
	 * @return the non-<code>null</code> property path that resolves to the given
	 *         descendant. An empty string is returned if the given model property
	 *         refers to *this* model instance.
	 * @throws IllegalArgumentException When the given model property is not a
	 *         descendant of this model instance.
	 */
	public String getRelPath(IModelProperty descendant) throws IllegalArgumentException {
		if(descendant == null) throw new IllegalArgumentException("Null descendant arg");
		if(descendant == selfRef) return "";
		if(descendant instanceof IModelRefProperty) {
			if(((IModelRefProperty) descendant).getModel() == this) return "";
		}
		final String s = resolveModelProperty(descendant, getSelfRef(), "", new RefSet<Model>());
		if(s == null)
			throw new IllegalArgumentException("[" + descendant + "] is not a descendant of model: [" + this + "]");
		return s;
	}

	/**
	 * Deep copies this instance creating a new distinct model instance containing
	 * a sub-set of properties that match the given criteria.
	 * @param criteria the copy criteria
	 * @return Clone of this instance or <code>null</code> if this model is marked
	 *         as deleted and the given <code>copyMarkedDeleted</code> param is
	 *         <code>true</code>.
	 */
	public Model copy(final CopyCriteria criteria) {
		ICopyPredicate cp;
		switch(criteria.getMode()) {
			case SUBSET:
				cp = new SubsetPredicate(criteria.getWhitelistProps());
				break;
			case CHANGES:
				cp = new ChangesPredicate(criteria.getWhitelistProps());
				break;
			case NO_REFERENCES:
				cp = new NoReferencesPredicate();
				break;
			case KEEP_REFERENCES:
				cp = new KeepReferencesPredicate();
				break;
			default:
			case ALL:
				cp = new AllPropsPredicate();
				break;
		}
		return copy(null, this, cp, new BindingRefSet<Model, Model>());
	}

	/**
	 * Clears a single property value.
	 * @param propPath Identifies the property value to clear
	 * @throws PropertyPathException
	 */
	public void clearPropertyValue(String propPath) throws PropertyPathException {
		getValueProperty(propPath).clear();
	}

	/**
	 * Walks the held collection of {@link IPropertyValue}s clearing then
	 * recursing as necessary to ensure all have been visited.
	 * @param clearReferences Clear valus held in related models marked as a
	 *        reference?
	 * @param retainIdAndVersion
	 */
	public void clearPropertyValues(boolean clearReferences, boolean retainIdAndVersion) {
		clearProps(this, clearReferences, retainIdAndVersion, new RefSet<Model>());
	}

	/**
	 * Non-hierarchical iteration.
	 * @return An iterator traversing only the immediate child model propertis of
	 *         this intance.
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

	/**
	 * @return The number of immediate (non-hierarchical) child properties.
	 */
	public int size() {
		return props.size();
	}

	// Don't rely on logical equals since we want to support scalar data in a
	// Model instance!
	// so we need to be careful and do manual checking for model collections when
	// searching for a particular instance
	/*
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
	 */

	@Override
	public String toString() {
		return getKey().toString()/* + " [" + ((Object) this).hashCode() + ']'*/;
	}

	private IModelRefProperty getSelfRef() {
		if(selfRef == null) {
			selfRef = new RelatedOneProperty(entityType, this, null, true);
		}
		return selfRef;
	}

	/**
	 * Sets a model properties' value conditionally creating the wrapping model
	 * property if not present.
	 * @param propPath
	 * @param value
	 * @param ptype if specified, the property will be automatically created if it
	 *        doesn't exist
	 * @throws PropertyPathException
	 * @throws IllegalArgumentException
	 */
	private void setPropertyInternal(String propPath, Object value, PropertyType ptype) throws PropertyPathException,
			IllegalArgumentException {
		try {
			IModelProperty mprop = null;
			if(PropertyPath.isIndexed(propPath)) {
				// divert to the "physical" related many property as indexed properties
				// are "virtual"
				mprop = relatedMany(PropertyPath.deIndex(propPath));
			}
			else {
				mprop = getModelProperty(propPath);
			}
			mprop.setProperty(propPath, value);
		}
		catch(final UnsetPropertyException e) {
			if(ptype != null) {
				// create it
				final IPropertyValue pv = AbstractPropertyValue.create(ptype, e.parentProperty, null);
				pv.setValue(value);
				e.parentModel.set(pv);
			}
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

			// find the prop val under current model
			prop = model.get(pname);
			if(prop == null) {
				if(atEnd) {
					throw new UnsetPropertyException(pp.toString(), model, pname);
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
					// an index is expected if we're not at the end
					throw new MalformedPropPathException(pp.toString());
				}
				else if(indexed) {
					// get the nested group prop val list...
					if(index >= rmp.size()) {
						throw new IndexOutOfRangeInPropPathException(pp.toString(), pname, index);
					}
					if(atEnd) {
						return rmp.getIndexedProperty(index);
					}
					// reset for next path
					model = rmp.getIndexedProperty(index).getModel();
				}
			}
		}
		throw new MalformedPropPathException(propPath);
	}

}
