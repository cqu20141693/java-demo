package com.cc.es;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableTest {
    public static void main(String[] args) {
        int page = 1;
        int size = 10;
        Sort sort = Sort.unsorted();
        Pageable pageRequest = PageRequest.of(page, size, sort);
    }
}
