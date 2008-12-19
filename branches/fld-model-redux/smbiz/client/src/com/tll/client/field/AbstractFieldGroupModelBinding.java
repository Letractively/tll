/**
 * The Logic Lab
 * @author jpk
 * Dec 14, 2008
 */
package com.tll.client.field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.IFieldBindingListener;
import com.tll.client.event.type.FieldBindingEvent;
import com.tll.client.event.type.FieldBindingEvent.FieldBindingEventType;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RelatedManyProperty;
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
	protected static final class ModelPropertyDefinition {

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
	 * FieldBinding - A coupling between an {@link IField} and a model
	 * {@link IPropertyValue} to facilite bi-directional data transfer between the
	 * two.
	 * @author jpk
	 */
	protected static final class FieldBinding {

		/**
		 * The bound field.
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
			if(field instanceof Widget == false) throw new IllegalArgumentException("The field must be a Widget.");
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
	private List<ModelPropertyDefinition> modelPropDefs;

	/**
	 * The field bindings that connect a single field to a single non-relational
	 * model property.
	 */
	private final Set<FieldBinding> set = new HashSet<FieldBinding>();

	/**
	 * Flag for tracking bound/unbound state.
	 */
	private boolean bound;

	private FieldBindingEventListenerCollection bindingListeners;

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
			clear();
			this.model = model;
		}
	}

	/**
	 * Gets either the root or a nested model.
	 * @param propPath The property resolving to the model ref property in the
	 *        root model. If <code>null</code>, the root {@link Model} is
	 *        returned.
	 * @return The resolved {@link Model}
	 */
	public Model getModel(String propPath) {
		return model.getNestedModel(propPath);
	}

	/**
	 * @return The root field group
	 */
	public FieldGroup getRootFieldGroup() {
		return fields;
	}

	/**
	 * Populates the internally held set of {@link FieldBinding}s.
	 */
	protected abstract void createFieldBindings();

	/**
	 * Binds the root {@link FieldGroup} to the root {@link Model}.
	 */
	public void bind() {
		if(bound) throw new IllegalStateException("Already bound");
		if(fields == null) throw new IllegalStateException("No root field group specified");
		if(model == null) throw new IllegalStateException("No root model specified");

		// fire before bind event
		if(bindingListeners != null)
			bindingListeners.fireOnFieldBindingEvent(new FieldBindingEvent(this, this, FieldBindingEventType.BEFORE_BIND));

		// create the field bindings
		createFieldBindings();

		// bind each field binding
		for(FieldBinding b : set) {
			b.bind();
		}

		// fire after bind event
		if(bindingListeners != null)
			bindingListeners.fireOnFieldBindingEvent(new FieldBindingEvent(this, this, FieldBindingEventType.AFTER_BIND));

		bound = true;
	}

	/**
	 * Unbinds the root {@link FieldGroup} from the root {@link Model}.
	 */
	public void unbind() {
		if(bound) {
			for(FieldBinding b : set) {
				b.unbind();
			}
			bound = false;
		}
	}

	private void addModelPropDef(ModelPropertyDefinition d) {
		if(modelPropDefs == null) {
			modelPropDefs = new ArrayList<ModelPropertyDefinition>();
		}
		modelPropDefs.add(d);
	}

	/**
	 * Adds a related one model property defintion for a model property which may
	 * <em>not</em> be present. This definition is accessed if necessary when
	 * transferring data to the model.
	 * @param modelPropertyPath Relative to the root model, this param resolves to
	 *        the property definition
	 * @param modelType The related one model type
	 */
	protected final void addRelatedOnePropertyDefinition(String modelPropertyPath, EntityType modelType) {
		addModelPropDef(new ModelPropertyDefinition(modelPropertyPath, PropertyType.RELATED_ONE, modelType));
	}

	/**
	 * Adds a related many model property defintion in the same manner as a
	 * related one model property is added.
	 * @param modelPropertyPath Relative to the root model, this param resolves to
	 *        the property definition
	 * @param modelType The model type of the indexed model properties under this
	 *        related many property
	 * @see #addRelatedOnePropertyDefinition(String, EntityType)
	 */
	protected final void addRelatedManyPropertyDefinition(String modelPropertyPath, EntityType modelType) {
		addModelPropDef(new ModelPropertyDefinition(modelPropertyPath, PropertyType.RELATED_MANY, modelType));
	}

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

	/**
	 * Binds the given model to the given fields then adds the fields to the root
	 * field group.
	 * @param indexFields The fields to bind to the indexed model
	 * @param relatedManyPropPath The path relative to the root field group that
	 *        resolves to the related many model property to which the indexed
	 *        model property will be added
	 * @param model The model subject to binding
	 * @return The property path of the bound indexed model
	 */
	public final String bindIndexedModel(FieldGroup indexFields, String relatedManyPropPath, Model model) {
		final RelatedManyProperty rmp = model.relatedMany(relatedManyPropPath);
		FieldBinding fb;

		// determine the indexed property path
		final int index = rmp == null ? 0 : rmp.size();
		final String indexedPropPath = PropertyPath.index(relatedManyPropPath, index);

		IPropertyValue pv;
		final PropertyPath pp = new PropertyPath();
		for(IField f : indexFields) {
			pp.parse(f.getPropertyName());
			f.setPropertyName(PropertyPath.getPropertyPath(relatedManyPropPath, pp.last()));
			pv = model.getPropertyValue(f.getPropertyName());
			if(pv != null) {
				fb = new FieldBinding(f, pv);
				addBinding(fb);
				fb.push();
			}
		}

		return indexedPropPath;
	}

	/**
	 * Unbinds either a single field or field group.
	 * @param field The field to unbind
	 */
	protected final void unbindField(IField field) {
		if(field == null || field == fields) return;

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

	/**
	 * Adds a field binding resolving the model property given a model property
	 * path.
	 * @param modelPropertyPath The property path targeting the model property in
	 *        the given {@link Model}
	 */
	protected final void addBinding(IField field, String modelPropertyPath) {
		addBinding(field, model.getPropertyValue(PropertyPath.getPropertyPath(modelPropertyPath, field.getPropertyName())));
	}

	/**
	 * Adds a field binding given a field ref and model property ref.
	 * @param field The resolved field to be bound
	 * @param prop The resolved model property
	 */
	protected void addBinding(IField field, IPropertyValue prop) {
		addBinding(new FieldBinding(field, prop));
	}

	/**
	 * Adds a {@link FieldBinding}.
	 * @param b The {@link FieldBinding} to add
	 */
	protected void addBinding(FieldBinding b) {
		set.add(b);
	}

	/**
	 * Data transfer (model -> field).
	 */
	public final void setFieldValues() {
		if(!bound) throw new IllegalStateException("Unable to set field values: field/model binding not bound");
		for(FieldBinding b : set) {
			b.push();
		}
	}

	/**
	 * Data transfer (field -> model).
	 */
	public final void setModelValues() {
		if(!bound) throw new IllegalStateException("Unable to set model values: field/model binding not bound");
		// iterate the bound fields
		for(FieldBinding b : set) {
			b.pull();
		}
	}

	/**
	 * Unbinds then removes all field bindings.
	 */
	protected final void clear() {
		unbind();
		set.clear();
	}
}
