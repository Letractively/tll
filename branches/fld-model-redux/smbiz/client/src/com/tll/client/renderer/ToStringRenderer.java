/*
 * ToStringRenderer.java
 *
 * Created on April 12, 2007, 12:57 PM
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.tll.client.renderer;

import com.tll.util.INameValueProvider;

/**
 * ToStringRenderer - Renders an arbitrary {@link Object} to a {@link String}.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public class ToStringRenderer implements IRenderer<String, Object> {

	public static final ToStringRenderer INSTANCE = new ToStringRenderer();

	/**
	 * Constructor
	 */
	private ToStringRenderer() {
	}

	/**
	 * Translates an arbitrary {@link Object} instance to a non-<code>null</code>
	 * {@link String} instance.
	 * @return A never <code>null</code> {@link String}.
	 */
	@SuppressWarnings("unchecked")
	public String render(Object o) {
		if(o != null) {
			if(o instanceof INameValueProvider) {
				return ((INameValueProvider) o).getName();
			}
		}
		return o == null ? "" : o.toString();
	}
}
