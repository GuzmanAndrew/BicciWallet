package com.bankw.ms_transactions.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
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

}
