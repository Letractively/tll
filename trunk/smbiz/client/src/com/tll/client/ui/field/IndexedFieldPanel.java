/**
 * The Logic Lab
 * @author jpk
 * Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.Binding;
import com.tll.client.bind.IBindable;
import com.tll.client.model.NullNodeInPropPathException;
import com.tll.client.model.PropertyPathException;
import com.tll.client.model.UnsetPropertyException;
import com.tll.client.ui.AbstractBoundWidget;
import com.tll.client.ui.IBoundWidget;
import com.tll.client.validate.ValidationFeedbackManager;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * IndexedFieldPanel - Caters to the display of field collections that are
 * indexable by adapting the {@link FieldPanel} with index related routines. All
 * indexing operations sync with the {@link FieldPanel}'s member ("underlying")
 * field group.
 * @param <I> the index field panel type
 * @param <M> the model type
 * @author jpk
 */
public abstract class IndexedFieldPanel<I extends FieldPanel<? extends Widget, M>, M extends IBindable> extends
		AbstractBoundWidget<Collection<M>, Collection<M>, M> implements IFieldGroupProvider {

	/**
	 * The group that serving as a common parent to the index field groups.
	 */
	private final FieldGroup topGroup;

	/**
	 * The list of indexed {@link FieldPanel}s.
	 */
	protected final List<I> indexPanels = new ArrayList<I>();

	/**
	 * The model data collection.
	 */
	private Collection<M> value;

	/**
	 * Holds bindings that bind {@link #value} to {@link #list}. Each sub-binding
	 * under this binding corresponds to a the field group of the same index in
	 * the {@link #list}.
	 */
	private final Binding binding = new Binding();

	private boolean drawn;

	/**
	 * Constructor
	 * @param name The name to ascribe to this field panel which serves the name
	 *        of the underlying top field group as well
	 */
	public IndexedFieldPanel(String name) {
		super();
		this.topGroup = new FieldGroup(name);
	}

	/**
	 * @return The number of indexed {@link FieldPanel}s.
	 */
	protected final int size() {
		return indexPanels.size();
	}

	/**
	 * Adds an indexed field group syncing with the underlying field group given a
	 * model instance which is first converted to a field group.
	 * @param model The model to be converted to a field group
	 * @throws IllegalArgumentException When the field to add already exists in
	 *         the underlying field group or has the same name as an existing
	 *         field in the underlying group.
	 */
	public final void add(M model) throws IllegalArgumentException {
		Log.debug("IndexedFieldPanel.add() - START");
		I ip = createIndexPanel(model);

		// apply property metadata
		if(model instanceof IPropertyMetadataProvider) {
			ip.getFieldGroup().applyPropertyMetadata((IPropertyMetadataProvider) model);
		}

		if(indexPanels.add(ip)) {
			ip.getFieldGroup().setName(topGroup.getName() + '[' + (size() - 1) + ']');
			topGroup.addField(ip.getFieldGroup());
			Binding indexBinding = new Binding();
			binding.getChildren().add(indexBinding);
			updateIndexedFieldGroup(ip.getFieldGroup(), model, indexBinding);
		}
	}

	/**
	 * Factory method to obtain a new index field panel instance.
	 * @param indexModel Provided to set field panel specific properties and,
	 *        consequently, may be ignored if there are no such properties.
	 * @return new field panel instance for employ at a particular index.
	 */
	protected abstract I createIndexPanel(M indexModel);

	/**
	 * Updates an <em>existing</em> indexed field group with data provided by the
	 * given model syncing with the underlying field group.
	 * @param index The index at which the field group is replaced
	 * @param model The model from which data is retrieved to update the field
	 *        group
	 * @throws IndexOutOfBoundsException When the given index is out of bounds
	 */
	protected final void update(int index, M model) throws IndexOutOfBoundsException {
		assert model != null;
		unbindAtIndex(index);
		FieldGroup fg = indexPanels.get(index).getFieldGroup();
		updateIndexedFieldGroup(fg, model, binding.getChildren().get(index));
	}

	/**
	 * Unbinds and removes all child bindings at the given index.
	 * @param index The index at which to unbind
	 */
	private void unbindAtIndex(int index) {
		Binding indexBinding = binding.getChildren().get(index);
		indexBinding.unbind();
		indexBinding.getChildren().clear();
	}

	/**
	 * Removes an indexed field group at the given index syncing with the
	 * underlying field group.
	 * @param index The index at which the field group is removed
	 * @return The removed field group
	 * @throws IndexOutOfBoundsException
	 */
	protected final FieldGroup remove(int index) throws IndexOutOfBoundsException {
		I removed = indexPanels.remove(index);
		topGroup.removeField(removed.getFieldGroup());
		unbindAtIndex(index);
		return removed.getFieldGroup();
	}

	/**
	 * @return The top-most aggregate {@link FieldGroup} containing the index
	 *         {@link FieldGroup}s.
	 */
	public final FieldGroup getFieldGroup() {
		return topGroup;
	}

	/**
	 * @return The indexed binding
	 */
	public final Binding getBinding() {
		return binding;
	}

	/**
	 * Draws the composite wrapped widget.
	 */
	protected abstract void draw();

	/**
	 * Regenerates the indexed field group list from the member {@link #value}
	 * collection.
	 */
	private void regenerate() {
		Log.debug("IndexedFieldPanel.regenerate() - START");
		clear();
		if(value != null) {
			for(M m : value) {
				add(m);
			}
		}
		Log.debug("IndexedFieldPanel.regenerate() - END");
	}

	/**
	 * Updates an indexed field group with the values held in the given model.
	 * @param fg The indexed field group to be updated
	 * @param model The model
	 * @param indexBinding If specified, bindings for each field in the given
	 *        field group whose value is successfully set will be added under this
	 *        binding.
	 */
	private void updateIndexedFieldGroup(FieldGroup fg, M model, Binding indexBinding) {
		for(IField<?, ?> f : fg) {
			if(f instanceof FieldGroup == false) {
				String propName = f.getPropertyName();
				try {
					f.setProperty(IBoundWidget.PROPERTY_VALUE, model.getProperty(propName));
					if(indexBinding != null) {
						binding.getChildren().add(
								new Binding(model, propName, null, null, f, IBoundWidget.PROPERTY_VALUE, f, ValidationFeedbackManager
										.instance()));
					}
				}
				catch(UnsetPropertyException e) {
					// ok
				}
				catch(NullNodeInPropPathException e) {
					// ok
				}
				catch(PropertyPathException e) {
					// bad proeperty path/name
					throw new IllegalStateException(e);
				}
				catch(Exception e) {
					// bad value
					throw new IllegalStateException(e);
				}
			}
			else {
				// drill down
				updateIndexedFieldGroup((FieldGroup) f, model, indexBinding);
			}
		}
	}

	/**
	 * Clears the field group list cleaning up bindings and listeners.
	 */
	public final void clear() {
		Log.debug("IndexedFieldPanel.clearing " + toString() + "..");
		binding.unbind();
		binding.getChildren().clear();
		indexPanels.clear();
	}

	public final Collection<M> getValue() {
		return value;
	}

	public final void setValue(Collection<M> value) {
		Collection<M> old = this.value;
		this.value = value;
		if(changeSupport != null) changeSupport.firePropertyChange("value", old, this.value);

		if(isAttached()) {
			regenerate();
		}
	}

	public final Object getProperty(String propPath) {
		return getValue();
	}

	/*
	 * NOTE: we ignore the given property path
	 */
	@SuppressWarnings("unchecked")
	public final void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		if(value instanceof Collection == false) {
			throw new Exception("The value must be a collection");
		}
		setValue((Collection<M>) value);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if(!drawn) {
			regenerate();
			Log.debug("IndexedFieldPanel.onLoad() drawing..");
			draw();
			drawn = true;
		}
	}

	@Override
	public String toString() {
		return "IndexedFieldPanel [ " + (topGroup.getName() == null ? "<noname>" : topGroup.getName()) + " ]";
	}
}
