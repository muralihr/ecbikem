package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.MemberMobile;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MemberMobile entity.
 */
public interface MemberMobileRepository extends JpaRepository<MemberMobile,Long> {

}
