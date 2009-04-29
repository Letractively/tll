package com.tll.client.bind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.convert.IConverter;
import com.tll.client.ui.IWidgetRef;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.client.validate.IErrorHandler.Attrib;
import com.tll.common.bind.IBindable;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.bind.IndexedPropertyChangeEvent;
import com.tll.common.bind.PropertyChangeEvent;
import com.tll.util.PropertyPath;

/**
 * Binding - This class represents a DataBinding between two objects. It also
 * supports Child bindings.
 * <p>
 * <b>IMPT:</b> This construct currently does <em>not</em> support adding or
 * removing relational type properties! In other words, the assumption in this
 * class' logic is always a non-<code>null</code> ref on bound properties.
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public final class Binding {

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
		 * A IErrorHandler object when needed.
		 */
		public IErrorHandler feedback;

		private IPropertyChangeListener listener;

		/**
		 * Constructor
		 */
		private BindingInstance() {
			super();
		}

	} // BindingInstance

	/**
	 * DefaultPropertyChangeListener - Listens for property changes for a property
	 * in a given <em>instance</em> and propagates these changes to a
	 * <em>targeted<em> {@link IPropertyChangeListener}.
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
				catch(final ValidationException ve) {
					if(instance.feedback != null) {
						if(lastException != null) {
							instance.feedback.resolveError((IWidgetRef) propertyChangeEvent.getSource());
						}
						instance.feedback.handleError((IWidgetRef) propertyChangeEvent.getSource(), ve.getError(), Attrib.LOCAL
								.flag());
						lastException = ve;
						return;
					}
					lastException = ve;
					throw new RuntimeException(ve);
				}
			}

			if(instance.feedback != null) {
				instance.feedback.resolveError((IWidgetRef) propertyChangeEvent.getSource());
			}

			lastException = null;

			if(instance.converter != null) {
				value = instance.converter.convert(value);
			}

			// resolve the property
			String targetProperty = target.property;
			if(propertyChangeEvent instanceof IndexedPropertyChangeEvent && !PropertyPath.isIndexed(targetProperty)) {
				targetProperty =
					PropertyPath.index(targetProperty, ((IndexedPropertyChangeEvent) propertyChangeEvent).getIndex());
			}

			Log.debug("Issuing propertyChange on [ " + target.object + " ] for " + targetProperty + "..");
			try {
				target.object.setProperty(targetProperty, value);
			}
			catch(final Exception e) {
				throw new RuntimeException("Unable to set property '" + targetProperty + "': " + e.getMessage(), e);
			}
		}

		@Override
		public String toString() {
			return "[Listener on : " + instance.object + " ] ";
		}
	} // DefaultPropertyChangeListener

	private BindingInstance left;
	private BindingInstance right;
	private List<Binding> children;

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
	 * @param right The right hand object
	 * @param property The common property name for <em>both</em> the left and
	 *        right objects.
	 * @throws RuntimeException When the binding can't be created usu. due to
	 *         missing bindable properties.
	 */
	public Binding(IBindable left, IBindable right, String property) throws RuntimeException {
		this(left, property, null, null, null, right, property, null, null, null);
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
	 * @throws RuntimeException When the binding can't be created usu. due to
	 *         missing bindable properties.
	 */
	public Binding(IBindable left, String leftProperty, IConverter<Object, Object> leftConverter,
			IValidator leftValidator,
			IErrorHandler leftFeedback, IBindable right,
			String rightProperty, IConverter<Object, Object> rightConverter, IValidator rightValidator,
			IErrorHandler rightFeedback)
	throws RuntimeException {

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
		return children =
			(children == null) ? new ArrayList<Binding>() : children;
	}

	/**
	 * Sets the left hand property to the current value of the right.
	 */
	public void setLeft() {
		if((left != null) && (right != null)) {
			Log.debug("Binding.setLeft..");
			try {
				right.listener.propertyChange(new PropertyChangeEvent(right.object, right.property, null, right.object
						.getProperty(right.property)));
			}
			catch(final Exception e) {
				throw new RuntimeException(e);
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			children.get(i).setLeft();
		}
	}

	/**
	 * Sets the right object's property to the current value of the left.
	 */
	public void setRight() {
		if((left != null) && (right != null)) {
			Log.debug("Binding.setRight..");
			try {
				left.listener.propertyChange(new PropertyChangeEvent(left.object, left.property, null, left.object
						.getProperty(left.property)));
			}
			catch(final Exception e) {
				throw new RuntimeException(e);
			}
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			children.get(i).setRight();
		}
	}

	/**
	 * Establishes a two-way binding between the objects.
	 * <p>
	 * NOTE: Addition of property change listeners to the bindable is presumed to
	 * have the smarts to discriminate on the property name and remove any
	 * existing having the same name.
	 */
	public void bind() {
		if(!bound && (left != null) && (right != null)) {
			left.object.addPropertyChangeListener(left.property, left.listener);
			right.object.addPropertyChangeListener(right.property, right.listener);
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
		if(bound && (left != null) && (right != null)) {
			left.object.removePropertyChangeListener(left.property, left.listener);
			right.object.removePropertyChangeListener(right.property, right.listener);
		}

		for(int i = 0; (children != null) && (i < children.size()); i++) {
			children.get(i).unbind();
		}

		bound = false;
	}

	/**
	 * Creates an instance of {@link BindingInstance}.
	 * @param bindable the required bindable
	 * @param propertyName the required property name
	 * @return A new {@link BindingInstance}.
	 * @throws RuntimeException When the instance can't be created
	 */
	private BindingInstance createBindingInstance(IBindable bindable, String propertyName) throws RuntimeException {
		int dotIndex = propertyName.indexOf(".");
		final BindingInstance instance = new BindingInstance();
		if(dotIndex != -1) {
			final ArrayList<IBindable> parents = new ArrayList<IBindable>();
			final ArrayList<String> propertyNames = new ArrayList<String>();

			while(dotIndex != -1) {
				String pname = propertyName.substring(0, dotIndex);
				propertyName = propertyName.substring(dotIndex + 1);
				parents.add(bindable);

				try {
					String discriminator = null;
					final int descIndex = pname.indexOf("[");

					if(descIndex != -1) {
						discriminator = pname.substring(descIndex + 1, pname.indexOf("]", descIndex));
						pname = pname.substring(0, descIndex);
					}

					propertyNames.add(pname);

					if(discriminator != null) {
						// TODO Need a loop here to handle multi-dimensional/collections of
						// collections
						bindable = getDiscriminatedObject(bindable.getProperty(pname), discriminator);
					}
					else {
						final Object nested = bindable.getProperty(pname);
						if(nested instanceof IBindable == false) {
							throw new RuntimeException("Non-bindable property: " + pname);
						}
						bindable = (IBindable) nested;
					}
				}
				catch(final ClassCastException cce) {
					throw new RuntimeException("Nonbindable sub property: " + bindable + " . " + pname, cce);
				}
				catch(final Exception e) {
					throw new RuntimeException(e);
				}

				dotIndex = propertyName.indexOf(".");
			}
		}

		if(bindable == null) {
			throw new RuntimeException("Missing bindable property: " + propertyName);
		}
		instance.object = bindable;
		instance.property = propertyName;

		return instance;
	}

	@SuppressWarnings("unchecked")
	private IBindable getDiscriminatedObject(Object collectionOrArray, String discriminator) {
		final int equalsIndex = discriminator.indexOf("=");

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
		for(final IBindable b : array) {
			try {
				if((b.getProperty(propertyName) + "").equals(stringValue)) {
					return b;
				}
			}
			catch(final Exception e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("No Object matching " + propertyName + "=" + stringValue);
	}

	private IBindable getBindableHavingPropertyOfValue(Iterable<IBindable> itr, String propertyName, String stringValue) {
		for(final IBindable b : itr) {
			try {
				if((b.getProperty(propertyName) + "").equals(stringValue)) {
					return b;
				}
			}
			catch(final Exception e) {
				throw new RuntimeException(e);
			}
		}

		throw new RuntimeException("No Object matching " + propertyName + "=" + stringValue);
	}

	private IBindable getBindableAtMapKey(Map<String, IBindable> map, String key) {
		IBindable result = null;

		for(final Iterator<Entry<String, IBindable>> it = map.entrySet().iterator(); it.hasNext();) {
			final Entry<String, IBindable> e = it.next();
			if(e.getKey().toString().equals(key)) {
				result = e.getValue();
				break;
			}
		}

		return result;
	}

	private IBindable getBindableAtCollectionIndex(Iterable<IBindable> collection, int index) {
		int i = 0;
		for(final IBindable b : collection) {
			if(i++ == index) return b;
		}
		throw new IndexOutOfBoundsException("Binding discriminator too high: " + index);
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
