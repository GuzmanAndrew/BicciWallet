package com.bankw.ms_transactions.model.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedResponseDto<T> {
    private List<T> content;
    private int currentPage;
    private long totalItems;
    private int totalPages;

    public PaginatedResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.currentPage = page.getNumber();
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
