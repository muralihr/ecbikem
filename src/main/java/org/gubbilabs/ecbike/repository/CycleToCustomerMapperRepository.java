package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.CycleToCustomerMapper;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the CycleToCustomerMapper entity.
 */
public interface CycleToCustomerMapperRepository extends JpaRepository<CycleToCustomerMapper,Long> {
	
	public final static String FIND_BY_CYCLE_AND_CUSTOMER = "SELECT s " + 
            "FROM CycleToCustomerMapper s  " +
            "WHERE ( s.rentedCustomer.id = :memberid) AND (s.movedCycle.id =  :bicycleid) ";
	
 
	@Query(FIND_BY_CYCLE_AND_CUSTOMER)
	CycleToCustomerMapper  findEntryForCycleAtCustomer(@Param("memberid")   Long id ,@Param("bicycleid")  Long cycleid);
	

}
