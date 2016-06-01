package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.RentalBufferNode;
 
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RentalBufferNode entity.
 */
public interface RentalBufferNodeRepository extends JpaRepository<RentalBufferNode,Long> {

    @Query("select rentalBufferNode from RentalBufferNode rentalBufferNode where rentalBufferNode.nodeManager.login = ?#{principal}")
    List<RentalBufferNode> findByNodeManagerIsCurrentUser();
    

	RentalBufferNode findById(Long nodeId);
	public List<RentalBufferNode> findAllByOrderByIdAsc();
	 

}
