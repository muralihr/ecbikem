package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.MemberBill;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MemberBill entity.
 */
public interface MemberBillRepository extends JpaRepository<MemberBill,Long> {

}
