package com.tll.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.tll.client.IMarshalable;

/**
 * DateRange
 * @author jpk
 */
public class DateRange implements IMarshalable, Cloneable {

	public static final Date DATE_PAST = new Date(0L);

	public static final Date DATE_FUTURE = new Date(63387360000000L); // year 2010

	public static DateRange upTo(Date end) {
		return new DateRange(DATE_PAST, end);
	}

	public static DateRange startingOn(Date start) {
		return new DateRange(start, DATE_FUTURE);
	}

	public static DateRange empty() {
		return new DateRange(DATE_PAST, DATE_FUTURE);
	}

	private Date start;
	private Date end;

	public DateRange() {
		clear();
	}

	public DateRange(Date start, Date end) {
		this.start = (start == null ? DATE_PAST : start);
		this.end = (end == null ? DATE_FUTURE : end);
	}

	public String descriptor() {
		return toString();
	}

	public Date end() {
		return end;
	}

	public Date start() {
		return start;
	}

	public Date getEndDate() {
		return end;
	}

	public Date getStartDate() {
		return start;
	}

	public void setEnd(Date end) {
		this.end = (end == null ? DATE_FUTURE : end);
	}

	public void setStart(Date start) {
		this.start = (start == null ? DATE_PAST : start);
	}

	public boolean includes(Date arg) {
		if(arg == null) return false;
		return !arg.before(start) && !arg.after(end);
	}

	@Override
	public String toString() {
		return isEmpty() ? "Empty Date Range" : (DateTimeFormat.getShortDateFormat().format(start) + " - " + DateTimeFormat
				.getShortDateFormat().format(end));
	}

	public boolean isEmpty() {
		return (DATE_PAST.equals(start) && DATE_FUTURE.equals(end)) || start.after(end);
	}

	@Override
	public boolean equals(Object arg) {
		if(!(arg instanceof DateRange)) return false;
		DateRange other = (DateRange) arg;
		return start.equals(other.start) && end.equals(other.end);
	}

	@Override
	public int hashCode() {
		return start.hashCode() * 7 * end.hashCode();
	}

	public boolean overlaps(DateRange arg) {
		if(arg == null) return false;
		return arg.includes(start) || arg.includes(end) || this.includes(arg);
	}

	public boolean includes(DateRange arg) {
		if(arg == null) return false;
		return this.includes(arg.start) && this.includes(arg.end);
	}

	public void clear() {
		this.start = DATE_PAST;
		this.end = DATE_FUTURE;
	}

	/*
	public Object clone() {
		try {
			DateRange cln = (DateRange) super.clone();

			cln.start = (Date) start.clone();
			cln.start = (Date) start.clone();

			return cln;
		}
		catch(CloneNotSupportedException cnse) {
			throw new IllegalStateException("Unable to a clone a DateRange! NOT SUPPORTED");
		}
	}
	*/
}
