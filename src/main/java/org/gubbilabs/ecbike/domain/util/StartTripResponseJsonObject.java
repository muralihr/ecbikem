package org.gubbilabs.ecbike.domain.util;

public class StartTripResponseJsonObject {

	String status;
	int 	code;
	String 	message;
	long	tripid;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTripid() {
		return tripid;
	}
	public void setTripid(long tripid) {
		this.tripid = tripid;
	}
	 

}
