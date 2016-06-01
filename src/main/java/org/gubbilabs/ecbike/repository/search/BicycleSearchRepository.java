package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.Bicycle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bicycle entity.
 */
public interface BicycleSearchRepository extends ElasticsearchRepository<Bicycle, Long> {
}
