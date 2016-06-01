package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.Bicycle;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bicycle entity.
 */
public interface BicycleRepository extends JpaRepository<Bicycle,Long> {
	
	
	Bicycle findByTagId(String s);	
	public final static String FIND_ALL_UNASSIGNEDED_CYCLE_QUERY = "SELECT b " + 
            "FROM Bicycle b  " +
            "WHERE ( b.moveStatus = 0)  ";	
 
	@Query(FIND_ALL_UNASSIGNEDED_CYCLE_QUERY)
	List<Bicycle> findAllUnassignedCycles();
}
