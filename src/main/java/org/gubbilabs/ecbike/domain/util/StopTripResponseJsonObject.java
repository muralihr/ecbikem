package org.gubbilabs.ecbike.domain.util;

public class StopTripResponseJsonObject {

	String status;
	int 	code;
	String 	message;
	long	tripid;
	int deductibeUnits;
	float rentedHours;
	
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
	public int getDeductibeUnits() {
		return deductibeUnits;
	}
	public void setDeductibeUnits(int deductibeUnits) {
		this.deductibeUnits = deductibeUnits;
	}
	public float getRentedHours() {
		return rentedHours;
	}
	public void setRentedHours(float rentedHours) {
		this.rentedHours = rentedHours;
	}

}
