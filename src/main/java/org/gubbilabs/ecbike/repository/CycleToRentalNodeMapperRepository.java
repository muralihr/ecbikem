package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.CycleToRentalNodeMapper;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the CycleToRentalNodeMapper entity.
 */
public interface CycleToRentalNodeMapperRepository extends JpaRepository<CycleToRentalNodeMapper,Long> {
	
	
	List<CycleToRentalNodeMapper> findById(Long id);
	public final static String FIND_ALL_CYCLE_AT_NODE = "SELECT s " + 
            "FROM CycleToRentalNodeMapper s  " +
            "WHERE ( s.nodeDest.id = :nodeid)  ";
 
	@Query(FIND_ALL_CYCLE_AT_NODE)
	List<CycleToRentalNodeMapper> findAllCyclesAtNode(@Param("nodeid") Long id);
	public final static String FIND_BY_CYCLE_AND_NODE = "SELECT s " + 
            "FROM CycleToRentalNodeMapper s  " +
            "WHERE ( s.nodeDest.id = :nodeid) AND (s.movedCycle.id =  :bicycleid) ";	
 
	@Query(FIND_BY_CYCLE_AND_NODE)
	CycleToRentalNodeMapper  findEntryForCycleAtNode(@Param("nodeid")   Long id, @Param("bicycleid")  Long cycleid);
}
