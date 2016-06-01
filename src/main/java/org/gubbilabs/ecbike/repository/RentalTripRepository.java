package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.RentalTrip;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the RentalTrip entity.
 */
public interface RentalTripRepository extends JpaRepository<RentalTrip,Long> {
	
	public final static String FIND_BY_MEMBER_CYCLE_QUERY = "SELECT t " + "FROM RentalTrip t  "
			+ "WHERE ( t.rentStopTime = null) AND"
			+ "  (t.rentedCycle.id = :bicycleid) AND (t.customer.id = :memberId) ";

	@Query(FIND_BY_MEMBER_CYCLE_QUERY)
	RentalTrip findTripByStartTimeAndCycleAndMember(@Param("memberId") Long memberId, @Param("bicycleid") Long cycleId);

	public final static String FIND_BY_MEMBER = "SELECT t " + "FROM RentalTrip t  "
			+ "WHERE ( t.rentStopTime = null) AND" + "    (t.customer.id = :memberId) ";

	@Query(FIND_BY_MEMBER)
	RentalTrip findTripByMember(@Param("memberId") Long memberId);

	public static final String QUERY_TO_ANALYSE_SUCCESFUL_NODE_TRIP = "SELECT r " + "FROM RentalTrip r "
			+ "WHERE  r.rentStopTime IS NOT NULL AND r.rentStartTime BETWEEN :start AND :end";

	@Query(QUERY_TO_ANALYSE_SUCCESFUL_NODE_TRIP)
	List<RentalTrip> findCompleteTripsByDates(@Param("start") ZonedDateTime fromDate, @Param("end") ZonedDateTime toDate);

	public static final String QUERY_TO_ANALYSE_SUCCESFUL_NODE_TRIP_BY_NODE = "SELECT r " + "FROM RentalTrip r "
			+ "WHERE  r.rentStopTime IS NOT NULL AND r.startNode.id=:startNodeId AND r.rentStartTime BETWEEN :start AND :end";

	@Query(QUERY_TO_ANALYSE_SUCCESFUL_NODE_TRIP_BY_NODE)
	List<RentalTrip> findCompleteTripsByDatesFromANode(@Param("startNodeId") Long startNodeId,
			@Param("start") ZonedDateTime fromDate, @Param("end") ZonedDateTime toDate);

	public static final String FIND_MISSING_TRIPS_BY_DATES = "SELECT r " + "FROM RentalTrip r "
			+ "WHERE  r.rentStopTime IS NULL AND r.rentStartTime BETWEEN :start AND :end";

	@Query(FIND_MISSING_TRIPS_BY_DATES)
	List<RentalTrip> findMissingTripsByDates(@Param("start") ZonedDateTime fromDate, @Param("end") ZonedDateTime toDate);

}
