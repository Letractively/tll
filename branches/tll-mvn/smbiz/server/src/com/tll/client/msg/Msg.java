package com.tll.client.msg;

import com.tll.IMarshalable;

/**
 * Represents a single status msg for use in the UI.
 * @author jpk
 */
public final class Msg implements IMarshalable {

	public static enum MsgLevel {
		INFO("Info"),
		WARN("Warn"),
		ERROR("Error"),
		FATAL("Fatal");

		private final String name;

		private MsgLevel(final String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public boolean isError() {
			return this == ERROR || this == FATAL;
		}
	}

	public static enum MsgAttr {
		/**
		 * Bit mask flag for indicating NO flags.
		 */
		NONE(0),
		/**
		 * Bit mask flag for indicating this message represents a status.
		 */
		STATUS(1),
		/**
		 * Bit mask flag for messages associated with UI fields.
		 */
		FIELD(2),
		/**
		 * Bit mask flag for indicating this message should not be displayed.
		 */
		NODISPLAY(4),
		/**
		 * Bit mask flag for indicating this message is associated with an exception
		 * that occurred.
		 */
		EXCEPTION(8);

		public final int flag;

		private MsgAttr(int flag) {
			this.flag = flag;
		}
	}

	private String msg;

	private MsgLevel level;

	/**
	 * Attribute flags
	 */
	private int attribs = MsgAttr.NONE.flag;

	/**
	 * Identifies a particular UI artifact to which this message is associated.
	 * <p>
	 * For messages bound to a field, the ref token is the property name.
	 */
	private String refToken;

	/**
	 * Constructor
	 */
	public Msg() {
	}

	/**
	 * Constructor
	 * @param msg
	 * @param level
	 */
	public Msg(final String msg, final MsgLevel level) {
		super();
		if(msg == null || msg.length() < 1) {
			throw new IllegalArgumentException("null or empty msg argument not allowed");
		}
		this.msg = msg;
		this.level = level;
	}

	/**
	 * Constructor
	 * @param msg The message text
	 * @param level The message level
	 * @param attribs Attribute flags
	 * @param refToken A reference token. May be <code>null</code>.
	 */
	public Msg(final String msg, final MsgLevel level, final int attribs, final String refToken) {
		this(msg, level);
		this.attribs = attribs;
		setRefToken(refToken);
	}

	public String getMsg() {
		return msg;
	}

	public MsgLevel getLevel() {
		return level;
	}

	public boolean hasAttribute(final MsgAttr attr) {
		return (attribs & attr.flag) == attr.flag;
	}

	public void addAttribute(final int attrib) {
		attribs = attribs | attrib;
	}

	public void setAttributes(final int attribs) {
		this.attribs = attribs;
	}

	public String getRefToken() {
		return refToken;
	}

	public void setRefToken(final String refToken) {
		if(hasAttribute(MsgAttr.FIELD) && refToken == null) {
			throw new IllegalArgumentException("A field bound message must specify a ref token");
		}
		this.refToken = refToken;
	}

	@Override
	public boolean equals(final Object o) {
		if(this == o) return true;
		if(o == null || o instanceof Msg == false) return false;
		final Msg that = (Msg) o;
		return msg != null && msg.equals(that.msg) && level == that.level;
	}

	@Override
	public int hashCode() {
		return (msg == null ? 0 : msg.hashCode()) + 7 * level.hashCode();
	}

	@Override
	public String toString() {
		String s = level.getName() + ": '" + msg + "'";
		String flags = "";
		if(hasAttribute(MsgAttr.STATUS)) {
			flags += "|Status";
		}
		if(hasAttribute(MsgAttr.FIELD)) {
			flags += "|Field";
		}
		if(hasAttribute(MsgAttr.NODISPLAY)) {
			flags += "|No Display";
		}
		if(flags.startsWith("|")) {
			s += (" [" + flags.substring(1) + ']');
		}
		if(refToken != null) {
			s += " (refToken: " + refToken + ")";
		}
		return s;
	}
}
