/**
 * The Logic Lab
 * @author jpk
 * Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.Binding;
import com.tll.client.bind.IBindable;
import com.tll.client.model.NullNodeInPropPathException;
import com.tll.client.model.PropertyPathException;
import com.tll.client.model.UnsetPropertyException;
import com.tll.client.ui.AbstractBoundWidget;
import com.tll.client.validate.ValidationFeedbackManager;

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
	private final FieldGroup topGroup = new FieldGroup();

	/**
	 * The list of index {@link Widget}s where each elements index corresponds to
	 * the {@link FieldGroup} at the same index in the field group list.
	 */
	protected final List<I> indexPanels = new ArrayList<I>();

	/**
	 * Holds bindings that bind {@link #value} to {@link #list}. Each sub-binding
	 * under this binding corresponds to a the field group of the same index in
	 * the {@link #list}.
	 */
	private final Binding indexedBinding = new Binding();

	/**
	 * The model data collection.
	 */
	private Collection<M> value;

	private boolean drawn;

	/**
	 * Constructor
	 */
	public IndexedFieldPanel() {
		super();
	}

	/**
	 * @return The number of indexed field groups.
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
		I ip = generateIndexPanel();
		if(indexPanels.add(ip)) {
			topGroup.addField(ip.getFieldGroup());
			Binding indexBinding = new Binding();
			indexedBinding.getChildren().add(indexBinding);
			updateIndexedFieldGroup(ip.getFieldGroup(), model, indexBinding);
		}
	}

	/**
	 * Generates a new index field panel instance with the field group set.
	 * @return new field panel for employ at a particular index.
	 */
	protected abstract I generateIndexPanel();

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
		updateIndexedFieldGroup(fg, model, indexedBinding.getChildren().get(index));
	}

	/**
	 * Unbinds and removes all child bindings at the given index.
	 * @param index The index at which to unbind
	 */
	private void unbindAtIndex(int index) {
		Binding indexBinding = indexedBinding.getChildren().get(index);
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

	public FieldGroup getFieldGroup() {
		return topGroup;
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
		clear();
		if(value != null) {
			for(M m : value) {
				add(m);
			}
		}
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
			String propName = f.getPropertyName();
			try {
				f.setProperty(propName, model.getProperty(propName));
				if(indexBinding != null) {
					// TODO specify field validator
					indexedBinding.getChildren().add(
							new Binding(model, null, null, f, /*field validator*/null, ValidationFeedbackManager.instance(),
									propName));
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
	}

	/**
	 * Clears the field group list cleaning up listeners.
	 */
	public void clear() {
		indexedBinding.unbind();
		indexedBinding.getChildren().clear();
		indexPanels.clear();
	}

	public Collection<M> getValue() {
		return value;
	}

	public void setValue(Collection<M> value) {
		Collection<M> old = this.value;
		this.value = value;
		if(changeSupport != null) changeSupport.firePropertyChange("value", old, this.value);

		if(isAttached()) {
			regenerate();
		}
	}

	public Object getProperty(String propPath) {
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
	protected void onAttach() {
		super.onAttach();
		regenerate();
		if(!drawn) {
			draw();
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		clear();
	}

}
