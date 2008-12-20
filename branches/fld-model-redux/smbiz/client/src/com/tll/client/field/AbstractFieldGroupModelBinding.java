/**
 * The Logic Lab
 * @author jpk
 * Dec 14, 2008
 */
package com.tll.client.field;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.event.IFieldBindingListener;
import com.tll.client.event.type.FieldBindingEvent;
import com.tll.client.event.type.FieldBindingEvent.FieldBindingEventType;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.MalformedPropPathException;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.PropertyPathException;
import com.tll.client.model.RelatedManyProperty;
import com.tll.client.model.UnsetPropertyException;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.BooleanValidator;
import com.tll.client.validate.CharacterValidator;
import com.tll.client.validate.DateValidator;
import com.tll.client.validate.DecimalValidator;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.IntegerValidator;
import com.tll.model.EntityType;
import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * AbstractFieldGroupModelBinding - Base class for all declared field/model
 * binding definitions.
 * @author jpk
 */
public abstract class AbstractFieldGroupModelBinding implements IFieldGroupModelBinding {

	/**
	 * ModelPropertyDefinition - Defines a model property at a given property path
	 * relative to a given model.
	 * @author jpk
	 */
	/*private*/static final class ModelPropertyDefinition {

		/**
		 * The property path identifying the target model property.
		 */
		String propertyPath;

		/**
		 * The property type of the targeted model property.
		 */
		PropertyType propType;

		/**
		 * Relevant only for relational model properties and identifies the ascribed
		 * model type for that property.
		 */
		EntityType entityType;

		/**
		 * Constructor
		 * @param propertyPath The property path that locates the model property
		 *        relative to a root model
		 * @param propType The property type
		 * @param entityType The model type when identifying relational model
		 *        properties
		 */
		public ModelPropertyDefinition(String propertyPath, PropertyType propType, EntityType entityType) {
			super();
			this.propertyPath = propertyPath;
			this.propType = propType;
			this.entityType = entityType;
		}
	} // ModelPropertyDefinition

	/**
	 * FieldBinding - A coupling between a non-group {@link IField} and a model
	 * {@link IPropertyValue} to facilite bi-directional data transfer between the
	 * two.
	 * @author jpk
	 */
	protected static final class FieldBinding {

		/**
		 * The bound, non-group field.
		 */
		private final IField field;

		/**
		 * The bound model property.
		 */
		private final IPropertyValue prop;

		/**
		 * The validator added to the field as a result of binding.
		 */
		private IValidator boundValidator;

		/**
		 * Constructor
		 * @param field The required field to bind
		 * @param prop The required model property to bind
		 */
		public FieldBinding(IField field, IPropertyValue prop) {
			if(field == null) throw new IllegalArgumentException("A field must be specified.");
			if(field instanceof FieldGroup) throw new IllegalArgumentException("The field can't be a group.");
			if(prop == null) throw new IllegalArgumentException("A model property must be specified.");
			this.field = field;
			this.prop = prop;
		}

		private GlobalFormat getFieldFormat() {
			return (field instanceof HasFormat) ? ((HasFormat) field).getFormat() : null;
		}

		/**
		 * Binds an {@link IField} to a model {@link IPropertyValue}.
		 */
		void bind() {
			assert field != null && prop != null;

			boolean required;
			int maxlen;

			// process model prop metadata
			final PropertyMetadata metadata = prop.getMetadata();
			if(metadata != null) {
				required = metadata.isRequired() && !metadata.isManaged();
				maxlen = metadata.getMaxLen();

				// set the binding (type coercion) validator
				switch(metadata.getPropertyType()) {
					case BOOL:
						field.addValidator(BooleanValidator.INSTANCE);
						break;
					case CHAR:
						field.addValidator(CharacterValidator.INSTANCE);
						break;
					case DATE: {
						switch(getFieldFormat()) {
							case DATE:
								field.addValidator(DateValidator.DATE_VALIDATOR);
								break;
							case TIME:
								field.addValidator(DateValidator.TIME_VALIDATOR);
								break;
							default:
							case TIMESTAMP:
								field.addValidator(DateValidator.TIMESTAMP_VALIDATOR);
								break;
						}
						break;
					}
					case FLOAT:
					case DOUBLE: {
						switch(getFieldFormat()) {
							case CURRENCY:
								field.addValidator(DecimalValidator.CURRENCY_VALIDATOR);
								break;
							case PERCENT:
								field.addValidator(DecimalValidator.PERCENT_VALIDATOR);
								break;
							case DECIMAL:
								field.addValidator(DecimalValidator.DECIMAL_VALIDATOR);
								break;
						}
						break;
					}
					case INT:
					case LONG:
						field.addValidator(IntegerValidator.INSTANCE);
						break;

					case ENUM:
					case STRING:
						// no type coercion validator needed
						break;

					case STRING_MAP:
						// TODO impl
						throw new UnsupportedOperationException();

					default:
						throw new IllegalStateException("Unhandled model property type: " + metadata.getPropertyType().name());
				}
			}
			else {
				required = false;
				maxlen = -1;
			}

			field.setRequired(required);
			if(field instanceof HasMaxLength) {
				((HasMaxLength) field).setMaxLen(maxlen);
			}
		}

		void unbind() {
			if(boundValidator != null) {
				field.removeValidator(boundValidator);
				boundValidator = null;
			}
		}

		/**
		 * Data transfer (model -> field).
		 */
		void push() {
			// string-ize model prop value
			final String sval = Fmt.format(prop.getValue(), getFieldFormat());
			// assign it to the field
			field.setResetValue(sval);
			field.setValue(sval);
		}

		/**
		 * Data transfer (field -> model).
		 */
		void pull() {
			prop.setValue(field.getValidatedValue());
		}
	} // FieldBinding

	/**
	 * FieldBindingSet - Aggregates {@link FieldBinding} instances.
	 * @author jpk
	 */
	private static final class FieldBindingSet extends HashSet<FieldBinding> {

	} // FieldBindingSet

	/**
	 * BindingInstance
	 * @author jpk
	 */
	private static final class BindingInstance {

		IField field;
		Model model;
		FieldBindingSet set;

		/**
		 * Constructor
		 * @param field The group or non-group field
		 * @param model The model
		 * @param set The set of bindings between the given field and model
		 */
		public BindingInstance(IField field, Model model, FieldBindingSet set) {
			super();
			this.field = field;
			this.model = model;
			this.set = set;
		}
	}

	/**
	 * The root field group.
	 */
	private FieldGroup fields;

	/**
	 * The root model.
	 */
	private Model model;

	/**
	 * List of model property definitions serving to create model properties as
	 * necessary during data transfer.
	 */
	// private List<ModelPropertyDefinition> modelPropDefs;
	/**
	 * The field bindings that connect a single field to a single non-relational
	 * model property.
	 */
	private final FieldBindingSet set = new FieldBindingSet();

	/**
	 * Flag for tracking bound/unbound state.
	 */
	private boolean bound;

	/**
	 * The binding listeners.
	 */
	private FieldBindingEventListenerCollection bindingListeners;

	/**
	 * Map of scheduled bindings keyed by property path that is relative to the
	 * root model.
	 */
	private Map<String, BindingInstance> scheduledBindings;

	/**
	 * Constructor
	 */
	public AbstractFieldGroupModelBinding() {
		super();
	}

	public void addFieldBindingEventListener(IFieldBindingListener listener) {
		if(bindingListeners == null) {
			bindingListeners = new FieldBindingEventListenerCollection();
		}
		bindingListeners.add(listener);
	}

	public void removeFieldBindingEventListener(IFieldBindingListener listener) {
		if(bindingListeners != null) {
			bindingListeners.remove(listener);
		}
	}

	private void scheduleBinding(String rootPropertyPath, BindingInstance binding) {
		if(scheduledBindings == null) scheduledBindings = new HashMap<String, BindingInstance>();
		scheduledBindings.put(rootPropertyPath, binding);
	}

	/**
	 * Unschedules a scheduled binding.
	 * @param rootPropertyPath The identifying property path relative to the root
	 *        model
	 * @return <code>true</code> if the binding was present and removed
	 *         successfully, <code>false</code> otherwise.
	 */
	private boolean unscheduleBinding(String rootPropertyPath) {
		if(scheduledBindings != null) {
			scheduledBindings.remove(rootPropertyPath);
		}
		return false;
	}

	public void setRootFieldGroup(FieldGroup fields) {
		if(fields == null) {
			throw new IllegalStateException("A root field group must be specified.");
		}
		if(this.fields == fields) return;
		if(bound && this.fields != null) unbind();
		this.fields = fields;
	}

	public void setRootModel(Model model) {
		if(model == null) {
			throw new IllegalStateException("A root model must be specified.");
		}
		if(this.model != model) {
			unbind();
			this.model = model;
		}
	}

	public FieldGroup getRootFieldGroup() {
		return fields;
	}

	/**
	 * Sub-classes must implement this method which is use-case dependent.
	 * @param modelType The target model type
	 * @return The resolved model or <code>null</code>
	 * @throws IllegalArgumentException When the given model type is not supported
	 *         by this binding.
	 */
	protected abstract Model doResolveModel(EntityType modelType) throws IllegalArgumentException;

	public Model resolveModel(EntityType type) throws IllegalArgumentException, IllegalStateException {
		final Model m = doResolveModel(type);
		if(m == null) throw new IllegalStateException("Unresolvable model: " + type.getName());
		return m;
	}

	/**
	 * Gets a nested model given a property path.
	 * @param propPath The property path
	 * @return The resolved model or <code>null</code> if non-existant under the
	 *         root model.
	 * @throws IllegalArgumentException When the property path is mal-formed.
	 */
	protected Model getModel(String propPath) throws IllegalArgumentException {
		try {
			return model.getNestedModel(propPath);
		}
		catch(MalformedPropPathException e) {
			throw new IllegalArgumentException(e);
		}
		catch(PropertyPathException e) {
			return null;
		}
	}

	public void bind() {
		if(bound)
			throw new IllegalStateException("Already bound");
		else if(fields == null)
			throw new IllegalStateException("No root field group specified");
		else if(model == null) throw new IllegalStateException("No root model specified");

		// fire before bind event
		if(bindingListeners != null)
			bindingListeners.fireOnFieldBindingEvent(new FieldBindingEvent(this, this, FieldBindingEventType.BEFORE_BIND));

		// create the field bindings
		createFieldBindings(fields);

		// bind each field binding
		for(FieldBinding b : set) {
			b.bind();
		}

		// fire after bind event
		if(bindingListeners != null)
			bindingListeners.fireOnFieldBindingEvent(new FieldBindingEvent(this, this, FieldBindingEventType.AFTER_BIND));

		bound = true;
	}

	public void unbind() {
		if(bound) {
			for(FieldBinding b : set) {
				b.unbind();
			}
			set.clear();
			bound = false;
		}
	}

	/**
	 * Populates the internally held set of {@link FieldBinding}s.
	 */
	private void createFieldBindings(FieldGroup fg) {
		assert bound == true;

		// iterate the root field group and create field bindings for all found
		// non-group fields
		for(IField f : fields) {
			if(f instanceof FieldGroup) {
				createFieldBindings((FieldGroup) f);
			}
			else {
				try {
					addFieldBinding(f, model.getPropertyValue(f.getPropertyName()));
				}
				catch(MalformedPropPathException e) {
					throw new IllegalArgumentException("Unable to create field bindings", e);
				}
				catch(PropertyPathException e) {
					// skip this field
				}
			}
		}
	}

	/**
	 * Adds a field binding resolving the model property given a model property
	 * path.
	 * @param modelPropertyPath The property path targeting the model property in
	 *        the given {@link Model}
	 */
	/*
	public final void addFieldBinding(IField field, String modelPropertyPath) throws PropertyPathException {
		addFieldBinding(field, model.getPropertyValue(PropertyPath.getPropertyPath(modelPropertyPath, field
				.getPropertyName())));
	}
	*/

	/**
	 * Adds a field binding given a field ref and model property ref.
	 * @param field The resolved field to be bound
	 * @param prop The resolved model property
	 */
	protected final void addFieldBinding(IField field, IPropertyValue prop) {
		set.add(new FieldBinding(field, prop));
	}

	public final String bindIndexedModel(IField field, String relatedManyPropPath, EntityType modelType) {

		// resolve the related many property in the root model
		RelatedManyProperty rmp;
		try {
			rmp = model.relatedMany(relatedManyPropPath);
		}
		catch(UnsetPropertyException e) {
			// TODO create this if not present as we don't want to rely on the server
			// side binding process to put this there if there aren't initially at
			// least one indexed model under this property
			throw new IllegalArgumentException(e);
		}
		catch(PropertyPathException e) {
			throw new IllegalArgumentException(e);
		}
		assert rmp != null;

		FieldBinding fb;

		// determine the indexed property path
		final int index = rmp.size();
		final String indexedPropPath = PropertyPath.index(relatedManyPropPath, index);

		// get fresh prototype model instance
		Model proto = AuxDataCache.instance().getEntityPrototype(modelType);
		if(proto == null) throw new IllegalStateException("Unable to obtain model prototype");

		// iterate through the given fields
		final FieldBindingSet bs = new FieldBindingSet();
		IPropertyValue pv;
		final PropertyPath pp = new PropertyPath();
		if(field instanceof FieldGroup) {
			for(IField f : (FieldGroup) field) {
				pp.parse(f.getPropertyName());
				f.setPropertyName(PropertyPath.getPropertyPath(relatedManyPropPath, pp.last()));
				try {
					pv = model.getPropertyValue(f.getPropertyName());
					fb = new FieldBinding(f, pv);
					bs.add(fb);
				}
				catch(UnsetPropertyException e) {
					// ok don't bind this field
				}
				catch(PropertyPathException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		else {
			try {
				pv = model.getPropertyValue(field.getPropertyName());
				fb = new FieldBinding(field, pv);
				bs.add(fb);
			}
			catch(UnsetPropertyException e) {
				// ok don't bind this field
			}
			catch(PropertyPathException e) {
				throw new IllegalArgumentException(e);
			}
		}

		// bind each created field binding
		for(FieldBinding b : bs) {
			b.bind();
			// b.push(); // TODO do we push here ??
		}

		// now "schedule" the created binding set
		scheduleBinding(indexedPropPath, new BindingInstance(field, proto, bs));

		return indexedPropPath;
	}

	/**
	 * Unbinds either a single field or field group.
	 * @param field The field to unbind
	 */
	public final void unbindField(IField field) {
		if(field == null || field == fields) return;

		// first check the scheduled bindings
		if(unscheduleBinding(field.getPropertyName())) return;

		// remove field bindings
		if(field instanceof FieldGroup) {
			FieldGroup fg = (FieldGroup) field;
			for(IField f : fg) {
				if(f instanceof FieldGroup) {
					unbindField(f);
				}
				else {
					for(FieldBinding b : set) {
						if(b.field == f) {
							set.remove(f);
						}
					}
				}
			}
		}
		else {
			for(FieldBinding b : set) {
				if(b.field == field) {
					set.remove(field);
				}
			}
		}
	}

	public void markModelDeleted() {
	}

	public void unmarkModelDeleted() {
	}

	/**
	 * Data transfer to/from field/model.
	 * @param push push (model -> field) or pull (model <- field)?
	 */
	private void xfr(boolean push) {
		if(!bound) throw new IllegalStateException("Unable to perform data transfer: field/model binding not bound");

		// xfr on the un-scheduled
		for(FieldBinding b : set) {
			if(push)
				b.push();
			else
				b.pull();
		}

		// xfr on the scheduled
		if(scheduledBindings != null) {
			for(BindingInstance bi : scheduledBindings.values()) {
				for(FieldBinding b : bi.set) {
					if(push)
						b.push();
					else
						b.pull();
				}
			}
		}
	}

	public final void setFieldValues() {
		xfr(true);
	}

	public final void setModelValues() {
		xfr(false);
	}

	/*
	private void addModelPropDef(ModelPropertyDefinition d) {
		if(modelPropDefs == null) {
			modelPropDefs = new ArrayList<ModelPropertyDefinition>();
		}
		modelPropDefs.add(d);
	}
	*/

	/**
	 * Adds a related one model property defintion for a model property which may
	 * <em>not</em> be present. This definition is accessed if necessary when
	 * transferring data to the model.
	 * @param modelPropertyPath Relative to the root model, this param resolves to
	 *        the property definition
	 * @param modelType The related one model type
	 */
	/*
	protected final void addRelatedOnePropertyDefinition(String modelPropertyPath, EntityType modelType) {
		addModelPropDef(new ModelPropertyDefinition(modelPropertyPath, PropertyType.RELATED_ONE, modelType));
	}
	*/

	/**
	 * Adds a related many model property defintion in the same manner as a
	 * related one model property is added.
	 * @param modelPropertyPath Relative to the root model, this param resolves to
	 *        the property definition
	 * @param modelType The model type of the indexed model properties under this
	 *        related many property
	 * @see #addRelatedOnePropertyDefinition(String, EntityType)
	 */
	/*
	protected final void addRelatedManyPropertyDefinition(String modelPropertyPath, EntityType modelType) {
		addModelPropDef(new ModelPropertyDefinition(modelPropertyPath, PropertyType.RELATED_MANY, modelType));
	}
	*/

	/**
	 * List of pending relational property additions.
	 */
	// private Set<String> pendingRelationalPropertyAdditions;
	/**
	 * List of pending relational property deletions.
	 */
	// private Set<String> pendingRelationalPropertyDeletions;
	/*
	private void addPendingRelationalPropertyAddition(String propPath) {
		if(pendingRelationalPropertyAdditions == null) {
			pendingRelationalPropertyAdditions = new HashSet<String>();
		}
		pendingRelationalPropertyAdditions.add(propPath);
	}

	private void removePendingRelationalPropertyAddition(String propPath) {
		if(pendingRelationalPropertyAdditions != null) {
			pendingRelationalPropertyAdditions.remove(propPath);
		}
	}

	private void addPendingRelationalPropertyDeletion(String propPath) {
		if(pendingRelationalPropertyDeletions == null) {
			pendingRelationalPropertyDeletions = new HashSet<String>();
		}
		pendingRelationalPropertyDeletions.add(propPath);
	}

	private void removePendingRelationalPropertyDeletion(String propPath) {
		if(pendingRelationalPropertyDeletions != null) {
			pendingRelationalPropertyDeletions.remove(propPath);
		}
	}
	*/
}
