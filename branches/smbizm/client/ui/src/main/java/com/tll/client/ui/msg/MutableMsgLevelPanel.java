/**
 * The Logic Lab
 * @author jpk
 * Mar 2, 2009
 */
package com.tll.client.ui.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.ImageContainer;
import com.tll.client.ui.P;
import com.tll.client.ui.SimpleHyperLink;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * SourcedMsgPanel - Displays messages associated with a particular referencable
 * widget providing a link to that widget if it is focusable.
 * @author jpk
 */
public class MutableMsgLevelPanel extends Composite {

	/**
	 * Styles - (msg.css)
	 * @author jpk
	 */
	static class Styles {

		/**
		 * Style applied to to the containing div that contains the table.
		 */
		public static final String CONTAINER = "container";

		/**
		 * Style applied to to widgets containing messages.
		 */
		public static final String MSG = "msg";
		
		/**
		 * Style applied to the title of this panel.
		 */
		public static final String TITLE = "title";
	}
	
	/**
	 * The filtering message level. Only messages matching this level are
	 * displayed.
	 */
	private final MsgLevel mlevel;

	/**
	 * Cache of reference widgets and their associated li entry widgets.
	 */
	private final Map<IWidgetRef, List<Widget>> entries = new HashMap<IWidgetRef, List<Widget>>();

	/**
	 * Unordered HTML list of entries.
	 */
	private final HtmlListPanel list = new HtmlListPanel(false);

	/**
	 * The wrapped div.
	 */
	private final FlowPanel container = new FlowPanel();

	/**
	 * Constructor
	 * @param mlevel the message level for this panel.
	 */
	public MutableMsgLevelPanel(MsgLevel mlevel) {
		super();
		this.mlevel = mlevel;
		init();
	}
	
	public MsgLevel getMsgLevel() {
		return mlevel;
	}
	
	/**
	 * @return The number of messages in this panel.
	 */
	public int size() {
		return list.size();
	}

	private void init() {
		container.setStyleName(Styles.CONTAINER);
		container.addStyleName(mlevel.getName().toLowerCase());
		initWidget(container);
		
		final Image img = Util.getMsgLevelImage(mlevel);
		// NOTE: since this is a clipped image, the width/height should be known
		final FlowPanel fp = new FlowPanel();
		fp.add(new ImageContainer(img));
		String title;
		switch(mlevel) {
			case ERROR:
				title = "Errors";
				break;
			case FATAL:
				title = "Fatal Errors";
				break;
			default:
			case INFO:
				title = "Info";
				break;
			case WARN:
				title = "Warnings";
				break;
		}
		final Label l = new Label(title);
		l.setStyleName("bold");
		l.addStyleName(Styles.TITLE);
		fp.add(l);
		container.add(fp);
		container.add(list);
	}
	
	private String html(Msg msg, IWidgetRef ref) {
		assert msg != null && ref != null;
		final StringBuilder sb = new StringBuilder();
		sb.append("<b>");
		sb.append(ref.descriptor());
		sb.append(":</b>&nbsp;&nbsp;");
		sb.append(msg.getMsg());
		return sb.toString();
	}

	/**
	 * Creates the widget contained in the li widget conveying the message.
	 * @param msg the required message
	 * @param ref optional ref widget where if specified, a link is created that
	 *        sets focus to the referenced widget. Otherwise the message text is
	 *        used.
	 * @return widget that is added to the li tag to display the message
	 */
	private Widget liEntry(Msg msg, final IWidgetRef ref) {
		Widget liw;
		if(ref != null && (ref.getWidget() instanceof Focusable)) {
			liw = new SimpleHyperLink(html(msg, ref), true, new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					((Focusable) ref.getWidget()).setFocus(true);
				}
			});
			liw.setTitle(ref.descriptor());
		}
		else {
			// no ref simple error msg
			liw = new P(msg.getMsg());
		}

		// cache it (a null ref is ok as we want to query un-ref'd messages too)
		List<Widget> l = entries.get(ref);
		if(l == null) {
			l = new ArrayList<Widget>();
			entries.put(ref, l);
		}
		l.add(liw);
		
		return liw;
	}

	/**
	 * Adds messages for a given referenced widget. Only those msgs that
	 * <em>match</em> this panel's message level are added.
	 * @param wref the referenced widget. May be <code>null</code>.
	 * @param msgs the messages to add
	 */
	public void add(final IWidgetRef wref, Iterable<Msg> msgs) {
		for(final Msg m : msgs) {
			add(wref, m);
		}
	}

	/**
	 * Adds un-sourced messages. Only those msgs that <em>match</em> this panel's
	 * message level are added.
	 * @param msgs the messages to add
	 */
	public void add(Iterable<Msg> msgs) {
		for(final Msg m : msgs) {
			add(null, m);
		}
	}

	/**
	 * Adds a single message for a given referenced widget <em>only</em> if the
	 * msg's level <em>matches</em> this panel's message level.
	 * @param wref the referenced widget. May be <code>null</code>.
	 * @param msg the message to add
	 */
	public void add(final IWidgetRef wref, Msg msg) {
		if(msg.getLevel() == mlevel) {
			list.append(liEntry(msg, wref));
		}
	}

	/**
	 * Adds a single un-sourced message for a given referenced widget
	 * <em>only</em> if the msg's level <em>matches</em> this panel's message
	 * level.
	 * @param msg the message to add
	 */
	public void add(Msg msg) {
		if(msg.getLevel() == mlevel) {
			list.append(liEntry(msg, null));
		}
	}

	/**
	 * Removes all existing messages associated with a given referenced widget.
	 * @param wref the referenced widget
	 */
	public void remove(IWidgetRef wref) {
		final List<Widget> tormv = entries.get(wref);
		if(tormv != null) {
			for(final Widget liw : tormv) {
				list.remove(liw);
			}
			entries.remove(wref);
		}
	}

	/**
	 * Removes all un-sourced messages from this panel.
	 */
	public void removeUnsourced() {
		remove(null);
	}

	/**
	 * Clears out <em>all<em> messages in this message panel.
	 */
	public void clear() {
		list.clear();
		entries.clear();
	}
}
