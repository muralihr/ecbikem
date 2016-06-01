package org.gubbilabs.ecbike.domain.util;

public class FoundTripResponseJsonObject {

	String status;
	int 	code;
	String 	message;
	long	tripid;
	StartTripMapper tripMapper;
	
	 
	public StartTripMapper getTripMapper() {
		return tripMapper;
	}
	public void setTripMapper(StartTripMapper tripMapper) {
		this.tripMapper = tripMapper;
	}
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
