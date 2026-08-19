package com.liquicool.common;

import lombok.Data;

@Data
public class PageResult<T> {

    private long total;
    private int page;
    private int size;
    private java.util.List<T> records;

    public PageResult(long total, int page, int size, java.util.List<T> records) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.records = records;
    }
}
