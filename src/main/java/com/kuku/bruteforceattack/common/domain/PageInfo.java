package com.kuku.bruteforceattack.common.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageInfo {
    private Integer size;
    private Integer page;
    private String searchQuery;

    public Integer getPage() {
        return page + 1;
    }

    public Integer getSize() {
        return size;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public PageInfo(Integer page, Integer size, String searchQuery) {
        this.page = 0;
        if (page >= 1) {
            this.page = page - 1;
        }
        this.size = 25;
        if (size < 200 && size > 0) {
            this.size = size;
        }
        this.searchQuery = StringUtils.trimToEmpty(searchQuery);
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(page, size);
    }

    public PageRequest toPageRequest(Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    public List<Integer> calculatorSmartPage(int totalPages, int maxItem) {
        if (totalPages < 2) {
            return new ArrayList<>();
        }
        if (totalPages <= maxItem) {
            return IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        }

        int middleItem = Math.round(maxItem / 2f);

        if (page <= middleItem) {
            return IntStream.rangeClosed(1, maxItem).boxed().collect(Collectors.toList());
        }

        if (page > (totalPages - middleItem)) {
            return IntStream.rangeClosed(totalPages-maxItem, totalPages).boxed().collect(Collectors.toList());
        }

        return IntStream.rangeClosed(page - middleItem, page + middleItem).boxed().collect(Collectors.toList());
    }
}