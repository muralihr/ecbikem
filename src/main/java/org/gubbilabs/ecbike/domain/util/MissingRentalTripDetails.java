package org.gubbilabs.ecbike.domain.util;

import java.time.ZonedDateTime;

import org.joda.time.DateTime;

public class MissingRentalTripDetails
{
	private Long id;
	private Long memberid;
	private String memberName;	
	private String cycle;
	private String startNode;
	private ZonedDateTime starttime;
	 
	
	public MissingRentalTripDetails() {
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
	 
	
	 
}
 

