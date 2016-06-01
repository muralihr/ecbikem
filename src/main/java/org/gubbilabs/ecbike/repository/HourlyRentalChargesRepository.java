package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.HourlyRentalCharges;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HourlyRentalCharges entity.
 */
public interface HourlyRentalChargesRepository extends JpaRepository<HourlyRentalCharges,Long> {

}
