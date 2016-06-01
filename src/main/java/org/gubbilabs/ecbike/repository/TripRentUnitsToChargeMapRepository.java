package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.TripRentUnitsToChargeMap;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TripRentUnitsToChargeMap entity.
 */
public interface TripRentUnitsToChargeMapRepository extends JpaRepository<TripRentUnitsToChargeMap,Long> {

}
