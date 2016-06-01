package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.CycleToCustomerMapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CycleToCustomerMapper entity.
 */
public interface CycleToCustomerMapperSearchRepository extends ElasticsearchRepository<CycleToCustomerMapper, Long> {
}
