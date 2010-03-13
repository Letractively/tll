/**
 * The Logic Lab
 * @author jpk
 * Jul 11, 2008
 */
package com.tll.client.storage;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;

/**
 * CookieStorage
 * @author jpk
 */
public class CookieStorage extends Storage {

	private static class CookieConstants {

		public String cookiePrefix() {
			return "_cs";
		}

		public int maxCookies() {
			return 20;
		}

		public int maxCookieLength() {
			return 4096;
		}

		public int maxTotalStorage() {
			return maxCookies() * maxCookieLength();
		}
	}

	@SuppressWarnings( {
		"unused", "synthetic-access" })
	private static class CookieConstantsIE6 extends CookieConstants {

		@Override
		public int maxCookies() {
			return 1;
		}
	}

	private static CookieConstants constants = GWT.create(CookieConstants.class);

	/**
	 * Constructor
	 * @throws StorageException
	 */
	public CookieStorage() throws StorageException {
		super();
	}

	@Override
	public void load() /*throws StorageException*/{
		// read the cookies to build the string
		String valueString = "";
		String cookieValue = Cookies.getCookie(constants.cookiePrefix() + "0");
		for(int cookieNum = 0; cookieValue != null && cookieValue.length() > 0;) {
			valueString += cookieValue;
			++cookieNum;
			cookieValue = Cookies.getCookie(constants.cookiePrefix() + cookieNum);
		}

		// set up the hash map from the string
		// TODO implement
		// setValuesFromString(valueString);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void save() throws StorageException {
		// clear cookies
		Date now = new Date();
		Date expires = new Date(now.getYear(), now.getMonth() - 1, now.getDate());
		for(int i = 0; i < constants.maxCookies(); ++i) {
			Cookies.setCookie(constants.cookiePrefix() + i, "", expires);
		}

		// get the values as a string and check their length
		// String valueString = getValuesAsString();
		// TODO implement
		String valueString = ""; // temp
		if(valueString.length() > constants.maxTotalStorage()) {
			throw new StorageException("Out of cookie storage space");
		}

		// set the expire date to 30 days
		expires = new Date(now.getYear(), now.getMonth() + 1, now.getDate());

		// set the cookies in chunks
		int cookiesRequired = (valueString.length() / constants.maxCookieLength()) + 1;
		for(int cookieNum = 0; cookieNum < cookiesRequired; ++cookieNum) {
			int begin = cookieNum * constants.maxCookieLength();
			int end = Math.min(begin + constants.maxCookieLength(), valueString.length());
			String cookieValue = valueString.substring(begin, end);
			Cookies.setCookie(constants.cookiePrefix() + cookieNum, cookieValue, expires);
		}
	}

}
