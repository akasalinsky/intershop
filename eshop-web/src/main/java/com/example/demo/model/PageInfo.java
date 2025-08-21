package com.example.demo.model;

public class PageInfo {
    private int pageSize;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public PageInfo(int pageSize, int currentPage, int totalPages, long totalElements) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public int getPageSize() { return pageSize; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPages() { return totalPages; }
    public long getTotalElements() { return totalElements; }

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }
}