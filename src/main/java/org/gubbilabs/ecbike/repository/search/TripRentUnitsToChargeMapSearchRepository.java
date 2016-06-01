package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.TripRentUnitsToChargeMap;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TripRentUnitsToChargeMap entity.
 */
public interface TripRentUnitsToChargeMapSearchRepository extends ElasticsearchRepository<TripRentUnitsToChargeMap, Long> {
}
