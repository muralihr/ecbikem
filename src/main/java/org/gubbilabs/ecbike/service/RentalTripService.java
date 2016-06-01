package org.gubbilabs.ecbike.service;

import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import org.gubbilabs.ecbike.domain.Bicycle;
import org.gubbilabs.ecbike.domain.HourlyRentalCharges;
import org.gubbilabs.ecbike.domain.MemberMobile;
import org.gubbilabs.ecbike.domain.RentalBufferNode;
import org.gubbilabs.ecbike.domain.RentalTrip;
import org.gubbilabs.ecbike.domain.util.FoundTripResponseJsonObject;
import org.gubbilabs.ecbike.domain.util.MissingRentalTripDetails;
import org.gubbilabs.ecbike.domain.util.RentalTripDetails;
import org.gubbilabs.ecbike.domain.util.StartTripMapper;
import org.gubbilabs.ecbike.domain.util.StartTripResponseJsonObject;
import org.gubbilabs.ecbike.domain.util.StopTripMapper;
import org.gubbilabs.ecbike.domain.util.StopTripResponseJsonObject;
import org.gubbilabs.ecbike.repository.BicycleRepository;
import org.gubbilabs.ecbike.repository.CycleToCustomerMapperRepository;
import org.gubbilabs.ecbike.repository.CycleToRentalNodeMapperRepository;
import org.gubbilabs.ecbike.repository.HourlyRentalChargesRepository;
import org.gubbilabs.ecbike.repository.MemberMobileRepository;
import org.gubbilabs.ecbike.repository.RentalBufferNodeRepository;
import org.gubbilabs.ecbike.repository.RentalTripRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class RentalTripService {

	private final Logger log = LoggerFactory.getLogger(RentalTripService.class);

	@Inject
	private RentalBufferNodeRepository rentalBufferNodeRepository;
	@Inject
	private BicycleRepository bicycleRepository;
	@Inject
	private CycleToRentalNodeMapperRepository cycleToRentalNodeMapperRepository;
 
	@Inject
	private CycleToCustomerMapperRepository cycleToCustomerMapperRepository;
	
	@Inject
	private RentalTripRepository rentalTripRepository;
	 @Inject
	private HourlyRentalChargesRepository hourlyRentalChargesRepository;
	@Inject
	private MemberMobileRepository memberMobileRepository;

    @Inject
    private CycleMovementService cycleMovementService;
	
	
	public ResponseEntity<StartTripResponseJsonObject> startRentalTrip(
			 StartTripMapper startTripMapperObj) throws URISyntaxException {

		log.debug("startRentalTrip details :::getRentalBicyclegetStartNode" + startTripMapperObj.getBicycleId());
		log.debug("startRentalTrip details :::getRentalMember" + startTripMapperObj.getRentalMemberId());
		log.debug("startRentalTrip details :::" + startTripMapperObj.getStartNodeId());
		StartTripResponseJsonObject startTripResponseJsonObject = new StartTripResponseJsonObject();
		if (startTripMapperObj == null) {
			log.debug("startRentalTrip is null");
			throw new IllegalArgumentException("A startTripMapperObj is required");

		}
//check start 
		MemberMobile rentedMember = null;
		rentedMember = memberMobileRepository.findOne((long) startTripMapperObj.getRentalMemberId());
		if (rentedMember == null) {
			startTripResponseJsonObject.setCode(399);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Not able to find member mobile");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);
		} else if (this.checkIfMemberThief(rentedMember) == true) {
			startTripResponseJsonObject.setCode(401);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Member Thief");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);
		} else if (this.checkIfMemberBlackListed(rentedMember) == true) {
			startTripResponseJsonObject.setCode(402);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Member Black Listed");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);
		} else if (this.checkIfMemberDateExpired(rentedMember) == true) {
			startTripResponseJsonObject.setCode(403);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Member Subscription Date Expired");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);

		} else if (this.checkIfMemberTakenCycles(rentedMember) == true) {
			startTripResponseJsonObject.setCode(404);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("member has taken cycle");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);

		} else if (this.checkIfMemberHasCurrency(rentedMember) == true) {
			startTripResponseJsonObject.setCode(405);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("member has no currency");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);

		}

		RentalBufferNode rentalBufferNode;
		rentalBufferNode = rentalBufferNodeRepository.findOne((long) startTripMapperObj.getStartNodeId());
		if (rentalBufferNode == null) {
			startTripResponseJsonObject.setCode(406);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Not able to find NODE");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);
		}

		Bicycle rentedBicycle = bicycleRepository.findByTagId(startTripMapperObj.getBicycleId());
		log.debug("startRentalTrip details :::rentedBicycle" + rentedBicycle);
		if (rentedBicycle == null) {
			startTripResponseJsonObject.setCode(407);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Not able to find cycle");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);
		}
		log.debug("startRentalTrip details :::rentedMember" + rentedMember);
		log.debug("startRentalTrip details :::rentalBufferNode" + rentalBufferNode);
		DateTime dt = null;
		ZonedDateTime zdt = null;
		if(startTripMapperObj.getRentalStartTime() == null)
		{
			  dt = new DateTime();
			  zdt = dt.toGregorianCalendar().toZonedDateTime();
		}
		else
		{
			zdt = startTripMapperObj.getRentalStartTime() ;
		}
		//check stop  
		RentalTrip r = new RentalTrip();
		r.setCustomer(rentedMember);
		r.setStartNode(rentalBufferNode);
		r.setRentedCycle(rentedBicycle); 
	 	r.setRentStartTime(zdt);
	
		cycleMovementService.moveCycleFromNodeToMember(rentedBicycle, rentedMember, rentalBufferNode );
		 
		RentalTrip result = rentalTripRepository.save(r);
		startTripResponseJsonObject.setCode(200);
		startTripResponseJsonObject.setMessage("trip created succesfully");
		startTripResponseJsonObject.setTripid(result.getId());
		startTripResponseJsonObject.setStatus("OK");
		return ResponseEntity.accepted().body(startTripResponseJsonObject);
	}
	
	private boolean checkIfMemberDateExpired(MemberMobile rentedMember) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean checkIfMemberHasCurrency(MemberMobile rentedMember) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean checkIfMemberTakenCycles(MemberMobile rentedMember) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean checkIfMemberBlackListed(MemberMobile rentedMember) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean checkIfMemberThief(MemberMobile rentedMember) {
		// TODO Auto-generated method stub
		return false;
	}
	 
	private HourlyRentalCharges findHourlyRentalCharges(Integer rentedHoursInADay) {
		// TODO Auto-generated method stub
	 
		List<HourlyRentalCharges> listCharges = hourlyRentalChargesRepository.findAll();
		log.debug("HourlyRentalCharges" + listCharges.toArray());

		HourlyRentalCharges d = findRentalChargeFor(listCharges, rentedHoursInADay);
		if (d == null)
		{
			d  = listCharges.get(listCharges.size()-1);
		}

		return d;
	}
	 public static boolean betweenExclusive(Float  x, Float min, Float max)
	   {
	       return x>=min && x<=max;    
	   }
	 
	 
	private HourlyRentalCharges findRentalChargeFor(List<HourlyRentalCharges> listCharges, double duration) {
		// TODO Auto-generated method stub

		Comparator<HourlyRentalCharges> c = new Comparator<HourlyRentalCharges>() {

			@Override
			public int compare(HourlyRentalCharges r1, HourlyRentalCharges r2) {

				Float hour1 = r1.getRentedHours();
				Float hour2 = r2.getRentedHours();
				return hour1.compareTo(hour2);
			}
		};

		listCharges.sort(c); 
		Float rentLatest ;
		HourlyRentalCharges returnCharges = null;
		Float rentPrev =(float) 0.0;
		for (HourlyRentalCharges r : listCharges) {

			rentLatest = r.getRentedHours(); 
			if( betweenExclusive((float)duration,rentPrev , rentLatest))
			{
				log.debug( "rent breaking"+ rentLatest);
				
				returnCharges = r;
				break;	
			}
				 
			rentPrev = rentLatest; 

		}
		log.debug("rent hours not found");		
		return returnCharges;
	}

	public static int getHours(DateTime dt1, DateTime dt2) {
		int m = Minutes.minutesBetween(dt1, dt2).getMinutes();

		if (m < 0)
			m = m * -1;

		float result = (float) m / 60;
		int h = Math.round(result);
		System.out.println("hhhhhmmmm duration ::: " + h);
		return h;
	}

	private Integer getMaxRentalTimeAllowedForADay() {
		// TODO Auto-generated method stub
		return 9;
	}

	
 
	
	
	public ResponseEntity<FoundTripResponseJsonObject> findTripByMember(
			 Long id) throws URISyntaxException
	{
		MemberMobile rentedMember = null;
		FoundTripResponseJsonObject foundTripResponseObject   =  new FoundTripResponseJsonObject() ;
		rentedMember = memberMobileRepository.findOne((long)  id);
		if (rentedMember == null) {
			foundTripResponseObject.setCode(399);
			foundTripResponseObject.setStatus("NOTOK");
			foundTripResponseObject.setTripid(0);
			foundTripResponseObject.setMessage("Not able to find member mobile");
			return ResponseEntity.accepted().body(foundTripResponseObject);
		 
			}
		RentalTrip r = rentalTripRepository.findTripByMember( rentedMember.getId() );
		if (r == null) {
			foundTripResponseObject.setCode(408);
			foundTripResponseObject.setStatus("NOTOK");
			foundTripResponseObject.setTripid(0);
			foundTripResponseObject.setMessage("Not able to find trip");
			return ResponseEntity.accepted().body(foundTripResponseObject);
		}
		
		Bicycle b  = r.getRentedCycle();
		String bicycleId =   b.getTagId();
		Long startNodeId = r.getStartNode().getId();
		foundTripResponseObject.setCode(200);
		foundTripResponseObject.setStatus("OK");
		foundTripResponseObject.setTripid(r.getId());
		foundTripResponseObject.setMessage("Trip Found ");
		
		StartTripMapper sTrip  = new StartTripMapper();
	    sTrip.setBicycleId(bicycleId);
	 	sTrip.setStartNodeId(startNodeId);
		sTrip.setRentalMemberId(rentedMember.getId());
		sTrip.setRentalStartTime(r.getRentStartTime());
		foundTripResponseObject.setTripMapper(sTrip);
		
		return ResponseEntity.accepted().body(foundTripResponseObject);
		
		
		
	}
	
	public ResponseEntity<StopTripResponseJsonObject> stopRentalTrip(
			 StopTripMapper stopTripMapperObj) throws URISyntaxException {

		StopTripResponseJsonObject stopTripResponseJsonObject = new StopTripResponseJsonObject();
		if (stopTripMapperObj == null) {
			log.debug("stopTripMapperObj is null");
			throw new IllegalArgumentException("A stopTripMapperObj is required");

		}

		RentalBufferNode startNode;
		startNode = rentalBufferNodeRepository.findOne((long) stopTripMapperObj.getStartNodeId());
		RentalBufferNode stopNode;
		stopNode = rentalBufferNodeRepository.findOne((long) stopTripMapperObj.getStopNodeId());
		if (startNode == null || stopNode == null) {
			stopTripResponseJsonObject.setCode(406);
			stopTripResponseJsonObject.setStatus("NOTOK");
			stopTripResponseJsonObject.setTripid(0);
			if (startNode == null)
				stopTripResponseJsonObject.setMessage("Not able to find START NODE");
			if (stopNode == null)
				stopTripResponseJsonObject.setMessage("Not able to find STOP NODE");
			return ResponseEntity.accepted().body(stopTripResponseJsonObject);
		}
		Bicycle rentedBicycle = bicycleRepository.findByTagId(stopTripMapperObj.getBicycleId());
		log.debug("stopTrip    :::rentedBicycle" + rentedBicycle);
		if (rentedBicycle == null) {
			stopTripResponseJsonObject.setCode(407);
			stopTripResponseJsonObject.setStatus("NOTOK");
			stopTripResponseJsonObject.setTripid(0);
			stopTripResponseJsonObject.setMessage("Not able to find cycle");
			return ResponseEntity.accepted().body(stopTripResponseJsonObject);
		}
		MemberMobile rentedMember = null;
		rentedMember = memberMobileRepository.findOne((long) stopTripMapperObj.getRentalMemberId());
		if (rentedMember == null) {
			stopTripResponseJsonObject.setCode(399);
			stopTripResponseJsonObject.setStatus("NOTOK");
			stopTripResponseJsonObject.setTripid(0);
			stopTripResponseJsonObject.setMessage("Not able to find member mobile");
			return ResponseEntity.accepted().body(stopTripResponseJsonObject);
		}

		ZonedDateTime startTimeZ = stopTripMapperObj.getRentalStartTime();
	//	DateTime startTimeJoda = new DateTime(startTime);
		
		DateTime startTimeJoda1=  new DateTime(
				startTimeZ.toInstant().toEpochMilli(),
			    DateTimeZone.forTimeZone(TimeZone.getTimeZone(startTimeZ.getZone())));

		ZonedDateTime stopTimeZ = stopTripMapperObj.getRentalStopTime();
		//DateTime stopTimeJoda = new DateTime(stopTime);
		
		DateTime stopTimeJoda1=  new DateTime(
				stopTimeZ.toInstant().toEpochMilli(),
			    DateTimeZone.forTimeZone(TimeZone.getTimeZone(stopTimeZ.getZone())));

		Integer rentedTimeInDays = Days.daysBetween(startTimeJoda1, stopTimeJoda1).getDays();
		Integer rentedTimeInHours = Hours.hoursBetween(startTimeJoda1, stopTimeJoda1).getHours();
		Integer rentedTimeInMinutes = Minutes.minutesBetween(startTimeJoda1, stopTimeJoda1).getMinutes();

		log.debug("rentedTimeInDays " + rentedTimeInDays);
		log.debug("rentedTimeInHours " + rentedTimeInHours);
		Integer rentedHoursInADay = 0;
		boolean applyDayFine = false;
		if (rentedTimeInDays > 0) {
			applyDayFine = true;
		} else if (rentedTimeInHours > getMaxRentalTimeAllowedForADay() || rentedTimeInHours > 24) {
			applyDayFine = true;
		} else {
			rentedHoursInADay = getHours(stopTimeJoda1, startTimeJoda1);
		}

		log.debug("rentedHoursInADay " + rentedHoursInADay);
		HourlyRentalCharges deductUnits = null;
		if (applyDayFine == false) {
			log.debug("rentedTimeInHours < getMaxRentalTimeForADay()");
			deductUnits = findHourlyRentalCharges(rentedHoursInADay);
		}

		RentalTrip r = rentalTripRepository.findTripByStartTimeAndCycleAndMember(rentedMember.getId(),
				rentedBicycle.getId());

		if (r == null) {
			stopTripResponseJsonObject.setCode(408);
			stopTripResponseJsonObject.setStatus("NOTOK");
			stopTripResponseJsonObject.setTripid(0);
			stopTripResponseJsonObject.setMessage("Not able to find trip");
			return ResponseEntity.accepted().body(stopTripResponseJsonObject);
		}
		r.setStopNode(stopNode);
		
		 
		ZonedDateTime zdt = stopTimeJoda1.toGregorianCalendar().toZonedDateTime();
	 	r.setRentStopTime(zdt);
		r.setFineApplied(applyDayFine);
		r.setRentalCharge(deductUnits);
//SAVE TRIP
		RentalTrip result = rentalTripRepository.saveAndFlush(r);
		
		Integer myCurrentRentUnits = rentedMember.getMyCurrentRentUnits();
		Integer myLatestRentalUnits = myCurrentRentUnits - deductUnits.getDeductableUnits();
		rentedMember.setMyCurrentRentUnits(myLatestRentalUnits );
		rentedMember.setBehaviorStatus(5);
// SAVE MEMBER		
		MemberMobile resultM = memberMobileRepository.saveAndFlush(rentedMember);
		
		
		stopTripResponseJsonObject.setCode(200);
		stopTripResponseJsonObject.setStatus("OK");
		stopTripResponseJsonObject.setTripid(result.getId());
		stopTripResponseJsonObject.setMessage("Trip Stopped ");

// SAVE CYCLE AND MAPPING TABLE
		cycleMovementService.moveCycleFromMemberToNode(rentedBicycle, rentedMember, stopNode );
		
		//stopTripResponseJsonObject.setDeductibeUnits(deductUnits.getDeductableUnits());
		//stopTripResponseJsonObject.setRentedHours(deductUnits.getRentedHours());
		return ResponseEntity.accepted().body(stopTripResponseJsonObject);

	}

	public ResponseEntity<StartTripResponseJsonObject> isMemberValid(Long memberId) {
		// TODO Auto-generated method stub
		MemberMobile rentedMember = null;
		
		StartTripResponseJsonObject startTripResponseJsonObject = new StartTripResponseJsonObject();
		 
		rentedMember = memberMobileRepository.findOne( memberId);
		if (rentedMember == null) {
			startTripResponseJsonObject.setCode(399);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Not able to find member mobile");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);
		} else if (this.checkIfMemberThief(rentedMember) == true) {
			startTripResponseJsonObject.setCode(401);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Member Thief");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);
		} else if (this.checkIfMemberBlackListed(rentedMember) == true) {
			startTripResponseJsonObject.setCode(402);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Member Black Listed");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);
		} else if (this.checkIfMemberDateExpired(rentedMember) == true) {
			startTripResponseJsonObject.setCode(403);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("Member Subscription Date Expired");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);

		} else if (this.checkIfMemberTakenCycles(rentedMember) == true) {
			startTripResponseJsonObject.setCode(404);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("member has taken cycle");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);

		} else if (this.checkIfMemberHasCurrency(rentedMember) == true) {
			startTripResponseJsonObject.setCode(405);
			startTripResponseJsonObject.setStatus("NOTOK");
			startTripResponseJsonObject.setTripid(0);
			startTripResponseJsonObject.setMessage("member has no currency");
			return ResponseEntity.accepted().body(startTripResponseJsonObject);

		}
		startTripResponseJsonObject.setCode(200);
		startTripResponseJsonObject.setStatus("OK");
		startTripResponseJsonObject.setTripid(0);
		startTripResponseJsonObject.setMessage("member valid");
		return ResponseEntity.accepted().body(startTripResponseJsonObject);
		

	}

	public List<MissingRentalTripDetails> findStolenTripsByDates( DateTime fromdate,  DateTime toDate) {
		   
		

		ZonedDateTime zdtFrom = ZonedDateTime.of(
			    fromdate.getYear(),
			    fromdate.getMonthOfYear(),
			    fromdate.getDayOfMonth(),
			    fromdate.getHourOfDay(),
			    fromdate.getMinuteOfHour(),
			    fromdate.getSecondOfMinute(),
			    fromdate.getMillisOfSecond() * 1_000_000,
			    ZoneId.of(fromdate.getZone().getID(), ZoneId.SHORT_IDS));
		
		ZonedDateTime zdtTo = ZonedDateTime.of(
				toDate.getYear(),
				toDate.getMonthOfYear(),
				toDate.getDayOfMonth(),
				toDate.getHourOfDay(),
				toDate.getMinuteOfHour(),
				toDate.getSecondOfMinute(),
			    toDate.getMillisOfSecond() * 1_000_000,
			    ZoneId.of(toDate.getZone().getID(), ZoneId.SHORT_IDS));
		 List<RentalTrip>  listTrips = rentalTripRepository.findMissingTripsByDates( zdtFrom, zdtTo);
		
		    
		 List<RentalTrip> stolenTrips =   findStolenTrips(listTrips);
		 log.debug("-findStolenTripsByDates" +stolenTrips.size());
		 List<MissingRentalTripDetails>  rDetails  =  getDetailsfromMissingTrips(stolenTrips);
		 return rDetails;
	}

	
	
	private List<RentalTrip > findStolenTrips(List<RentalTrip> listTrips) {
		 List<RentalTrip>  stolenList =  new  LinkedList<RentalTrip> ();
		
		for(RentalTrip r : listTrips)
		{
			Integer status   = r.getCustomer().getBehaviorStatus();
			if(status == 5) //stolen status
			{
				stolenList.add(r);
			}
		}
		
		return stolenList;
	}

	public List<MissingRentalTripDetails> findMissingTripsByDates(DateTime fromdate, DateTime toDate) {
	  
				ZonedDateTime zdtFrom = ZonedDateTime.of(
					    fromdate.getYear(),
					    fromdate.getMonthOfYear(),
					    fromdate.getDayOfMonth(),
					    fromdate.getHourOfDay(),
					    fromdate.getMinuteOfHour(),
					    fromdate.getSecondOfMinute(),
					    fromdate.getMillisOfSecond() * 1_000_000,
					    ZoneId.of(fromdate.getZone().getID(), ZoneId.SHORT_IDS));
				
				ZonedDateTime zdtTo = ZonedDateTime.of(
						toDate.getYear(),
						toDate.getMonthOfYear(),
						toDate.getDayOfMonth(),
						toDate.getHourOfDay(),
						toDate.getMinuteOfHour(),
						toDate.getSecondOfMinute(),
					    toDate.getMillisOfSecond() * 1_000_000,
					    ZoneId.of(toDate.getZone().getID(), ZoneId.SHORT_IDS));
		
		List<RentalTrip> missingTrips =  rentalTripRepository.findMissingTripsByDates( zdtFrom, zdtTo);
		 log.debug("-findMissingTripsByDates" +missingTrips.size());
		 List<MissingRentalTripDetails>  rDetails  =  getDetailsfromMissingTrips(missingTrips);
		 return rDetails;
		
	}

	
public List<RentalTripDetails> findCompleteTripsByDates(DateTime fromdate, DateTime toDate) 
{
		  
		ZonedDateTime zdtFrom = ZonedDateTime.of(
			    fromdate.getYear(),
			    fromdate.getMonthOfYear(),
			    fromdate.getDayOfMonth(),
			    fromdate.getHourOfDay(),
			    fromdate.getMinuteOfHour(),
			    fromdate.getSecondOfMinute(),
			    fromdate.getMillisOfSecond() * 1_000_000,
			    ZoneId.of(fromdate.getZone().getID(), ZoneId.SHORT_IDS));
		
		ZonedDateTime zdtTo = ZonedDateTime.of(
				toDate.getYear(),
				toDate.getMonthOfYear(),
				toDate.getDayOfMonth(),
				toDate.getHourOfDay(),
				toDate.getMinuteOfHour(),
				toDate.getSecondOfMinute(),
			    toDate.getMillisOfSecond() * 1_000_000,
			    ZoneId.of(toDate.getZone().getID(), ZoneId.SHORT_IDS));

		List<RentalTrip> completeTrips =  rentalTripRepository.findCompleteTripsByDates( zdtFrom, zdtTo);
		log.debug("-findMissingTripsByDates" +completeTrips.size());
		List<RentalTripDetails>  rDetails  =  getDetailsfromTrips(completeTrips);	
		return rDetails;

} 
	private List<RentalTripDetails> getDetailsfromTrips(List<RentalTrip> stolenTrips) {
		// TODO Auto-generated method stub
		
		List<RentalTripDetails> convList  = new LinkedList<RentalTripDetails>();
		
		 for(RentalTrip r:  stolenTrips)
		{
			 RentalTripDetails e = new RentalTripDetails();;
			 e.setId(r.getId());
			 e.setMemberid(r.getCustomer().getId());
			 e.setMemberName(r.getCustomer().getFirstName()+"-"+r.getCustomer().getFirstName());
			 e.setCycle(r.getRentedCycle().getTagId());
			 e.setStartNode(r.getStartNode().getStationName());
			 e.setStopNode(r.getStopNode().getStationName());
		 	 e.setStarttime(r.getRentStartTime());
		 	 e.setStopTime(r.getRentStopTime());
			 e.setUnits(r.getRentalCharge().getDeductableUnits() );
			 e.setDuration(r.getRentalCharge().getRentedHours() );
			 	 
			  convList.add(e );
			 
		}
		return convList;
	}
	
	
	private List<MissingRentalTripDetails> getDetailsfromMissingTrips(List<RentalTrip> stolenTrips) {
		// TODO Auto-generated method stub
		List<MissingRentalTripDetails> convList  = new LinkedList<MissingRentalTripDetails>();
		
		 for(RentalTrip r:  stolenTrips)
		{
			 MissingRentalTripDetails e = new MissingRentalTripDetails();;
			 e.setId(r.getId());
			 e.setMemberid(r.getCustomer().getId());
			 e.setMemberName(r.getCustomer().getFirstName()+"-"+r.getCustomer().getFirstName());
			 e.setCycle(r.getRentedCycle().getTagId());
			 e.setStartNode(r.getStartNode().getStationName());			 
			  e.setStarttime(r.getRentStartTime());
		// 
			  convList.add(e );
			 
		}
		 
		 return convList;
	}


}
