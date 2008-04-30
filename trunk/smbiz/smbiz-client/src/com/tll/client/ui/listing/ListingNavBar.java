/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.App;
import com.tll.client.event.IListingListener;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.listing.ClientListingOp;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.Model;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.ui.Toolbar;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.util.StringUtil;
import com.tll.listhandler.IPage;

/**
 * ListingNavBar - Toolbar impl for listing navigation.
 * @author jpk
 */
public class ListingNavBar extends Toolbar implements IListingListener, ClickListener, KeyboardListener, ChangeListener {

	private static final String STYLE_TABLE_VIEW_NAVBAR = "tvnav";
	private static final String CSS_PAGE_CONTAINER = "page";
	private static final String CSS_SUMMARY = "smry";
	private static final String CSS_PAGE = "tbPage";

	private String listingElementName;
	private IListingOperator listingOperator;

	private final Image imgPageFirst = App.imgs().page_first().createImage();
	private final Image imgPagePrev = App.imgs().page_prev().createImage();
	private final Image imgPageNext = App.imgs().page_next().createImage();
	private final Image imgPageLast = App.imgs().page_last().createImage();
	private Image imgRefresh;
	// private Image imgAdd;
	private final PushButton btnPageFirst = new PushButton(imgPageFirst, this);
	private final PushButton btnPagePrev = new PushButton(imgPagePrev, this);
	private final PushButton btnPageNext = new PushButton(imgPageNext, this);
	private final PushButton btnPageLast = new PushButton(imgPageLast, this);
	private PushButton btnRefresh;
	private PushButton btnAdd;
	private final TextBox tbPage = new TextBox();
	private final Label lblPagePre = new Label("Page");
	private final Label lblPagePost = new Label();
	private final Label lblSmry = new Label();

	private int firstIndex = -1;
	private int lastIndex = -1;
	private int totalSize = -1;
	private int numPages = -1;
	private int crntPage = -1;

	/**
	 * Constructor
	 * @param config Must be specified.
	 */
	public ListingNavBar(IListingConfig config) {
		super();
		initialize(config);
	}

	/**
	 * Initializes the nav bar.
	 * @param config
	 */
	protected void initialize(IListingConfig config) {
		assert config != null;

		this.listingElementName = config.getListingElementName();
		assert listingElementName != null;
		// setTitle(config.descriptor() + " Navigation Panel");

		addStyleName(STYLE_TABLE_VIEW_NAVBAR);

		btnPageFirst.setTitle("Fist Page");
		btnPagePrev.setTitle("Previous Page");
		btnPageNext.setTitle("Next Page");
		btnPageLast.setTitle("Last Page");
		tbPage.setTitle("Current Page");

		tbPage.addKeyboardListener(this);
		tbPage.addChangeListener(this);
		tbPage.setMaxLength(4);
		tbPage.setStyleName(CSS_PAGE);

		// ImageContainer ic;
		Image split;

		// prev buttons (divs)
		add(btnPageFirst);
		add(btnPagePrev);

		// separator
		split = App.imgs().split().createImage();
		split.setStylePrimaryName(CSS_SEPARATOR);
		add(split);

		// Page x of y
		FlowPanel pageXofY = new FlowPanel();
		pageXofY.addStyleName(CSS_PAGE_CONTAINER);
		pageXofY.add(lblPagePre);
		pageXofY.add(tbPage);
		pageXofY.add(lblPagePost);
		add(pageXofY);

		// separator
		split = App.imgs().split().createImage();
		split.setStylePrimaryName(CSS_SEPARATOR);
		add(split);

		// next buttons (divs)
		add(btnPageNext);
		add(btnPageLast);

		// show refresh button?
		if(config.isShowRefreshBtn()) {
			imgRefresh = App.imgs().refresh().createImage();
			btnRefresh = new PushButton(imgRefresh, this);
			btnRefresh.setTitle("Refresh");
			// separator
			split = App.imgs().split().createImage();
			split.setStylePrimaryName(CSS_SEPARATOR);
			add(split);
			add(btnRefresh);
		}

		// show add button?
		if(config.isShowAddBtn()) {
			// imgAdd = App.imgs().add().createImage();
			String title = "Add " + config.getListingElementName();
			btnAdd = new PushButton(title);
			btnAdd.setTitle(title);
			// separator
			split = App.imgs().split().createImage();
			split.setStylePrimaryName(CSS_SEPARATOR);
			add(split);
			add(btnAdd);
		}

		// Displaying {listing element name} x - y of TOTAL
		lblSmry.setStyleName(CSS_SUMMARY);
		add(lblSmry);

		// NOTE: we do this to squish the other table cells to their smallest
		// possible width
		setWidgetContainerWidth(lblSmry, "100%");
	}

	/**
	 * @param listingOperator the listingOperator to set
	 */
	public void setListingOperator(IListingOperator listingOperator) {
		this.listingOperator = listingOperator;
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	public void onClick(Widget sender) {
		ClientListingOp action = null;
		Integer page = null;
		if(sender == btnPageFirst) {
			action = ClientListingOp.FIRST_PAGE;
		}
		else if(sender == btnPagePrev) {
			action = ClientListingOp.PREVIOUS_PAGE;
		}
		else if(sender == btnPageNext) {
			action = ClientListingOp.NEXT_PAGE;
		}
		else if(sender == btnPageLast) {
			action = ClientListingOp.LAST_PAGE;
		}
		else if(sender == btnRefresh) {
			listingOperator.refresh();
		}
		else if(sender == btnAdd) {
			// TODO: handle
		}
		else {
			throw new IllegalArgumentException("Unhandled listing nav bar action");
		}
		((HasFocus) sender).setFocus(false);
		assert listingOperator != null : "No listing operator set";
		if(action != null) {
			listingOperator.navigate(action, page);
		}
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
		if(sender == tbPage) {
			if(keyCode == KEY_ENTER) {
				tbPage.setFocus(false); // force invocation of onChange() event.
			}
		}
	}

	public void onChange(Widget sender) {
		if(sender == tbPage) {
			String s = tbPage.getText();
			int page = 0;
			boolean valid = true;
			try {
				page = Integer.parseInt(s);
				if(page < 1 || page > numPages) {
					valid = false;
				}
			}
			catch(NumberFormatException e) {
				valid = false;
			}
			if(!valid) {
				String smsg = StringUtil.replaceVariables("Please enter a number between %1 and %2.", new Object[] {
					new Integer(1),
					new Integer(numPages) });
				MsgManager.instance.post(true, new Msg(smsg, MsgLevel.ERROR), Position.BOTTOM, tbPage, 3000, true).show();
				tbPage.setText(Integer.toString(crntPage));
				return;
			}
			assert listingOperator != null : "No listing operator set";
			listingOperator.navigate(ClientListingOp.GOTO_PAGE, new Integer(page - 1));
		}
		else {
			throw new IllegalArgumentException("Unhandled listing nav change action");
		}
	}

	private void setSummaryCaption() {
		assert listingElementName != null;
		lblSmry.setText("Displaying " + listingElementName + "s " + (firstIndex + 1) + " - " + (lastIndex + 1) + " of "
				+ totalSize);
	}

	public void onListingEvent(ListingEvent event) {
		if(event.isSuccess()) {
			IPage<Model> page = event.getPage();
			if(page == null) {
				return;
			}
			this.firstIndex = page.getFirstIndex();
			this.lastIndex = page.getLastIndex();
			this.totalSize = page.getTotalSize();
			this.numPages = page.getNumPages();
			this.crntPage = page.getPageNumber() + 1;

			// first page btn
			btnPageFirst.setEnabled(!page.isFirstPage());
			if(page.isFirstPage()) {
				App.imgs().page_first_disabled().applyTo(imgPageFirst);
			}
			else {
				App.imgs().page_first().applyTo(imgPageFirst);
			}

			// last page btn
			btnPageLast.setEnabled(!page.isLastPage());
			if(page.isLastPage()) {
				App.imgs().page_last_disabled().applyTo(imgPageLast);
			}
			else {
				App.imgs().page_last().applyTo(imgPageLast);
			}

			// prev page btn
			btnPagePrev.setEnabled(!page.isFirstPage());
			if(page.isFirstPage()) {
				App.imgs().page_prev_disabled().applyTo(imgPagePrev);
			}
			else {
				App.imgs().page_prev().applyTo(imgPagePrev);
			}

			// next page btn
			btnPageNext.setEnabled(!page.isLastPage());
			if(page.isLastPage()) {
				App.imgs().page_next_disabled().applyTo(imgPageNext);
			}
			else {
				App.imgs().page_next().applyTo(imgPageNext);
			}

			tbPage.setText(Integer.toString(crntPage));
			tbPage.setEnabled(numPages > 1);
			lblPagePost.setText("of " + numPages);
			setSummaryCaption();
		}
	}
}
