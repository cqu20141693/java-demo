package com.cc.core.service;


import com.cc.core.index.AppIndexDTO;
import com.cc.core.search.SearchAppDTO;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface EsAppService {
    /**
     * 批量插入
     *
     * @param appIndexDTOList
     * @return
     */
    Iterable<AppIndexDTO> batchInsert(List<AppIndexDTO> appIndexDTOList);

    /**
     * 分页搜索app列表
     *
     * @param searchAppDTO
     * @return
     */
    SearchHits<AppIndexDTO> searchAppByPage(SearchAppDTO searchAppDTO);

    List<AppIndexDTO> searchByName(String name);

    long countByName(String name);
}
