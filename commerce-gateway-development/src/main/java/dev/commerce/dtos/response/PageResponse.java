package dev.commerce.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
public class PageResponse<T> implements Serializable {
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private T items;
}
