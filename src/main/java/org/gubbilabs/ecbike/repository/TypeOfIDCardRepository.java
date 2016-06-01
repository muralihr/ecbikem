package org.gubbilabs.ecbike.repository;

import org.gubbilabs.ecbike.domain.TypeOfIDCard;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeOfIDCard entity.
 */
public interface TypeOfIDCardRepository extends JpaRepository<TypeOfIDCard,Long> {

}
