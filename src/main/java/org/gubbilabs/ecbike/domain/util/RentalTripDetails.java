package org.gubbilabs.ecbike.domain.util;

import java.time.ZonedDateTime;

import org.joda.time.DateTime;

public class RentalTripDetails
{
	private Long id;
	private Long memberid;
	private String memberName;	
	private String cycle;
	private String startNode;
	private ZonedDateTime starttime;
	private String stopNode;
	private ZonedDateTime stopTime;
	private Float duration;
	private Integer units;
	
	public RentalTripDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberid() {
		return memberid;
	}
	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getStartNode() {
		return startNode;
	}
	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}
	public ZonedDateTime getStarttime() {
		return starttime;
	}
	public void setStarttime(ZonedDateTime starttime) {
		this.starttime = starttime;
	}
	public String getStopNode() {
		return stopNode;
	}
	public void setStopNode(String stopNode) {
		this.stopNode = stopNode;
	}
	public ZonedDateTime getStopTime() {
		return stopTime;
	}
	public void setStopTime(ZonedDateTime stopTime) {
		this.stopTime = stopTime;
	}
	public Float getDuration() {
		return duration;
	}
	public void setDuration(Float duration) {
		this.duration = duration;
	}
	public Integer getUnits() {
		return units;
	}
	public void setUnits(Integer units) {
		this.units = units;
	}
	
	 
}
 

