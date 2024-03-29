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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
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
 * MutableMsgLevelPanel - Displays messages associated with a particular
 * referencable widget providing a link to that widget if it is focusable.
 * @author jpk
 */
public class MutableMsgLevelPanel extends Composite {

	/**
	 * Entry - A single msg entry for the msg panel.
	 * @author jpk
	 */
	static class Entry extends Composite {

		/**
		 * Sourced entry?
		 */
		final boolean sourced;

		/**
		 * Optional classifier id.
		 */
		final Integer classifier;

		Entry(Msg msg, final IWidgetRef ref, Integer classifier) {
			this.classifier = classifier;
			this.sourced = (ref != null && (ref.getWidget() instanceof Focusable));
			if(sourced) {
				final SimpleHyperLink liw = new SimpleHyperLink(html(msg, ref), true, new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						((Focusable) ref.getWidget()).setFocus(true);
					}
				});
				liw.setTitle(ref.descriptor());
				initWidget(liw);
			}
			else {
				// no ref simple error msg
				final P p = new P(msg.getMsg());
				initWidget(p);
			}
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

	} // Entry

	/**
	 * The filtering message level. Only messages matching this level are
	 * displayed.
	 */
	private final MsgLevel mlevel;

	/**
	 * Cache of reference widgets and their associated {@link Entry} widgets.
	 */
	private final Map<Widget, List<Entry>> entries = new HashMap<Widget, List<Entry>>();

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

	private void init() {
		container.setStyleName(MsgStyles.css().container());
		container.addStyleName(MsgStyles.getMsgLevelStyle(mlevel));
		initWidget(container);

		final ImageResource img = MsgStyles.getMsgLevelImage(mlevel);
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
		l.addStyleName(MsgStyles.css().title());
		fp.add(l);
		container.add(fp);
		container.add(list);
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

	/**
	 * Factory method for creating a single {@link Entry}.
	 * @param msg the required message
	 * @param ref optional ref widget where if specified, a link is created that
	 *        sets focus to the referenced widget. Otherwise the message text is
	 *        used.
	 * @param classifier optional classifier id
	 * @return widget that is added to the li tag to display the message
	 */
	private Entry entry(Msg msg, final IWidgetRef ref, Integer classifier) {
		if(msg == null) throw new IllegalArgumentException("Null msg");
		final Entry entry = new Entry(msg, ref, classifier);

		// cache it (a null widget is ok as we want to query un-ref'd messages
		// too)
		final Widget w = ref == null ? null : ref.getWidget();
		List<Entry> elist = entries.get(w);
		if(elist == null) {
			elist = new ArrayList<Entry>();
			entries.put(w, elist);
		}
		elist.add(entry);

		return entry;
	}

	/**
	 * Adds messages for a given referenced widget. Only those msgs that
	 * <em>match</em> this panel's message level are added.
	 * @param wref the referenced widget. May be <code>null</code>.
	 * @param msgs the messages to add
	 * @param classifier optional classifier id
	 */
	public void add(final IWidgetRef wref, Iterable<Msg> msgs, Integer classifier) {
		for(final Msg m : msgs) {
			add(wref, m, classifier);
		}
	}

	/**
	 * Adds un-sourced messages. Only those msgs that <em>match</em> this panel's
	 * message level are added.
	 * @param msgs the messages to add
	 * @param classifier optional classifier id
	 */
	public void add(Iterable<Msg> msgs, Integer classifier) {
		for(final Msg m : msgs) {
			add(null, m, classifier);
		}
	}

	/**
	 * Adds a single message for a given referenced widget <em>only</em> if the
	 * msg's level <em>matches</em> this panel's message level.
	 * @param wref the referenced widget. May be <code>null</code>.
	 * @param msg the message to add
	 * @param classifier optional classifier id
	 */
	public void add(final IWidgetRef wref, Msg msg, Integer classifier) {
		if(msg.getLevel() == mlevel) {
			list.append(entry(msg, wref, classifier));
		}
	}

	/**
	 * Adds a single un-sourced message for a given referenced widget
	 * <em>only</em> if the msg's level <em>matches</em> this panel's message
	 * level.
	 * @param msg the message to add
	 * @param classifier optional classifier id
	 */
	public void add(Msg msg, Integer classifier) {
		if(msg.getLevel() == mlevel) {
			list.append(entry(msg, null, classifier));
		}
	}

	/**
	 * Removes all existing messages assoc. with the given ref widget and,
	 * optionally, having the same classifier.
	 * @param wref the widget ref which may be <code>null</code> meaning all
	 *        unsourced messages.
	 * @param classifier optional classifier id
	 */
	public void remove(IWidgetRef wref, Integer classifier) {
		final List<Entry> elist = entries.get(wref == null ? null : wref.getWidget());
		if(elist != null) {
			final ArrayList<Entry> tormv = new ArrayList<Entry>();
			for(final Entry entry : elist) {
				if(classifier == null || (entry.classifier != null && entry.classifier.intValue() == classifier.intValue())) {
					list.remove(entry);
					tormv.add(entry);
				}
			}
			for(final Entry e : tormv) {
				elist.remove(e);
			}
		}
	}

	/**
	 * Removes all sourced and un-sourced messages bound to the given classifier
	 * id.
	 * @param classifier the classifier id.
	 */
	public void remove(int classifier) {
		for(final List<Entry> elist : entries.values()) {
			assert elist != null;
			final ArrayList<Entry> tormv = new ArrayList<Entry>();
			for(final Entry e : elist) {
				if(e.classifier != null && e.classifier.intValue() == classifier) {
					list.remove(e);
					tormv.add(e);
				}
			}
			for(final Entry e : tormv) {
				elist.remove(e);
			}
		}
	}

	/**
	 * Clears out <em>all<em> messages in this message panel.
	 */
	public void clear() {
		list.clear();
		entries.clear();
	}
}
