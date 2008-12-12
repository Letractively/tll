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
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.IData;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.ui.Toolbar;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.util.StringUtil;

/**
 * ListingNavBar - Toolbar impl for listing navigation.
 * @param <R> The row data type.
 * @author jpk
 */
// TODO make non-static inner class of ListingWidget
public class ListingNavBar<R extends IData> extends Toolbar implements ClickListener, KeyboardListener, ChangeListener, IListingListener<R> {

	private static final String STYLE_TABLE_VIEW_NAVBAR = "tvnav";
	private static final String CSS_PAGE_CONTAINER = "page";
	private static final String CSS_SUMMARY = "smry";
	private static final String CSS_PAGE = "tbPage";

	private String listingElementName;

	private IListingOperator<R> listingOperator;

	private IAddRowDelegate addRowDelegate;

	private int pageSize;

	// page nav related
	private Image imgPageFirst;
	private Image imgPagePrev;
	private Image imgPageNext;
	private Image imgPageLast;
	private PushButton btnPageFirst;
	private PushButton btnPagePrev;
	private PushButton btnPageNext;
	private PushButton btnPageLast;
	private final TextBox tbPage = new TextBox();
	private final Label lblPagePre = new Label("Page");
	private final Label lblPagePost = new Label();

	// refresh related
	private Image imgRefresh;
	private PushButton btnRefresh;

	// add related
	private PushButton btnAdd;

	// summary text ("Displaying elements x of y")
	private Label lblSmry;

	private int firstIndex = -1;
	private int lastIndex = -1;
	private int totalSize = -1;
	private int numPages = -1;
	/**
	 * Current 1-based page number.
	 */
	private int crntPage = -1;

	private boolean isFirstPage, isLastPage;

	/**
	 * Constructor
	 * @param config Must be specified.
	 */
	public ListingNavBar(IListingConfig<? extends IData> config) {
		super();
		initialize(config);
	}

	/**
	 * Initializes the nav bar.
	 * @param config
	 */
	protected void initialize(IListingConfig<? extends IData> config) {
		assert config != null;

		this.listingElementName = config.getListingElementName();
		assert listingElementName != null;

		addStyleName(STYLE_TABLE_VIEW_NAVBAR);

		Image split;

		pageSize = config.getPageSize();

		if(pageSize > 0) {
			imgPageFirst = App.imgs().page_first().createImage();
			imgPagePrev = App.imgs().page_prev().createImage();
			imgPageNext = App.imgs().page_next().createImage();
			imgPageLast = App.imgs().page_last().createImage();

			btnPageFirst = new PushButton(imgPageFirst, this);
			btnPagePrev = new PushButton(imgPagePrev, this);
			btnPageNext = new PushButton(imgPageNext, this);
			btnPageLast = new PushButton(imgPageLast, this);

			btnPageFirst.setTitle("Fist Page");
			btnPagePrev.setTitle("Previous Page");
			btnPageNext.setTitle("Next Page");
			btnPageLast.setTitle("Last Page");
			tbPage.setTitle("Current Page");

			tbPage.addKeyboardListener(this);
			tbPage.addChangeListener(this);
			tbPage.setMaxLength(4);
			tbPage.setStyleName(CSS_PAGE);

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
		}

		// show refresh button?
		if(config.isShowRefreshBtn()) {
			imgRefresh = App.imgs().refresh().createImage();
			btnRefresh = new PushButton(imgRefresh, this);
			btnRefresh.setTitle("Refresh");

			if(pageSize > 0) {
				// separator
				split = App.imgs().split().createImage();
				split.setStylePrimaryName(CSS_SEPARATOR);
				add(split);
			}
			add(btnRefresh);
		}

		// show add button?
		if(config.getAddRowHandler() != null) {
			// imgAdd = App.imgs().add().createImage();
			String title = "Add " + config.getListingElementName();
			btnAdd = new PushButton(title, this);
			btnAdd.setTitle(title);
			if(pageSize > 0 || config.isShowRefreshBtn()) {
				// separator
				split = App.imgs().split().createImage();
				split.setStylePrimaryName(CSS_SEPARATOR);
				add(split);
			}
			add(btnAdd);
		}

		// Displaying {listing element name} x - y of TOTAL
		lblSmry = new Label();
		lblSmry.setStyleName(CSS_SUMMARY);
		add(lblSmry);

		// NOTE: we do this to squish the other table cells to their smallest
		// possible width
		setWidgetContainerWidth(lblSmry, "100%");
	}

	/**
	 * Sets the listing operator on behalf of the containing listing Widget.
	 * @param listingOperator
	 */
	void setListingOperator(IListingOperator<R> listingOperator) {
		this.listingOperator = listingOperator;
	}

	/**
	 * Sets the add row delegate
	 * @param addRowDelegate
	 */
	public void setAddRowDelegate(IAddRowDelegate addRowDelegate) {
		this.addRowDelegate = addRowDelegate;
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	public void onClick(Widget sender) {
		if(pageSize > 0) {
			if(sender == btnPageFirst) {
				listingOperator.firstPage();
			}
			else if(sender == btnPagePrev) {
				listingOperator.previousPage();
			}
			else if(sender == btnPageNext) {
				listingOperator.nextPage();
			}
			else if(sender == btnPageLast) {
				listingOperator.lastPage();
			}
		}
		else if(sender == btnRefresh) {
			listingOperator.refresh();
		}
		else if(sender == btnAdd) {
			assert addRowDelegate != null;
			addRowDelegate.handleAddRow();
		}
		((HasFocus) sender).setFocus(false);
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
			listingOperator.gotoPage(page - 1);
		}
		else {
			throw new IllegalArgumentException("Unhandled listing nav change action");
		}
	}

	void increment() {
		lastIndex++;
		totalSize++;
		draw();
	}

	void decrement() {
		lastIndex--;
		totalSize--;
		draw();
	}

	/**
	 * Re-draws the contents of the nav bar.
	 */
	private void draw() {
		if(pageSize > 0) {
			// first page btn
			btnPageFirst.setEnabled(!isFirstPage);
			if(isFirstPage) {
				App.imgs().page_first_disabled().applyTo(imgPageFirst);
			}
			else {
				App.imgs().page_first().applyTo(imgPageFirst);
			}

			// last page btn
			btnPageLast.setEnabled(!isLastPage);
			if(isLastPage) {
				App.imgs().page_last_disabled().applyTo(imgPageLast);
			}
			else {
				App.imgs().page_last().applyTo(imgPageLast);
			}

			// prev page btn
			btnPagePrev.setEnabled(!isFirstPage);
			if(isFirstPage) {
				App.imgs().page_prev_disabled().applyTo(imgPagePrev);
			}
			else {
				App.imgs().page_prev().applyTo(imgPagePrev);
			}

			// next page btn
			btnPageNext.setEnabled(!isLastPage);
			if(isLastPage) {
				App.imgs().page_next_disabled().applyTo(imgPageNext);
			}
			else {
				App.imgs().page_next().applyTo(imgPageNext);
			}

			tbPage.setText(Integer.toString(crntPage));
			tbPage.setEnabled(numPages > 1);
			lblPagePost.setText("of " + numPages);
		}

		// summary caption
		assert listingElementName != null;
		if(totalSize == 0) {
			lblSmry.setText("No " + listingElementName + "s exist");
		}
		else {
			lblSmry.setText("Displaying " + listingElementName + "s " + (firstIndex + 1) + " - " + (lastIndex + 1) + " of "
					+ totalSize);
		}
	}

	public void onListingEvent(ListingEvent<R> event) {
		if(event.getListingOp().isQuery() && event.isSuccess()) {
			this.firstIndex = event.getOffset();
			this.lastIndex = firstIndex + event.getPageElements().length - 1;
			this.totalSize = event.getListSize();
			this.numPages = event.getNumPages();
			this.crntPage = event.getPageNum() + 1;
			this.isFirstPage = (crntPage == 1);
			this.isLastPage = (crntPage == numPages);
			draw();
		}
	}
}
