package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.CycleToStockNodeMapper;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the CycleToStockNodeMapper entity.
 */
public interface CycleToStockNodeMapperRepository extends JpaRepository<CycleToStockNodeMapper,Long> {
	
	List<CycleToStockNodeMapper> findById(Long id );

	List<CycleToStockNodeMapper> findByNodeDest(Long id);
	
	
	public final static String FIND_ALL_CYCLE_AT_STOCK = "SELECT s " + 
            "FROM CycleToStockNodeMapper s  " +
            "WHERE ( s.nodeDest.id = :stockid)  ";
	
 
	@Query(FIND_ALL_CYCLE_AT_STOCK)
	List<CycleToStockNodeMapper> findAllCyclesAtStock(@Param("stockid")   Long id);
	
	

	
	public final static String FIND_BY_CYCLE_AND_STOCK = "SELECT s " + 
            "FROM CycleToStockNodeMapper s  " +
            "WHERE ( s.nodeDest.id = :stockid) AND (s.movedCycle.id =  :bicycleid) ";
	
 
	@Query(FIND_BY_CYCLE_AND_STOCK)
	 CycleToStockNodeMapper  findEntryForCycleAtStock(@Param("stockid")   Long id ,@Param("bicycleid")  Long cycleid);
	
	

}
