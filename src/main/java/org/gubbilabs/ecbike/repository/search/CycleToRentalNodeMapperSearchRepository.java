package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.CycleToRentalNodeMapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CycleToRentalNodeMapper entity.
 */
public interface CycleToRentalNodeMapperSearchRepository extends ElasticsearchRepository<CycleToRentalNodeMapper, Long> {
}
