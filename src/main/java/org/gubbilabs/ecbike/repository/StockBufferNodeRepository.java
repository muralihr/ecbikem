package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.StockBufferNode;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StockBufferNode entity.
 */
public interface StockBufferNodeRepository extends JpaRepository<StockBufferNode,Long> {

    @Query("select stockBufferNode from StockBufferNode stockBufferNode where stockBufferNode.stockManager.login = ?#{principal}")
    List<StockBufferNode> findByStockManagerIsCurrentUser();

}
