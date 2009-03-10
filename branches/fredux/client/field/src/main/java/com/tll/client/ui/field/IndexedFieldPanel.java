/**
 * The Logic Lab
 * @author jpk Jan 3, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Widget;
import com.tll.IProvider;
import com.tll.client.bind.FieldBindingAction;
import com.tll.client.bind.IBindingAction;
import com.tll.client.ui.AbstractBindableWidget;
import com.tll.client.validate.ValidationException;
import com.tll.common.bind.IModel;

/**
 * IndexedFieldPanel - Caters to the display of indexed field collections.
 * <p>
 * The {@link IndexedFieldPanel} is savagely different from its brethren
 * {@link FieldPanel} in that the value type is a raw reference to a model
 * collection rather than a {@link FieldGroup}. Because of this, this field
 * panel type manages its own binding life-cycle which is triggered by the
 * {@link #setValue(Collection)} and {@link #getValue()} methods.
 * <p>
 * {@link IndexedFieldPanel}s <em>must</em> "subscribe" to its parent
 * {@link FieldPanel}'s binding action and this panel does so upon dom
 * attachment.
 * <p>
 * <b>IMPT:</b> This panel's field group is expected to be a child of the parent
 * field panel's field group.
 * @param <W> the indexed field panel widget render type
 * @param <I> the index field panel type
 * @author jpk
 */
public abstract class IndexedFieldPanel<W extends Widget, I extends FieldPanel<?>> extends
		AbstractBindableWidget<Collection<IModel>> implements IFieldGroupProvider {

	/**
	 * Index - Wrapper class for each field panel at an index encapsulating the
	 * field panel and its own field binding action.
	 * @author jpk
	 * @param <I> the index field panel type
	 */
	static final class Index<I extends FieldPanel<?>> implements IProvider<I> {

		final I fieldPanel;
		final FieldBindingAction binding;

		/**
		 * Constructor
		 * @param fieldPanel
		 * @param binding
		 */
		public Index(I fieldPanel, FieldBindingAction binding) {
			super();
			this.fieldPanel = fieldPanel;
			this.binding = binding;
		}

		@Override
		public I get() {
			return fieldPanel;
		}

		private String getIndexTypeName() {
			return fieldPanel.getModel().getEntityType().getPresentationName();
		}
		
		public void setFieldGroupName(int index) {
			fieldPanel.getValue().setName(getIndexTypeName() + " - " + index);
		}
	}

	/**
	 * The indexed property name identifying the related many model collection in
	 * the root model.
	 */
	private final String indexedPropertyName;

	/**
	 * The list of indexed {@link FieldPanel}s.
	 */
	private final List<Index<I>> indexPanels = new ArrayList<Index<I>>();

	/**
	 * The root field group containing only child {@link FieldGroup}s associated
	 * with each indexed {@link FieldPanel}. This group corresponds property path
	 * wise to the related many model collection in the root model specified by
	 * {@link #indexedPropertyName}.
	 */
	private final FieldGroup group;

	/**
	 * Internally held ref to the related many model collection to know whether to
	 * re-bind or not. This avoids spurious re-binds when property change events
	 * fire!
	 */
	private transient Collection<IModel> value;

	/**
	 * Constructor
	 * @param fieldGroupName The presentation worthy field group for the managed
	 *        field group containing the indexable sub-field groups
	 * @param indexedPropertyName The name of the indexed property identifying the
	 *        related many model collection in the root model (the model held in
	 *        the parent field panel). This is needed to "subscribe" to the
	 *        parent's binding life-cycle so that this panel receives the raw
	 *        model collection during binding
	 */
	public IndexedFieldPanel(String fieldGroupName, String indexedPropertyName) {
		super();
		group = new FieldGroup(fieldGroupName);
		this.indexedPropertyName = indexedPropertyName;
	}

	/**
	 * @return A newly created iterator over the indexed field panels.
	 */
	protected final Iterator<? extends IProvider<I>> getIndexIterator() {
		return indexPanels.iterator();
	}

	/**
	 * Required ref to the parent field panel to be able to hook into the paren't
	 * binding action.
	 * @return Ref to the parent field panel.
	 */
	public abstract FieldPanel<?> getParentFieldPanel();

	@Override
	public final Collection<IModel> getValue() {
		if(value == null) {
			value = new ArrayList<IModel>(indexPanels.size());
			// update the model collection!
			for(final Index<I> index : indexPanels) {
				try {
					index.binding.execute();
				}
				catch(final ValidationException e) {
					throw new RuntimeException(e);
				}
				value.add(index.fieldPanel.getModel());
			}
		}
		return value;
	}

	@Override
	public final void setValue(Collection<IModel> value) {
		if(this.value != value) {
			this.value = null;
			clear();
			// we don't want auto-transfer of data!!
			//changeSupport.firePropertyChange(IBindableWidget.PROPERTY_VALUE, old, this.value);
			if(value != null) {
				for(final IModel m : value) {
					add(m, false);
				}
			}
		}
	}

	@Override
	public final FieldGroup getFieldGroup() {
		return group;
	}

	/**
	 * @return The number of {@link IndexFieldPanel}s.
	 */
	protected final int size() {
		return indexPanels.size();
	}

	/**
	 * @return A prototype model used to bind to a newly added indexed field
	 *         panel.
	 */
	protected abstract IModel createPrototypeModel();

	/**
	 * Factory method to obtain a new index field panel instance.
	 * @return new field panel instance for employ at a particular index.
	 */
	protected abstract I createIndexPanel();

	/**
	 * Adds a new indexed field group from the UI.
	 */
	protected final void add() {
		add(createPrototypeModel(), true);
	}

	/**
	 * Adds the given index in the ui.
	 * @param index the index to add to the ui
	 * @param isUiAdd Are we adding via the UI or adding an existing index?
	 */
	protected abstract void addUi(I index, boolean isUiAdd);

	/**
	 * Removes the index panel from the ui at the given numeric index.
	 * @param index the numeric index at which to remove the index panel in the ui
	 */
	protected abstract void removeUi(int index) throws IndexOutOfBoundsException;

	/**
	 * Removes an indexed field group at the given index syncing with the
	 * underlying field group.
	 * @param index The index at which the field group is removed
	 * @throws IndexOutOfBoundsException
	 */
	protected final void remove(int index) throws IndexOutOfBoundsException {
		// remove from the ui first
		removeUi(index);

		final boolean rebuildNames = index < indexPanels.size() - 1;
		Index<I> remove = indexPanels.remove(index);
		assert remove != null && remove.fieldPanel.getModel().isNew() == false;

		remove.binding.unbind(remove.fieldPanel);
		group.removeField(remove.fieldPanel.getValue());
		remove = null;

		// re-set remaining field group names if necessary
		if(rebuildNames) {
			for(int i = 0; i < indexPanels.size(); i++) {
				indexPanels.get(i).setFieldGroupName(i);
			}
		}
	}

	/**
	 * Marks or un-marks a field panel at the given index as deleted.
	 * @param index
	 * @param deleted mark as deleted or un-deleted?
	 * @throws IndexOutOfBoundsException
	 */
	protected void markDeleted(int index, boolean deleted) throws IndexOutOfBoundsException {
		final Index<I> ip = indexPanels.get(index);
		ip.fieldPanel.getModel().setMarkedDeleted(deleted);
		ip.fieldPanel.getValue().setEnabled(!deleted);
	}

	/**
	 * Clears the field group list cleaning up bindings and listeners.
	 */
	protected void clear() {
		// remove in reverse to avoid spurious index group re-names
		for(int i = size() - 1; i >= 0; i--) {
			remove(i);
		}
		assert group.size() == 0;
		assert indexPanels.size() == 0;
	}

	/**
	 * Adds an indexed field group populating the fields with data held in the
	 * given model.
	 * @param model The model to be converted to a field group
	 * @throws IllegalArgumentException When the field to add already exists in
	 *         the underlying field group or has the same name as an existing
	 *         field in the underlying group.
	 */
	private void add(IModel model, boolean isAddUi) throws IllegalArgumentException {
		Log.debug("IndexedFieldPanel.add() - START");

		final IBindingAction<FieldGroup> parentBindingAction = getParentFieldPanel().getAction();
		assert parentBindingAction != null;
		
		final I ip = createIndexPanel();
		ip.setModel(model);
		
		// NOTE: we *don't* specify and error handler for this index field panel's binding actions
		// as this is handled by the parent binding action since 
		// its root field group is expected to contain this panel's field group as a child
		final Index<I> index = new Index<I>(ip, new FieldBindingAction());
		indexPanels.add(index);

		index.setFieldGroupName(size());
		group.addField(ip.getValue());

		index.binding.set(index.fieldPanel);
		index.binding.bind(index.fieldPanel);

		// propagate the error handler from parent field group to this index field group
		ip.getValue().setErrorHandler(getParentFieldPanel().getValue().getErrorHandler());

		// add in the ui
		addUi(ip, isAddUi);
	}

	@Override
	protected void onAttach() {
		final FieldPanel<?> parent = getParentFieldPanel();
		if(parent == null || parent.getAction() == null) throw new IllegalStateException();
		parent.getAction().bindIndexed(this, indexedPropertyName);
		super.onAttach();
	}

	@Override
	public String toString() {
		return "IndexedFieldPanel [ " + indexedPropertyName + " ]";
	}
}
