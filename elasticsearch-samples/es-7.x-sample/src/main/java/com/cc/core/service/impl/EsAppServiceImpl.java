package com.cc.core.service.impl;

import com.cc.core.index.AppIndexDTO;
import com.cc.core.index.NestedArticle;
import com.cc.core.repository.AppRepository;
import com.cc.core.search.SearchAppDTO;
import com.cc.core.service.EsAppService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author gow
 * @date 2022/2/28
 */
@Component
public class EsAppServiceImpl implements EsAppService {

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    public void testTemplate() {
        Map<String, Object> mapping = elasticsearchRestTemplate.indexOps(NestedArticle.class).getMapping();
        System.out.println(mapping);
        Settings settings = elasticsearchRestTemplate.indexOps(NestedArticle.class).getSettings();
        System.out.println(settings);
    }

    @Override
    public Iterable<AppIndexDTO> batchInsert(List<AppIndexDTO> appIndexDTOList) {
        return appRepository.saveAll(appIndexDTOList);
    }

    @Override
    public SearchHits<AppIndexDTO> searchAppByPage(SearchAppDTO searchAppDTO) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(QueryBuilders.termQuery("status", 1));
        if (searchAppDTO.getUserId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", searchAppDTO.getUserId()));
        }
        if (searchAppDTO.getName() != null) {
            boolQueryBuilder.filter(QueryBuilders.wildcardQuery("name", searchAppDTO.getName() + "*"));
        }
        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("gmtModified").order(SortOrder.DESC))
                //es分页从page0开始
                .withPageable(PageRequest.of(searchAppDTO.getPage() - 1, searchAppDTO.getPageSize())).build();
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), AppIndexDTO.class);
    }

    public List<AppIndexDTO> searchByName(String name) {
        return appRepository.findByName(name);
    }

    public long countByName(String name) {
        return appRepository.countByName(name);
    }
}
