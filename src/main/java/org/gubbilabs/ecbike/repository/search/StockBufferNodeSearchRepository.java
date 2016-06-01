package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.StockBufferNode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the StockBufferNode entity.
 */
public interface StockBufferNodeSearchRepository extends ElasticsearchRepository<StockBufferNode, Long> {
}
