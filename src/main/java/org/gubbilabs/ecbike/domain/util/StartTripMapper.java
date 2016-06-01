/*
 * @(#)StartTripMapper.java   12/04/08
 *
 * Copyright (c) 2012 Gubbi Labs http://www.gubbilabs.in.  All rights reserved.
 *
 * This file is part of NammaCycle - Public Bicycle Sharing Project.

 * NammaCycle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * NammaCycle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with NammaCycle.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * @author Murali Ramanath H
 * @version $Revision: 1.0
 * @date 6-Apr-2012
**/

package org.gubbilabs.ecbike.domain.util;

//~--- non-JDK imports --------------------------------------------------------

import org.gubbilabs.ecbike.domain.Bicycle;
import org.gubbilabs.ecbike.domain.MemberMobile;
import org.gubbilabs.ecbike.domain.RentalBufferNode;
import org.joda.time.DateTime;

import java.time.ZonedDateTime;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

public class StartTripMapper {
	String bicycleId;
	Long    rentalMemberId;
	ZonedDateTime   rentalStartTime;
	Long    startNodeId;
	
	
	public Long getRentalMemberId() {
		return rentalMemberId;
	}
	public void setRentalMemberId(Long rentalMemberId) {
		this.rentalMemberId = rentalMemberId;
	}
	public Long getStartNodeId() {
		return startNodeId;
	}
	public void setStartNodeId(Long startNodeId) {
		this.startNodeId = startNodeId;
	}
	public String getBicycleId() {
		return bicycleId;
	}
	public void setBicycleId(String bicycleId) {
		this.bicycleId = bicycleId;
	}
 
	public ZonedDateTime getRentalStartTime() {
		return rentalStartTime;
	}
	public void setRentalStartTime(ZonedDateTime rentalStartTime) {
		this.rentalStartTime = rentalStartTime;
	}
	 
 

}
