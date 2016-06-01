package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.CycleToStockNodeMapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CycleToStockNodeMapper entity.
 */
public interface CycleToStockNodeMapperSearchRepository extends ElasticsearchRepository<CycleToStockNodeMapper, Long> {
}
