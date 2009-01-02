package com.tll.client.bind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.tll.client.ui.IBoundWidget;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;

/**
 * Binding - This class represents a DataBinding between two objects. It also
 * supports Child bindings.
 * <p>
 * <b>IMPT:</b> This construct currently does <em>not</em> support adding or
 * removing relational type properties! In other words, the assumption in this
 * class' logic is always a non-<code>null</code> ref on bound properties.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
// TODO enable support for relational/index removal and addition (i.e.: support
// null as a new property value).
public final class Binding {

	/**
	 * DefaultPropertyChangeListener
	 * @author jpk
	 */
	private static final class DefaultPropertyChangeListener implements IPropertyChangeListener {

		private final BindingInstance instance;
		private final BindingInstance target;
		private ValidationException lastException;

		/**
		 * Constructor
		 * @param instance
		 * @param target
		 */
		DefaultPropertyChangeListener(BindingInstance instance, BindingInstance target) {
			this.instance = instance;
			this.target = target;
		}

		public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
			Object value = propertyChangeEvent.getNewValue();

			if(instance.validator != null) {
				try {
					value = instance.validator.validate(value);
				}
				catch(ValidationException ve) {
					if(instance.feedback != null) {
						if(lastException != null) {
							instance.feedback.resolve(propertyChangeEvent.getSource());
						}
						instance.feedback.handleException(propertyChangeEvent.getSource(), ve);
						lastException = ve;
					}
					else {
						lastException = ve;
						throw new RuntimeException(ve);
					}
				}
			}

			if(instance.feedback != null) {
				instance.feedback.resolve(propertyChangeEvent.getSource());
			}

			lastException = null;

			if(instance.converter != null) {
				value = instance.converter.convert(value);
			}

			try {
				target.object.setProperty(target.property, value);
			}
			catch(Exception e) {
				throw new RuntimeException("Unable to set property: " + target.property, e);
			}
		}

		@Override
		public String toString() {
			return "[Listener on : " + instance.object + " ] ";
		}
	} // DefaultPropertyChangeListener

	/**
	 * NestedPropertyChangeListener
	 * @author jpk
	 */
	private final class NestedPropertyChangeListener implements IPropertyChangeListener {

		BindingInstance target;
		IBindable sourceObject;
		String propertyName;
		IBindable[] parents;
		String[] propertyNames;

		NestedPropertyChangeListener(BindingInstance target, IBindable sourceObject, String propertyName) {
			this.target = target;
			this.sourceObject = sourceObject;
			this.propertyName = propertyName;
		}

		public void propertyChange(PropertyChangeEvent evt) {

			if(bound) {
				unbind();
				bound = true;
			}

			BindingInstance newInstance = createBindingInstance(sourceObject, propertyName);

			bound = newInstance.object != null;

			target.object = newInstance.object;
			target.nestedListener = newInstance.nestedListener;

			if(lastSet == Boolean.TRUE) {
				setLeft();
			}
			else if(lastSet == Boolean.FALSE) {
				setRight();
			}

			if(bound) {
				bind();
			}
		}

		public void setup() {
			for(int i = 0; i < parents.length; i++) {
				parents[i].addPropertyChangeListener(propertyNames[i], this);
			}
		}

		public void cleanup() {
			for(int i = parents.length - 1; i >= 0; i--) {
				parents[i].removePropertyChangeListener(propertyNames[i], this);
			}
		}
	} // NestedPropertyChangeListener

	private BindingInstance left;
	private BindingInstance right;
	private List<Binding> children;

	/**
	 * TRUE = left; FALSE = right;
	 */
	private Boolean lastSet = null;
	private boolean bound = false;

	/**
	 * Constructor - Creates an empty Binding object. This is mostly useful for
	 * top-of-tree parent Bindings.
	 */
	public Binding() {
		super();
	}

	/**
	 * Constructor
	 * @param left The left hand object.
	 * @param leftProperty Property on the left object.
	 * @param right The right hand object
	 * @param rightProperty Property on the right object.
	 */
	public Binding(IBindable left, String leftProperty, IBindable right, String rightProperty) {
		this(left, leftProperty, null, null, null, right, rightProperty, null, null, null);
	}

	/**
	 * Constructor
	 * @param left The left hand object.
	 * @param right The right hand object
	 * @param property The common property name for <em>both</em> the left and
	 *        right objects.
	 */
	public Binding(IBindable left, IBindable right, String property) {
		this(left, property, right, property);
	}

	/**
	 * Constructor
	 * @param left The left hand object.
	 * @param leftProperty The property of the left hand object.
	 * @param leftValidator A validator for the left hand property.
	 * @param leftFeedback Feedback for the left hand validator.
	 * @param right The right hand object.
	 * @param rightProperty The property on the right hand object
	 * @param rightValidator IValidator for the right hand property.
	 * @param rightFeedback Feedback for the right hand validator.
	 */
	public Binding(IBindable left, String leftProperty, IValidator leftValidator, IValidationFeedback leftFeedback,
			IBindable right, String rightProperty, IValidator rightValidator, IValidationFeedback rightFeedback) {
		this(left, leftProperty, null, leftValidator, leftFeedback, right, rightProperty, null, rightValidator,
				rightFeedback);
	}

	/**
	 * Constructor
	 * @param left The left hand object.
	 * @param leftValidator A validator for the left hand property.
	 * @param leftFeedback Feedback for the left hand validator.
	 * @param right The right hand object.
	 * @param rightValidator IValidator for the right hand property.
	 * @param rightFeedback Feedback for the right hand validator.
	 * @param property The common property name for <em>both</em> the left and
	 *        right objects.
	 */
	public Binding(IBindable left, IValidator leftValidator, IValidationFeedback leftFeedback, IBindable right,
			IValidator rightValidator, IValidationFeedback rightFeedback, String property) {
		this(left, property, leftValidator, leftFeedback, right, property, rightValidator, rightFeedback);
	}

	/**
	 * Constructor
	 * @param left
	 * @param leftProperty
	 * @param leftConverter
	 * @param right
	 * @param rightProperty
	 * @param rightConverter
	 */
	public Binding(IBindable left, String leftProperty, IConverter<Object, Object> leftConverter, IBindable right,
			String rightProperty, IConverter<Object, Object> rightConverter) {
		this(left, leftProperty, leftConverter, null, null, right, rightProperty, rightConverter, null, null);
	}

	/**
	 * Constructor
	 * @param left
	 * @param leftConverter
	 * @param right
	 * @param rightConverter
	 * @param property The common property name for <em>both</em> the left and
	 *        right objects.
	 */
	public Binding(IBindable left, IConverter<Object, Object> leftConverter, IBindable right, String rightProperty,
			IConverter<Object, Object> rightConverter, String property) {
		this(left, property, leftConverter, right, property, rightConverter);
	}

	/**
	 * Creates a new binding. This method is a shorthand for working with
	 * BoundWidgets. The bound widget provided will become the left-hand binding
	 * and the bound widget's model property becomes the right-hand binding.
	 * @param widget IBoundWidget containing the model.
	 * @param validator A validator for the BouldWidget's value property.
	 * @param feedback A feedback implementation for validation errors.
	 * @param property The common property name for <em>both</em> the left and
	 *        right objects.
	 */
	public Binding(IBoundWidget<?, ?, ?> widget, IValidator validator, IValidationFeedback feedback, String property) {
		this(widget, property, null, validator, feedback, widget.getModel(), property, null, null, null);
	}

	/**
	 * Creates a new binding. This method is a shorthand for working with
	 * BoundWidgets. The bound widget provided will become the left-hand binding
	 * and the bound widget's model property becomes the right hand binding.
	 * @param widget IBoundWidget containing the model.
	 * @param property The common property name for <em>both</em> the left and
	 *        right objects.
	 */
	public Binding(IBoundWidget<?, ?, ?> widget, String property) {
		this(widget, property, null, null, null, widget.getModel(), property, null, null, null);
	}

	/**
	 * Constructor - The base constructor.
	 * @param left
	 * @param leftProperty
	 * @param leftConverter
	 * @param leftValidator
	 * @param leftFeedback
	 * @param right
	 * @param rightProperty
	 * @param rightConverter
	 * @param rightValidator
	 * @param rightFeedback
	 */
	private Binding(IBindable left, String leftProperty, IConverter<Object, Object> leftConverter,
			IValidator leftValidator, IValidationFeedback leftFeedback, IBindable right, String rightProperty,
			IConverter<Object, Object> rightConverter, IValidator rightValidator, IValidationFeedback rightFeedback) {

		this.left = createBindingInstance(left, leftProperty);
		this.left.converter = leftConverter;
		this.left.validator = leftValidator;
		this.left.feedback = leftFeedback;

		this.right = createBindingInstance(right, rightProperty);
		this.right.converter = rightConverter;
		this.right.validator = rightValidator;
		this.right.feedback = rightFeedback;

		this.left.listener = new DefaultPropertyChangeListener(this.left, this.right);
		this.right.listener = new DefaultPropertyChangeListener(this.right, this.left);
	}

	/**
	 * Returns a list of child Bindings.
	 * @return List of child bindings.
	 */
	public List<Binding> getChildren() {
		return children = (children == null) ? new ArrayList<Binding>() : children;
	}

	/**
	 * Sets the left hand property to the current value of the right.
	 */
	public void setLeft() {
		if((left != null) && (right != null)) {
			try {
				Object source = right.object == null ? this : right.object;
				Object pv = right.object == null ? null : right.object.getProperty(right.property);
				right.listener.propertyChange(new PropertyChangeEvent(source, right.property, null, pv));
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			children.get(i).setLeft();
		}

		lastSet = Boolean.TRUE;
	}

	/**
	 * Sets the right object's property to the current value of the left.
	 */
	public void setRight() {
		if((left != null) && (right != null)) {
			try {
				Object source = left.object == null ? this : left.object;
				Object pv = left.object == null ? null : left.object.getProperty(left.property);
				left.listener.propertyChange(new PropertyChangeEvent(source, left.property, null, pv));
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			children.get(i).setRight();
		}

		lastSet = Boolean.FALSE;
	}

	/**
	 * Establishes a two-way binding between the objects.
	 */
	public void bind() {
		if((left != null) && (right != null)) {
			left.object.addPropertyChangeListener(left.property, left.listener);

			if(left.nestedListener != null) {
				left.nestedListener.setup();
			}

			right.object.addPropertyChangeListener(right.property, right.listener);

			if(right.nestedListener != null) {
				right.nestedListener.setup();
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			children.get(i).bind();
		}

		bound = true;
	}

	/**
	 * Breaks the two way binding and removes all listeners.
	 */
	public void unbind() {
		if((left != null) && (right != null)) {
			left.object.removePropertyChangeListener(left.property, left.listener);

			if(left.nestedListener != null) {
				left.nestedListener.cleanup();
			}

			right.object.removePropertyChangeListener(right.property, right.listener);

			if(right.nestedListener != null) {
				right.nestedListener.cleanup();
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			children.get(i).unbind();
		}

		bound = false;
	}

	/**
	 * Validates <em>all</em> bindings in this binding.
	 * @return boolean indicating this binding is valid.
	 */
	public boolean validate() {
		boolean valid = true;

		if((left != null) && (right != null)) {
			if(left.validator != null) {
				try {
					left.validator.validate(left.object.getProperty(left.property));
				}
				catch(ValidationException ve) {
					valid = false;

					if(left.feedback != null) {
						left.feedback.handleException(left.object, ve);
					}

					if(left.listener instanceof DefaultPropertyChangeListener) {
						((DefaultPropertyChangeListener) left.listener).lastException = ve;
					}
				}
				catch(Exception e) {
					valid = false;
					// Binding.LOGGER.log(Level.WARN, null, e);
					GWT.log("Binding warning", e);
				}
			}

			if(right.validator != null) {
				try {
					right.validator.validate(right.object.getProperty(right.property));
				}
				catch(ValidationException ve) {
					valid = false;

					if(right.feedback != null) {
						right.feedback.handleException(right.object, ve);
					}

					if(right.listener instanceof DefaultPropertyChangeListener) {
						((DefaultPropertyChangeListener) right.listener).lastException = ve;
					}
				}
				catch(Exception e) {
					valid = false;
					// Binding.LOGGER.log(Level.WARN, null, e);
					GWT.log("Binding warning", e);
				}
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			Binding child = children.get(i);
			valid = valid & child.validate();
		}

		return valid;
	}

	/**
	 * Validates stopping at the first found invalid binding.
	 * @return boolean indicating if all values are valid.
	 */
	public boolean isValid() {
		try {
			if((left != null) && (right != null)) {
				if(left.validator != null) {
					left.validator.validate(left.object.getProperty(left.property));
				}

				if(right.validator != null) {
					right.validator.validate(right.object.getProperty(right.property));
				}
			}

			boolean valid = true;

			for(int i = 0; (children != null) && (i < children.size()); i++) {
				valid = valid & children.get(i).isValid();
			}

			return valid;
		}
		catch(ValidationException ve) {
			// Binding.LOGGER.log(Level.INFO, "Invalid", ve);
			GWT.log("Binding validation info", ve);

			return false;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the left hand BindingInstance.
	 * @return Returns the left hand BindingInstance.
	 */
	public BindingInstance getLeft() {
		return left;
	}

	/**
	 * Returns the right hand BindingInstance.
	 * @return Returns the left hand BindingInstance.
	 */
	public BindingInstance getRight() {
		return right;
	}

	/**
	 * Creates an instance of {@link BindingInstance}.
	 * @param object
	 * @param propertyName
	 * @return A new {@link BindingInstance}.
	 */
	private BindingInstance createBindingInstance(IBindable object, String propertyName) {
		int dotIndex = propertyName.indexOf(".");
		BindingInstance instance = new BindingInstance();

		NestedPropertyChangeListener rtpcl =
				(dotIndex == -1) ? null : new NestedPropertyChangeListener(instance, object, propertyName);

		if(dotIndex != -1) {
			ArrayList<IBindable> parents = new ArrayList<IBindable>();
			ArrayList<String> propertyNames = new ArrayList<String>();

			while(dotIndex != -1) {
				String pname = propertyName.substring(0, dotIndex);
				propertyName = propertyName.substring(dotIndex + 1);
				parents.add(object);

				try {
					String discriminator = null;
					int descIndex = pname.indexOf("[");

					if(descIndex != -1) {
						discriminator = pname.substring(descIndex + 1, pname.indexOf("]", descIndex));
						pname = pname.substring(0, descIndex);
					}

					propertyNames.add(pname);

					if(discriminator != null) {
						// TODO Need a loop here to handle multi-dimensional/collections of
						// collections
						object = getDiscriminatedObject(object.getProperty(pname), discriminator);
					}
					else {
						object = (IBindable) object.getProperty(pname);
					}
				}
				catch(ClassCastException cce) {
					throw new RuntimeException("Nonbindable sub property: " + object + " . " + pname, cce);
				}
				catch(Exception e) {
					throw new RuntimeException(e);
				}

				dotIndex = propertyName.indexOf(".");
			}

			rtpcl.parents = new IBindable[parents.size()];
			parents.toArray(rtpcl.parents);
			rtpcl.propertyNames = new String[propertyNames.size()];
			propertyNames.toArray(rtpcl.propertyNames);
		}

		instance.object = object;
		instance.property = propertyName;
		instance.nestedListener = rtpcl;

		return instance;
	}

	@SuppressWarnings("unchecked")
	private IBindable getDiscriminatedObject(Object collectionOrArray, String discriminator) {
		int equalsIndex = discriminator.indexOf("=");

		if(collectionOrArray instanceof Collection && (equalsIndex == -1)) {
			return getBindableAtCollectionIndex((Collection<IBindable>) collectionOrArray, Integer.parseInt(discriminator));
		}
		else if(collectionOrArray instanceof Collection) {
			return getBindableHavingPropertyOfValue((Collection<IBindable>) collectionOrArray, discriminator.substring(0,
					equalsIndex), discriminator.substring(equalsIndex + 1));
		}
		else if(collectionOrArray instanceof Map) {
			return getBindableAtMapKey((Map) collectionOrArray, discriminator);
		}
		else if(equalsIndex == -1) {
			return ((IBindable[]) collectionOrArray)[Integer.parseInt(discriminator)];
		}
		else {
			return getBindableHavingPropertyOfValue((IBindable[]) collectionOrArray, discriminator.substring(0, equalsIndex),
					discriminator.substring(equalsIndex + 1));
		}
	}

	private IBindable getBindableHavingPropertyOfValue(IBindable[] array, String propertyName, String stringValue) {
		for(IBindable b : array) {
			try {
				if((b.getProperty(propertyName) + "").equals(stringValue)) {
					return b;
				}
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("No Object matching " + propertyName + "=" + stringValue);
	}

	private IBindable getBindableHavingPropertyOfValue(Iterable<IBindable> itr, String propertyName, String stringValue) {
		for(IBindable b : itr) {
			try {
				if((b.getProperty(propertyName) + "").equals(stringValue)) {
					return b;
				}
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

		throw new RuntimeException("No Object matching " + propertyName + "=" + stringValue);
	}

	private IBindable getBindableAtMapKey(Map<String, IBindable> map, String key) {
		IBindable result = null;

		for(Iterator<Entry<String, IBindable>> it = map.entrySet().iterator(); it.hasNext();) {
			Entry<String, IBindable> e = it.next();
			if(e.getKey().toString().equals(key)) {
				result = e.getValue();
				break;
			}
		}

		return result;
	}

	private IBindable getBindableAtCollectionIndex(Iterable<IBindable> collection, int index) {
		int i = 0;
		IBindable result = null;

		for(Iterator<IBindable> it = collection.iterator(); it.hasNext() && (i <= index); i++) {
			result = it.next();
			return result;
		}

		throw new IndexOutOfBoundsException("Binding discriminator too high: " + index);
	}

	/**
	 * A data class containing the relevant data for one half of a binding
	 * relationship.
	 */
	private static final class BindingInstance {

		/**
		 * The Object being bound.
		 */
		public IBindable object;

		/**
		 * The full property path of the property being bound.
		 */
		public String property;

		/**
		 * A converter when needed.
		 */
		public IConverter<Object, Object> converter;

		/**
		 * A IValidator object when needed.
		 */
		public IValidator validator;

		/**
		 * A IValidationFeedback object when needed.
		 */
		public IValidationFeedback feedback;

		private IPropertyChangeListener listener;

		private NestedPropertyChangeListener nestedListener;

		/**
		 * Constructor
		 */
		private BindingInstance() {
			super();
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if(obj == null) {
			return false;
		}

		final Binding other = (Binding) obj;
		if(left != other.left && (left == null || !left.equals(other.left))) {
			return false;
		}
		if(right != other.right && (right == null || !right.equals(other.right))) {
			return false;
		}
		return true;
	}

	/**
	 * @return int based on hash of the two objects being bound.
	 */
	@Override
	public int hashCode() {
		return right.object.hashCode() ^ left.object.hashCode();
	}

	/**
	 * @return String value representing the binding.
	 */
	@Override
	public String toString() {
		return "Binding [ \n + " + right.object + " \n " + left.object + "\n ]";
	}
}
