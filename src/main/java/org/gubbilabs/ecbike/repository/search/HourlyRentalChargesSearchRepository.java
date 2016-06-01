package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.HourlyRentalCharges;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the HourlyRentalCharges entity.
 */
public interface HourlyRentalChargesSearchRepository extends ElasticsearchRepository<HourlyRentalCharges, Long> {
}
