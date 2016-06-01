package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.BillingNode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BillingNode entity.
 */
public interface BillingNodeSearchRepository extends ElasticsearchRepository<BillingNode, Long> {
}
