package org.gubbilabs.ecbike.repository.search;

import org.gubbilabs.ecbike.domain.TypeOfIDCard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeOfIDCard entity.
 */
public interface TypeOfIDCardSearchRepository extends ElasticsearchRepository<TypeOfIDCard, Long> {
}
