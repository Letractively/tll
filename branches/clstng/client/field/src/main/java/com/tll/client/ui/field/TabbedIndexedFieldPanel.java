/**
 * The Logic Lab
 * @author jpk
 * Jan 14, 2009
 */
package com.tll.client.ui.field;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.WidgetAndLabel;

/**
 * TabbedIndexedFieldPanel - {@link IndexedFieldPanel} implementation employing
 * a {@link TabPanel} to show the indexed field panels.
 * @param <I> the index field panel type
 * @author jpk
 */
public abstract class TabbedIndexedFieldPanel<I extends FieldPanel<?>> extends IndexedFieldPanel<FlowPanel, I>
		implements SelectionHandler<Integer>, BeforeSelectionHandler<Integer> {

	/**
	 * ImageBundle
	 * @author jpk
	 */
	public interface ImageBundle extends com.google.gwt.user.client.ui.ImageBundle {

		/**
		 * add (16x16)
		 * @return the image prototype
		 */
		@Resource(value = "com/tll/public/images/add.gif")
		AbstractImagePrototype add();

		/**
		 * undo (18x18)
		 * @return the image prototype
		 */
		@Resource(value = "com/tll/public/images/undo.gif")
		AbstractImagePrototype undo();

		/**
		 * delete (18x18)
		 * @return the image prototype
		 */
		@Resource(value = "com/tll/public/images/delete.gif")
		AbstractImagePrototype delete();
	}

	/**
	 * Styles - (widget-tll.css)
	 * <p>
	 * Styles used for a TabbedIndexedFieldPanel family of widgets.
	 * @author jpk
	 */
	protected static class Styles {

		/**
		 * The root style applied to the composite widget.
		 */
		public static final String ROOT = "tifp";

		/**
		 * The style applied to the empty widget.
		 */
		public static final String EMPTY = "empty";

		/**
		 * The style applied to the delete button in the tab widget.
		 */
		public static final String DELETE_BUTTON = "delbtn";
	}
	
	/**
	 * EmptyWidget - Displayed in place of the tab panel when no index field
	 * panels exist.
	 * @author jpk
	 */
	private final class EmptyWidget extends Composite {

		private final SimplePanel spnl = new SimplePanel();

		/**
		 * Constructor
		 */
		@SuppressWarnings("synthetic-access")
		public EmptyWidget() {
			super();
			spnl.setStylePrimaryName(Styles.EMPTY);
			initWidget(spnl);
			if(enableAdd) {
				final Button button = new Button("Click to add a new " + getIndexTypeName() + "..");
				button.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						add();
						tabPanel.setVisible(true);
						emptyWidget.setVisible(false);
					}
				});
				spnl.add(button);
			}
			else {
				spnl.add(new Label("No " + getIndexTypeName() + "(s) currently exist."));
			}
		}

	} // EmptyWidget

	/**
	 * The local image bundle for this widget.
	 */
	private static final ImageBundle imageBundle = (ImageBundle) GWT.create(ImageBundle.class);

	/**
	 * Token to indicate whether the index was added via the UI.
	 */
	private static final String UI_ADD = "uiadd";

	/**
	 * The composite wrapped panel holding the tab widget.
	 */
	private final FlowPanel pnl = new FlowPanel();

	/**
	 * The tab panel enumerating the indexed field panels.
	 */
	protected final TabPanel tabPanel = new TabPanel();

	/**
	 * The widget that is shown when there are no index field panels.
	 */
	private final EmptyWidget emptyWidget;

	/**
	 * Enable user to add and delete index field panels?
	 */
	private final boolean enableAdd, enableDelete;

	private final List<Widget> tabWidgets = new ArrayList<Widget>();
	
	//private int lastSelectedTabIndex = -1;
	
	/**
	 * Constructor
	 * @param name The collective name
	 * @param indexedPropertyName The name of the indexed property relative to a
	 *        parent root model.
	 * @param enableAdd Allow tabs to be added?
	 * @param enableDelete Allow tabs to be marked as deleted?
	 */
	public TabbedIndexedFieldPanel(String name, String indexedPropertyName, boolean enableAdd, boolean enableDelete) {
		super(name, indexedPropertyName);

		this.enableAdd = enableAdd;
		this.enableDelete = enableDelete;

		if(enableAdd) {
			// add trailing *add* tab
			final Image imgAdd = imageBundle.add().createImage();
			imgAdd.setTitle("Add " + getIndexTypeName());
			tabPanel.add(new SimplePanel(), imgAdd);
		}
		
		// listen to tab events
		tabPanel.addBeforeSelectionHandler(this);
		tabPanel.addSelectionHandler(this);

		pnl.add(tabPanel);

		emptyWidget = new EmptyWidget();
		emptyWidget.setVisible(false);
		pnl.add(emptyWidget);

		pnl.setStylePrimaryName(Styles.ROOT);
		
		initWidget(pnl);
	}

	/**
	 * @return The generic name for what is being indexed.
	 */
	protected abstract String getIndexTypeName();

	/**
	 * @param index
	 * @return A <em>unique</em> name for an index.
	 */
	protected abstract String getInstanceName(I index);

	/**
	 * Responsible for creating a single {@link Widget} that is placed in the UI
	 * tab of the {@link TabPanel}.
	 * @param index The index ref
	 * @param isUiAdd Are we adding a new index via the UI?
	 * @return The {@link Widget} to be used for the tab in the {@link TabPanel}
	 *         at the index assoc. with the given index field panel.
	 */
	private Widget getTabWidget(I index, boolean isUiAdd) {

		final String labelText = isUiAdd ? ("-New " + getIndexTypeName() + "-") : getInstanceName(index);

		if(enableDelete || isUiAdd) {
			final ToggleButton btnDeleteTgl =
					new ToggleButton(imageBundle.delete().createImage(), imageBundle.undo().createImage());
			btnDeleteTgl.addStyleName(Styles.DELETE_BUTTON);
			btnDeleteTgl.setTitle("Delete " + labelText);
			btnDeleteTgl.getElement().setPropertyBoolean(UI_ADD, isUiAdd);
			btnDeleteTgl.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					if(Element.as(event.getNativeEvent().getEventTarget()).getPropertyBoolean(UI_ADD)) {
						remove(tabPanel.getTabBar().getSelectedTab(), true);
					}
					else {
						markDeleted(tabPanel.getTabBar().getSelectedTab(), btnDeleteTgl.isDown());
						assert event.getSource() == btnDeleteTgl;
						btnDeleteTgl.setTitle(btnDeleteTgl.isDown() ? "Un-delete " + labelText : "Delete " + labelText);
					}
				}
			});

			return new WidgetAndLabel(btnDeleteTgl, labelText);
		}

		return new Label(labelText);
	}

	@Override
	protected void addUi(I index, boolean isUiAdd) {
		final int insertIndex = tabPanel.getWidgetCount() == 0 ? 0 : tabPanel.getWidgetCount() - 1;
		final Widget tw = getTabWidget(index, isUiAdd);
		tabPanel.insert(index, tw, insertIndex);
		tabWidgets.add(tw);
		tabPanel.setVisible(true);
		emptyWidget.setVisible(false);
		if(isUiAdd) {
			// auto-select the added tab
			DeferredCommand.addCommand(new Command() {

				public void execute() {
					tabPanel.selectTab(insertIndex);
				}
			});
		}
	}

	@Override
	protected void removeUi(int index, boolean isUiRemove) throws IndexOutOfBoundsException {
		// remove the tab
		if(!tabPanel.remove(index)) {
			// shouldn't happen
			throw new IllegalStateException("Unable to remove tab panel at index: " + index);
		}
		tabWidgets.remove(index);
		if(size() == 0) {
			tabPanel.setVisible(false);
			emptyWidget.setVisible(true);
		}
		else {
			if(isUiRemove) tabPanel.selectTab(index - 1 < 0 ? 0 : index - 1);
		}
	}

	@Override
	protected void markDeleted(int index, boolean deleted) throws IndexOutOfBoundsException {
		((WidgetAndLabel) tabWidgets.get(index)).getTheWidget().setTitle(
				(deleted ? "Un-delete " : "Delete ") + getIndexTypeName());
		super.markDeleted(index, deleted);
	}

	@Override
	public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
		assert event.getSource() == tabPanel;
		if(enableAdd && (event.getItem().intValue() == tabPanel.getTabBar().getTabCount() - 1)) {
			add();
		}
	}

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		//lastSelectedTabIndex = event.getSelectedItem();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if(tabPanel.getWidgetCount() > 0 && tabPanel.getTabBar().getSelectedTab() == -1) {
			tabPanel.selectTab(0);
		}
	}
}
