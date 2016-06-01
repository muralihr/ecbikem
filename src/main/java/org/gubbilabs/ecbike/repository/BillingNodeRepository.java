package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.BillingNode;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BillingNode entity.
 */
public interface BillingNodeRepository extends JpaRepository<BillingNode,Long> {

    @Query("select billingNode from BillingNode billingNode where billingNode.billCenterManager.login = ?#{principal}")
    List<BillingNode> findByBillCenterManagerIsCurrentUser();

}
