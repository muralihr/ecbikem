package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.MemberBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MemberBill entity.
 */
public interface MemberBillSearchRepository extends ElasticsearchRepository<MemberBill, Long> {
}
