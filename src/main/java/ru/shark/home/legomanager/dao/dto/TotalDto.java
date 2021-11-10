package ru.shark.home.legomanager.dao.dto;

public class TotalDto {
    private Long total;
    private Long inStock;

    public TotalDto() {
        // empty constructor
    }

    public TotalDto(Long total, Long inStock) {
        this.total = total;
        this.inStock = inStock;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getInStock() {
        return inStock;
    }

    public void setInStock(Long inStock) {
        this.inStock = inStock;
    }
}
