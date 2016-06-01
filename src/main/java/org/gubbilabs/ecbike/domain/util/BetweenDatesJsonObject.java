package org.gubbilabs.ecbike.domain.util;

import org.joda.time.DateTime;

public class BetweenDatesJsonObject 
{
	String fromDate;
	String toDate;
	
	public BetweenDatesJsonObject() {
		super();
		 
	}
	public BetweenDatesJsonObject(String fromDate, String toDate) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}	 
}
