/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.IField.LabelMode;
import com.tll.client.ui.Br;
import com.tll.client.ui.CSS;
import com.tll.client.util.StringUtil;

/**
 * FieldAdapter - Use as a base class for {@link Widget}s wishing to have field
 * type UI styling.
 * @author jpk
 */
public abstract class FieldAdapter extends Composite implements HasHorizontalAlignment {

	private static final String CSS_FIELD = "fld";

	/**
	 * The wrapped UI panel containing all the field sub-widgets.
	 */
	protected final FlowPanel fp = new FlowPanel();

	/**
	 * The optional field label.
	 */
	protected final FieldLabel fldLbl;

	protected final LabelMode lblMode;
	protected String lblTxt;

	/**
	 * Constructor
	 * @param lblTxt
	 * @param lblMode
	 */
	public FieldAdapter(String lblTxt, LabelMode lblMode) {
		super();
		this.lblTxt = lblTxt;
		this.lblMode = lblMode;
		initWidget(fp);
		fp.setStyleName(CSS_FIELD);
		fldLbl = (lblMode == LabelMode.NONE) ? null : lblTxt == null ? null : new FieldLabel(lblTxt);
		if(fldLbl != null) {
			fp.add(fldLbl);
			if(lblMode == LabelMode.ABOVE) fp.add(new Br());
		}
	}

	public HorizontalAlignmentConstant getHorizontalAlignment() {
		final String s = getStyleName();
		if(StringUtil.isEmpty(s)) return null;
		if(s.indexOf(CSS.TEXT_CENTER) >= 0) return ALIGN_CENTER;
		if(s.indexOf(CSS.TEXT_RIGHT) >= 0) return ALIGN_RIGHT;
		return ALIGN_LEFT;
	}

	public void setHorizontalAlignment(HorizontalAlignmentConstant align) {
		removeStyleName(CSS.TEXT_CENTER);
		removeStyleName(CSS.TEXT_RIGHT);
		if(ALIGN_CENTER.equals(align)) {
			addStyleName(CSS.TEXT_CENTER);
		}
		else if(ALIGN_RIGHT.equals(align)) {
			addStyleName(CSS.TEXT_RIGHT);
		}
	}

}
