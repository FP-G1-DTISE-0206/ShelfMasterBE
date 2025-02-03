package com.DTISE.ShelfMasterBE.common.tools;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

public class Pagination {

    private static final int DEFAULT_START = 0;
    private static final int DEFAULT_LENGTH = 10;
    private static final String DEFAULT_SORT_FIELD = "id";
    private static final String DEFAULT_SORT_ORDER = "desc";

    public static Pageable createPageable(Integer start, Integer length, String sortField, String sortOrder) {
        start = (start != null && start >= 0) ? start : DEFAULT_START;
        length = (length != null && length > 0) ? length : DEFAULT_LENGTH;

        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : DEFAULT_SORT_FIELD;
        Sort.Direction direction = Sort.Direction.fromString(sortOrder != null ? sortOrder : DEFAULT_SORT_ORDER);

        Sort sort = Sort.by(direction, sortField);
        return PageRequest.of(start / length, length, sort);
    }

    public static Pageable createPageable(Integer start, Integer length) {
        return createPageable(start, length, DEFAULT_SORT_FIELD, DEFAULT_SORT_ORDER);
    }

    public static Pageable createPageable() {
        return createPageable(DEFAULT_START, DEFAULT_LENGTH, DEFAULT_SORT_FIELD, DEFAULT_SORT_ORDER);
    }

    public static <T> Map<String, Object> mapResponse(Page<T> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("recordsFiltered", data.getTotalElements());
        response.put("data", data.getContent());
        return response;
    }
}
