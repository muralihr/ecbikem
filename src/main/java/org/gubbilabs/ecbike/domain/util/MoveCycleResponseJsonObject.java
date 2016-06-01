package org.gubbilabs.ecbike.domain.util;

public class MoveCycleResponseJsonObject {

	String status;
	int 	code;
	String 	message;
	long	bicycleId;
	long rentalNodeMapperId;
	long stockNodeMapperId;
	
	 
	public long getBicycleId() {
		return bicycleId;
	}
	public void setBicycleId(long bicycleId) {
		this.bicycleId = bicycleId;
	}
	public long getRentalNodeMapperId() {
		return rentalNodeMapperId;
	}
	public void setRentalNodeMapperId(long rentalNodeMapperId) {
		this.rentalNodeMapperId = rentalNodeMapperId;
	}
	public long getStockNodeMapperId() {
		return stockNodeMapperId;
	}
	public void setStockNodeMapperId(long stockNodeMapperId) {
		this.stockNodeMapperId = stockNodeMapperId;
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
 

}
