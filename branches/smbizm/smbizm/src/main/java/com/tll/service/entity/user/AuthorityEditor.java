package com.tll.service.entity.user;

import java.beans.PropertyEditorSupport;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.tll.model.Authority;
import com.tll.util.StringUtil;

/**
 * @author jpk
 */
public class AuthorityEditor extends PropertyEditorSupport {

	protected List<Authority> masterAuthorities;

	/**
	 * Constructor
	 * @param masterAuthorities
	 */
	public AuthorityEditor(List<Authority> masterAuthorities) {
		super();
		this.masterAuthorities = masterAuthorities;
	}

	protected String formatText(String text) {
		return StringUtil.enumStyleToPresentation(StringUtils.replace(text, "ROLE_", ""));
	}

	@Override
	public String getAsText() {
		final Authority a = (Authority) getValue();
		return a == null ? "" : formatText(a.getAuthority());
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(text == null || text.length() < 1) throw new IllegalArgumentException("No authority specified");
		for(final Authority a : masterAuthorities) {
			if(text.equals(formatText(a.getAuthority()))) {
				setValue(a);
				return;
			}
		}
		throw new IllegalArgumentException("Unrecognized authority: '" + text + "'");
	}

}
