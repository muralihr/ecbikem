package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.RentalBufferNode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RentalBufferNode entity.
 */
public interface RentalBufferNodeSearchRepository extends ElasticsearchRepository<RentalBufferNode, Long> {
}
