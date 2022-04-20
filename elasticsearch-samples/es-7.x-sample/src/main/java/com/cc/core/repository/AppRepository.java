package com.cc.core.repository;

import com.cc.core.index.AppIndexDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AppRepository extends ElasticsearchRepository<AppIndexDTO, String> {
    long countByName(String name);

    List<AppIndexDTO> findByName(String name);

    /**
     * If you do not want to apply any sorting or pagination, use Sort.unsorted() and Pageable.unpaged().
     *
     * @param name
     * @param pageable
     * @return
     */
    Page<AppIndexDTO> findByName(String name, Pageable pageable);

    List<AppIndexDTO> findByName(String name, Sort sort);
}
