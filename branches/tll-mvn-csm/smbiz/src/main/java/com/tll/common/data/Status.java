/**
 * 
 */
package com.tll.common.data;

import java.util.ArrayList;
import java.util.List;

import com.tll.IMarshalable;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgAttr;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * Used to transport "status" messages from the server to the client.
 * @author jpk
 */
public final class Status implements IMarshalable {

	private boolean errors = false;

	private List<Msg> msgs;

	public Status() {
	}

	public Status(List<Msg> msgs) {
		addMsgs(msgs);
	}

	public Status(Msg msg) {
		addMsg(msg);
	}

	public Status(String msg, MsgLevel level) {
		addMsg(new Msg(msg, level));
	}

	public boolean hasErrors() {
		return errors;
	}

	public List<Msg> getAllMsgs() {
		return msgs;
	}

	/**
	 * @return {@link List} of messages deemed for global display.
	 */
	public List<Msg> getGlobalDisplayMsgs() {
		if(msgs == null) return null;
		final List<Msg> list = new ArrayList<Msg>();
		for(final Msg msg : msgs) {
			if(!msg.hasAttribute(MsgAttr.NODISPLAY) && !msg.hasAttribute(MsgAttr.EXCEPTION)) {
				list.add(msg);
			}
		}
		return list;
	}

	/**
	 * @return {@link List} of messages associated with a UI field.
	 */
	public List<Msg> getFieldMsgs() {
		if(msgs == null) return null;
		final List<Msg> list = new ArrayList<Msg>();
		for(final Msg msg : msgs) {
			if(msg.hasAttribute(MsgAttr.FIELD)) {
				list.add(msg);
			}
		}
		return list;
	}

	public void addMsgs(List<Msg> msgs) {
		if(msgs == null || msgs.size() < 1) {
			return;
		}
		if(this.msgs == null) {
			this.msgs = new ArrayList<Msg>();
		}
		this.msgs.addAll(msgs);
		for(final Msg msg : msgs) {
			if(msg.getLevel().isError()) {
				this.errors = true;
				break;
			}
		}
	}

	public int getNumTotalMsgs() {
		return msgs == null ? 0 : msgs.size();
	}

	public void addMsg(Msg msg) {
		if(msg == null) return;
		if(msgs == null) {
			msgs = new ArrayList<Msg>();
		}
		msgs.add(msg);
		errors = (errors || msg.getLevel().isError());
	}

	/**
	 * Adds a {@link Msg} with a specified ref token.
	 * <p>
	 * NOTE: {@link MsgAttr#STATUS} attribute flag is ORd to the message
	 * attributes since this message is part of this {@link Status} instance.
	 * @param msg
	 * @param level
	 * @param attribs
	 * @param refToken
	 * @see Msg#Msg(String, MsgLevel, int, String)
	 */
	public void addMsg(String msg, MsgLevel level, int attribs, String refToken) {
		addMsg(new Msg(msg, level, (attribs | MsgAttr.STATUS.flag), refToken));
	}

	/**
	 * Adds a {@link Msg} w/ no ref token.
	 * @param msg
	 * @param level
	 * @param attribs
	 */
	public void addMsg(String msg, MsgLevel level, int attribs) {
		addMsg(msg, level, attribs, null);
	}

	/**
	 * Adds a {@link Msg} w/ no ref token and no attributes.
	 * <p>
	 * <strong>NOTE: </strong>Adding messages via this method will, by default, be
	 * displayed in the UI.
	 * @param msg
	 * @param level
	 */
	public void addMsg(String msg, MsgLevel level) {
		addMsg(msg, level, MsgAttr.NONE.flag, null);
	}

	@Override
	public String toString() {
		if(msgs == null || msgs.size() < 1) return "";
		String msg = new String();
		for(final Msg sm : msgs) {
			msg += sm.toString();
			msg += "  ";
		}
		return msg;
	}
}
