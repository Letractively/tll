/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingListener;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.ListingEvent;
import com.tll.client.ui.Position;
import com.tll.client.ui.Toolbar;
import com.tll.client.ui.msg.Msgs;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;
import com.tll.common.util.StringUtil;

/**
 * ListingNavBar - Toolbar impl for listing navigation.
 * @param <R> The row data type.
 * @author jpk
 */
public class ListingNavBar<R> extends Toolbar implements ClickHandler, KeyUpHandler, ChangeHandler,
		IListingListener<R> {

	/**
	 * The listing nav bar specific image bundle.
	 */
	private static final ListingNavBarImageBundle imageBundle =
			(ListingNavBarImageBundle) GWT.create(ListingNavBarImageBundle.class);

	/**
	 * Styles - (tableview.css)
	 * @author jpk
	 */
	protected static class Styles {

		/**
		 * Style for a listing's nav bar.
		 */
		public static final String TABLE_VIEW_NAVBAR = "tvnav";
		/**
		 * Style for a listing's page container.
		 */
		public static final String PAGE_CONTAINER = "page";
		/**
		 * 
		 */
		public static final String SUMMARY = "smry";
		/**
		 * 
		 */
		public static final String PAGE = "tbPage";
	}

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
	public ListingNavBar(IListingConfig<R> config) {
		super();
		initialize(config);
	}

	/**
	 * Initializes the nav bar.
	 * @param config
	 */
	protected void initialize(IListingConfig<R> config) {
		assert config != null;

		this.listingElementName = config.getListingElementName();
		assert listingElementName != null;

		addStyleName(Styles.TABLE_VIEW_NAVBAR);

		Image split;

		pageSize = config.getPageSize();

		if(pageSize > 0) {
			imgPageFirst = imageBundle.page_first().createImage();
			imgPagePrev = imageBundle.page_prev().createImage();
			imgPageNext = imageBundle.page_next().createImage();
			imgPageLast = imageBundle.page_last().createImage();

			btnPageFirst = new PushButton(imgPageFirst, this);
			btnPagePrev = new PushButton(imgPagePrev, this);
			btnPageNext = new PushButton(imgPageNext, this);
			btnPageLast = new PushButton(imgPageLast, this);

			btnPageFirst.setTitle("Fist Page");
			btnPagePrev.setTitle("Previous Page");
			btnPageNext.setTitle("Next Page");
			btnPageLast.setTitle("Last Page");
			tbPage.setTitle("Current Page");

			tbPage.addKeyUpHandler(this);
			tbPage.addChangeHandler(this);
			tbPage.setMaxLength(4);
			tbPage.setStyleName(Styles.PAGE);

			// prev buttons (divs)
			add(btnPageFirst);
			add(btnPagePrev);

			// separator
			split = imageBundle.split().createImage();
			split.setStylePrimaryName(Toolbar.Styles.TOOLBAR_SEPARATOR);
			add(split);

			// Page x of y
			final FlowPanel pageXofY = new FlowPanel();
			pageXofY.addStyleName(Styles.PAGE_CONTAINER);
			pageXofY.add(lblPagePre);
			pageXofY.add(tbPage);
			pageXofY.add(lblPagePost);
			add(pageXofY);

			// separator
			split = imageBundle.split().createImage();
			split.setStylePrimaryName(Toolbar.Styles.TOOLBAR_SEPARATOR);
			add(split);

			// next buttons (divs)
			add(btnPageNext);
			add(btnPageLast);
		}

		// show refresh button?
		if(config.isShowRefreshBtn()) {
			imgRefresh = imageBundle.refresh().createImage();
			btnRefresh = new PushButton(imgRefresh, this);
			btnRefresh.setTitle("Refresh");

			if(pageSize > 0) {
				// separator
				split = imageBundle.split().createImage();
				split.setStylePrimaryName(Toolbar.Styles.TOOLBAR_SEPARATOR);
				add(split);
			}
			add(btnRefresh);
		}

		// show add button?
		if(config.getAddRowHandler() != null) {
			// imgAdd = imageBundle.add().createImage();
			final String title = "Add " + config.getListingElementName();
			btnAdd = new PushButton(title, this);
			btnAdd.setTitle(title);
			if(pageSize > 0 || config.isShowRefreshBtn()) {
				// separator
				split = imageBundle.split().createImage();
				split.setStylePrimaryName(Toolbar.Styles.TOOLBAR_SEPARATOR);
				add(split);
			}
			add(btnAdd);
		}

		// Displaying {listing element name} x - y of TOTAL
		lblSmry = new Label();
		lblSmry.setStyleName(Styles.SUMMARY);
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

	public void onClick(ClickEvent event) {
		final Object sender = event.getSource();
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
		((Focusable) sender).setFocus(false);
	}

	public void onKeyUp(KeyUpEvent event) {
		if(event.getSource() == tbPage) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				tbPage.setFocus(false); // force invocation of onChange() event.
			}
		}
	}

	public void onChange(ChangeEvent event) {
		if(event.getSource() == tbPage) {
			final String s = tbPage.getText();
			int page = 0;
			boolean valid = true;
			try {
				page = Integer.parseInt(s);
				if(page < 1 || page > numPages) {
					valid = false;
				}
			}
			catch(final NumberFormatException e) {
				valid = false;
			}
			if(!valid) {
				final String smsg = StringUtil.replaceVariables("Please enter a number between %1 and %2.", new Object[] {
					new Integer(1), new Integer(numPages) });
				Msgs.post(new Msg(smsg, MsgLevel.ERROR), tbPage, Position.BOTTOM, 3000, false);
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
				imageBundle.page_first_disabled().applyTo(imgPageFirst);
			}
			else {
				imageBundle.page_first().applyTo(imgPageFirst);
			}

			// last page btn
			btnPageLast.setEnabled(!isLastPage);
			if(isLastPage) {
				imageBundle.page_last_disabled().applyTo(imgPageLast);
			}
			else {
				imageBundle.page_last().applyTo(imgPageLast);
			}

			// prev page btn
			btnPagePrev.setEnabled(!isFirstPage);
			if(isFirstPage) {
				imageBundle.page_prev_disabled().applyTo(imgPagePrev);
			}
			else {
				imageBundle.page_prev().applyTo(imgPagePrev);
			}

			// next page btn
			btnPageNext.setEnabled(!isLastPage);
			if(isLastPage) {
				imageBundle.page_next_disabled().applyTo(imgPageNext);
			}
			else {
				imageBundle.page_next().applyTo(imgPageNext);
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
