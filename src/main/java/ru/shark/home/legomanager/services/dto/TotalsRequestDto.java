package ru.shark.home.legomanager.services.dto;

public class TotalsRequestDto {
    private Long userId;

    public TotalsRequestDto() {

    }

    public TotalsRequestDto(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
