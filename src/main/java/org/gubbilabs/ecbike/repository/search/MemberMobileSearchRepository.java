package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.MemberMobile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MemberMobile entity.
 */
public interface MemberMobileSearchRepository extends ElasticsearchRepository<MemberMobile, Long> {
}
