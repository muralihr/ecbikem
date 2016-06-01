package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.RentalTrip;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RentalTrip entity.
 */
public interface RentalTripSearchRepository extends ElasticsearchRepository<RentalTrip, Long> {
}
