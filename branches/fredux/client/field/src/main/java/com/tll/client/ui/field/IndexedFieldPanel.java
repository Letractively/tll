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
import com.tll.client.bind.FieldBindingAction;
import com.tll.client.ui.AbstractBindableWidget;
import com.tll.common.bind.IModel;
import com.tll.common.model.Model;

/**
 * IndexedFieldPanel - Caters to the display of indexed field collections.
 * <p>
 * The {@link IndexedFieldPanel} is savagely different from its brethren
 * {@link FieldPanel} in that the value type is a raw reference to a model
 * collection rather than a {@link FieldGroup}. Because of this, this field
 * panel type manages its own binding life-cycle which is soley dicated by the
 * {@link #setValue(Collection)} and {@link #getValue()} methods.
 * <p>
 * {@link IndexedFieldPanel}s <em>must</em> "subscribe" to its parent
 * {@link FieldPanel}'s binding action and this panel does so upon dom
 * attachment.
 * @param <W> the indexed field panel widget render type
 * @param <I> the index field panel type
 * @author jpk
 */
public abstract class IndexedFieldPanel<W extends Widget, I extends FieldPanel<?>> extends
		AbstractBindableWidget<Collection<IModel>> implements IFieldGroupProvider {

	/**
	 * The indexed property name needed to bind to the indexed model collection
	 * that is provided by the parent field binding action.
	 */
	private final String indexedPropertyName;

	/**
	 * The list of indexed {@link FieldPanel}s.
	 */
	private final List<I> indexPanels = new ArrayList<I>();

	/**
	 * The root field group containing only child {@link FieldGroup}s associated
	 * with each indexed {@link FieldPanel}.
	 */
	private final FieldGroup group;

	/**
	 * Constructor
	 * @param name The name to ascribe to this field panel serving as this field
	 *        panel's field group name
	 * @param indexedPropertyName The required name of the indexed property
	 *        relative to the parent field panel. This is needed to "subscribe" to
	 *        the parent's binding life-cycle so that this panel receives the raw
	 *        model collection!
	 */
	public IndexedFieldPanel(String name, String indexedPropertyName) {
		super();
		group = new FieldGroup(name);
		this.indexedPropertyName = indexedPropertyName;
	}
	
	/**
	 * @return A newly created iterator over the index panels.
	 */
	protected final Iterator<I> getIndexIterator() {
		return indexPanels.iterator();
	}

	/**
	 * Provides the ref to the parent field panel which is necessary for proper
	 * model/field binding.
	 * @return Ref to the parent field panel.
	 */
	public abstract FieldPanel<?> getParentFieldPanel();

	@Override
	public final Collection<IModel> getValue() {
		final ArrayList<IModel> list = new ArrayList<IModel>(indexPanels.size());
		// update the model collection!
		for(final I index : indexPanels) {
			list.add(index.getModel());
		}
		return list;
	}

	@Override
	public final void setValue(Collection<IModel> value) {
		clear();
		// we don't want auto-transfer of data!!
		//changeSupport.firePropertyChange(IBindableWidget.PROPERTY_VALUE, old, this.value);
		if(value != null) {
			for(final IModel m : value) {
				add(m, false);
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
		
		final I remove = indexPanels.get(index);
		assert ((Model) remove.getModel()).isNew() == false;
		indexPanels.remove(index);
		group.removeField(remove.getValue());
		getParentFieldPanel().getAction().unbind(remove);
	}

	/**
	 * Marks or un-marks a field panel at the given index as deleted.
	 * @param index
	 * @param deleted mark as deleted or un-deleted?
	 * @throws IndexOutOfBoundsException
	 */
	protected void markDeleted(int index, boolean deleted) throws IndexOutOfBoundsException {
		final I ip = indexPanels.get(index);
		ip.getModel().setMarkedDeleted(deleted);
		ip.getValue().setEnabled(!deleted);
	}

	/**
	 * Clears the field group list cleaning up bindings and listeners.
	 */
	protected void clear() {
		group.clear();
		indexPanels.clear();
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
		final I ip = createIndexPanel();
		indexPanels.add(ip);
		ip.setModel(model);
		final FieldGroup ipg = ip.getValue();
		ipg.setName(ipg.getName() + " - " + size());
		group.addField(ipg);
		ip.getValue().setErrorHandler(getParentFieldPanel().getValue().getErrorHandler());
		//getParentFieldPanel().getAction().bind(ip);
		// TODO: obtain ref to global msg panel somehow
		ip.setAction(new FieldBindingAction(null));
		// add in the ui
		addUi(ip, isAddUi);
	}

	@Override
	protected void onAttach() {
		final FieldPanel<?> parent = getParentFieldPanel();
		if(parent == null) throw new IllegalStateException();
		parent.getAction().bindIndexed(this, indexedPropertyName);
		super.onAttach();
	}
	
	@Override
	public String toString() {
		return "IndexedFieldPanel [ " + indexedPropertyName + " ]";
	}
}
