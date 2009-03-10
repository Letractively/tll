/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.ui.field;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.field.IFieldWidget.Styles;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.IError;
import com.tll.client.validate.PopupValidationFeedback;
import com.tll.client.validate.IError.Type;

/**
 * FieldErrorHandler - Localized error handling for field widgets.
 * @author jpk
 */
public class FieldErrorHandler extends PopupValidationFeedback implements IHoverHandler {

	/**
	 * MouseRegs
	 * @author jpk
	 */
	static final class MouseRegs {
		HandlerRegistration rMseOut, rMsgOvr;
	}

	/**
	 * The cache of the current invalid fields necessary to add/remove
	 * hoverability.
	 */
	private final Map<IFieldWidget<?>, MouseRegs> invalids = new HashMap<IFieldWidget<?>, MouseRegs>();
	

	/**
	 * Constructor
	 * @param mregistry
	 */
	public FieldErrorHandler(MsgPopupRegistry mregistry) {
		super(mregistry);
	}

	@Override
	public void handleError(IWidgetRef source, IError error, int attribs) {
		super.handleError(source, error, attribs);
		
		if(error.getType() == Type.SINGLE && Attrib.isLocal(attribs)) {
			if(source instanceof IFieldWidget) {
				// handle styling
				source.getWidget().removeStyleName(Styles.DIRTY);
				source.getWidget().addStyleName(Styles.INVALID);

				// track popup hovering
				MouseRegs regs = invalids.get(source);
				if(regs == null) {
					regs = new MouseRegs();
					invalids.put((IFieldWidget<?>) source, regs);
				}
				trackHover((IFieldWidget<?>) source, regs, true);
			}
		}
	}

	@Override
	public void resolveError(IWidgetRef source) {
		super.resolveError(source);
		if(source instanceof IFieldWidget) {
			// handle styling
			source.getWidget().removeStyleName(Styles.INVALID);
			
			// un-track popup hovering
			final MouseRegs regs = invalids.remove(source);
			if(regs != null) {
				trackHover((IFieldWidget<?>) source, regs, false);
			}
		}
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		final IFieldWidget<?> field = resolveField(event);
		if(field != null) {
			mregistry.getOperator(field.getWidget(), false).showMsgs(!field.isValid());
		}
	}

	@Override
	public void clear() {
		super.clear();
		for(final IFieldWidget<?> fw : invalids.keySet()) {
			trackHover(fw, invalids.get(fw), false);
		}
		invalids.clear();
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		final IFieldWidget<?> field = resolveField(event);
		if(field != null && !field.isValid()) {
			mregistry.getOperator(field.getWidget(), false).showMsgs(false);
		}
	}
	
	private IFieldWidget<?> resolveField(MouseEvent<?> event) {
		final Object src = event.getSource();
		for(final IFieldWidget<?> fw : invalids.keySet()) {
			if(src == fw || src == fw.getFieldLabel()) return fw;
		}
		return null;
	}

	/**
	 * Adds or removes hoverability to the given field.
	 * @param field the subject field
	 * @param mouseRegs the associated mouse registrations for the given field
	 * @param track track (add) or don't track (remove)?
	 */
	private void trackHover(IFieldWidget<?> field, MouseRegs mouseRegs, boolean track) {
		// resolve the hoverable
		if(track) {
			final IHasHoverHandlers hoverable = field.getFieldLabel() == null ? field.getEditable() : field.getFieldLabel();
			if(mouseRegs.rMseOut == null) mouseRegs.rMseOut = hoverable.addMouseOutHandler(this);
			if(mouseRegs.rMsgOvr == null) mouseRegs.rMsgOvr = hoverable.addMouseOverHandler(this);
		}
		else {
			if(mouseRegs.rMseOut != null) {
				mouseRegs.rMseOut.removeHandler();
				mouseRegs.rMseOut = null;
			}
			if(mouseRegs.rMsgOvr != null) {
				mouseRegs.rMsgOvr.removeHandler();
				mouseRegs.rMsgOvr = null;
			}
		}
	}
}
