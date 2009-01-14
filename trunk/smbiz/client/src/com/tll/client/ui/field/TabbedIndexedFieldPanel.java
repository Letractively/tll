/**
 * The Logic Lab
 * @author jpk
 * Jan 14, 2009
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.bind.IBindable;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.WidgetAndLabel;

/**
 * TabbedIndexedFieldPanel - {@link IndexedFieldPanel} implementation employing
 * a {@link TabPanel} to show the indexed field panels.
 * @author jpk
 * @param <I> the index field panel type
 * @param <M> the model type
 */
public abstract class TabbedIndexedFieldPanel<I extends FieldPanel<? extends Widget, M>, M extends IBindable> extends
		IndexedFieldPanel<I, M> implements TabListener {

	/**
	 * EmptyWidget - Displayed in place of the tab panel when no index field
	 * panels exist.
	 * @author jpk
	 */
	private final class EmptyWidget extends Composite {

		private final SimplePanel pnl = new SimplePanel();

		/**
		 * Constructor
		 */
		public EmptyWidget() {
			super();
			initWidget(pnl);
			if(enableAdd) {
				Button button = new Button("Click to add a new " + getIndexTypeName() + "..");
				button.addClickListener(new ClickListener() {

					public void onClick(Widget sender) {
						I newFp = add();
						tabPanel.add(newFp, getTabWidget(newFp, true));
						tabPanel.setVisible(true);
						emptyWidget.setVisible(false);
					}
				});
				pnl.add(button);
			}
			else {
				pnl.add(new Label("No " + getIndexTypeName() + "(s) currently exist."));
			}
		}

	} // EmptyWidget

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

	/**
	 * Constructor
	 * @param name The collective name
	 * @param enableAdd Allow tabs to be added?
	 * @param enableDelete Allow tabs to be marked as deleted?
	 */
	public TabbedIndexedFieldPanel(String name, boolean enableAdd, boolean enableDelete) {
		super(name);

		this.enableAdd = enableAdd;
		this.enableDelete = enableDelete;

		// listen to tab events
		tabPanel.addTabListener(this);

		pnl.add(tabPanel);

		emptyWidget = new EmptyWidget();
		emptyWidget.setVisible(false);
		pnl.add(emptyWidget);

		initWidget(pnl);
	}

	/**
	 * @return The generic name for what is being indexed.
	 */
	protected abstract String getIndexTypeName();

	/**
	 * Provides the tab label text for the given index field panel.
	 * @param indexFieldPanel
	 * @return label text
	 */
	protected abstract String getTabLabelText(I indexFieldPanel);

	/**
	 * Responsible for creating a single {@link Widget} that is places in the UI
	 * tab of the {@link TabPanel}.
	 * @param indexFieldPanel
	 * @param isNew Is this a newly added index field panel?
	 * @return The {@link Widget} to be used for the tab in the {@link TabPanel}
	 *         at the index assoc. with the given index field panel.
	 */
	private Widget getTabWidget(final I indexFieldPanel, final boolean isNew) {

		String labelText = isNew ? ("-New " + getIndexTypeName() + "-") : getTabLabelText(indexFieldPanel);

		if(enableDelete || isNew) {
			final ToggleButton btnDeleteTgl =
					new ToggleButton(App.imgs().delete().createImage(), App.imgs().undo().createImage());
			btnDeleteTgl.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					if(isNew) {
						// remove the tab
						tabPanel.remove(indexFieldPanel);
						if(tabPanel.getWidgetCount() == 0) {
							tabPanel.setVisible(false);
							emptyWidget.setVisible(true);
						}
					}
					else {
						markDeleted(tabPanel.getTabBar().getSelectedTab(), btnDeleteTgl.isDown());
					}
				}
			});

			return new WidgetAndLabel(btnDeleteTgl, labelText);
		}

		return new Label(labelText);
	}

	@Override
	protected void draw() {
		tabPanel.clear();

		if(size() == 0) {
			tabPanel.setVisible(false);
			emptyWidget.setVisible(true);
		}
		else {
			// add the *existing* index field panels to the tab panel
			for(I ap : this) {
				tabPanel.add(ap, getTabWidget(ap, false));
			}
			if(enableAdd) {
				// add trailing *add* tab
				PushButton pb = new PushButton(App.imgs().add().createImage());
				pb.setTitle("Add " + getIndexTypeName() + "..");
				tabPanel.add(new SimplePanel(), pb);
			}
		}
	}

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
		assert sender == tabPanel;

		// are we adding?
		if(tabPanel.getWidgetCount() > 0 && tabIndex == tabPanel.getWidgetCount() - 1) {
			I indexFieldPanel = add();
			tabPanel.remove(tabIndex);
			tabPanel.add(indexFieldPanel, getTabWidget(indexFieldPanel, true));
		}

		// need to hide any field messages bound to fields on the tab that is
		// going out of view
		else if(tabIndex != -1) {
			MsgManager.instance().show(tabPanel.getWidget(tabIndex), false, true);
		}

		return true;
	}

	public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
		// no-op
	}

	@Override
	protected I createIndexPanel(M indexModel) {
		return null;
	}

	@Override
	protected M createPrototypeModel() {
		return null;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if(tabPanel.getWidgetCount() > 0 && tabPanel.getTabBar().getSelectedTab() == -1) {
			tabPanel.selectTab(0);
		}
	}
}
